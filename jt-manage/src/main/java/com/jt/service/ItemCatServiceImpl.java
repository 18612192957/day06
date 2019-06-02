package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	//返回的是商品分类名称
	@Override
	public String findItemCatNameById(Long itemCatId) {
		
		ItemCat itemCat = itemCatMapper.selectById(itemCatId);
		
		return itemCat.getName();
	}

	/**
	 * 1.根据parentId查询ItemCatList
	 * 2.循环遍历/itemCat~~~~EasyUITree
	 */
	@Override
	public List<EasyUITree> findItemCatByParentId(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = 
				new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		//根据父级查询子级信息
		List<ItemCat> itemCatList = 
				itemCatMapper.selectList(queryWrapper);
		//将数据进行转化
		List<EasyUITree> treeList = new ArrayList<>();
		for (ItemCat itemCat : itemCatList) {
			Long id = itemCat.getId();
			String text = itemCat.getName();
			String state = 
					itemCat.getIsParent() ? "closed" : "open";
			EasyUITree tree = 
					new EasyUITree(id, text, state);
			treeList.add(tree);
		}
		
		return treeList;
	}
	
	/**
	 * key生成策略:
	 * 	每一个数据都应该有自己特定的key
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EasyUITree> findCacheItemCatByParentId(Long parentId) {
		List<EasyUITree> treeList = new ArrayList<>();
		String key = "ITEM_CAT_"+parentId;
		String jsonResult = jedisCluster.get(key);
		if(StringUtils.isEmpty(jsonResult)) {
			//缓存中没有数据.
			treeList = findItemCatByParentId(parentId);
			//将list集合转化为json串    对象转化为json必然调用get方法
			String json = ObjectMapperUtil.toJSON(treeList);
			//赋值操作并且添加超时时间
			jedisCluster.setex(key, 3600*24*7, json);
			System.out.println("查询数据库~!!!!!!!!");
		}else {
			
			treeList = ObjectMapperUtil.toObject(jsonResult, treeList.getClass());
			System.out.println("查询redis缓存!!!!!!");
		}
		
		return treeList;
	}	
}
