package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	
	//根据商品分类信息查询商品名称
	@RequestMapping("/queryItemCatNameById")
	public String findItemCatNameById(Long itemCatId) {
		
		return itemCatService.findItemCatNameById(itemCatId);
	}
	
	/**
	 * 实现商品分类目录展现
	 * 根据parentId查询数据信息
	 * @RequestParam 该注解,表示接受用户参数
	 * 当参数名称与接受参数名称不一致时才使用
	 * 可以设定默认值
	 * 
	 * 参数介绍:
	 * value/name = url中的参数名称
	 * defaultValue 默认值 
	 * required     是否为必填项 默认false
	 * @return
	 */
	@RequestMapping("/list")
	public List<EasyUITree> 					//url?id=100
			findItemCatByParentId(@RequestParam(value = "id",defaultValue ="0")Long parentId){
		
		return itemCatService.findCacheItemCatByParentId(parentId);
		//return itemCatService.findItemCatByParentId(parentId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
