package com.jt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUIData;
import com.jt.vo.SysResult;

//EasyUI要求返回的数据都是json
@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * 1.根据页面要求进行分页查询
	 * http://localhost:8091/item/query?page=1&rows=20
	 */
	@RequestMapping("/query")
	public EasyUIData findItemByPage
	(Integer page,Integer rows) {

		return itemService.findItemByPage(page,rows);
	}

	//实现商品新增 商品描述新增
	@RequestMapping("/save")
	public SysResult saveItem(Item item,String desc) {
		try {
			//同时入库2张表,完成商品/商品新增入库
			itemService.saveItem(item,desc);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}

	/**
	 * 实现商品详情回显
	 * 如果调用报错了,必须返回201
	 * 如果调用成功  返回200,并且data
	 * 
	 * 方式1:try-catch
	 * 方式2:全局异常处理机制  复习!!! aop
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById
	(@PathVariable Long itemId) {
		try {
			ItemDesc itemDesc = 
					itemService.findItemDescById(itemId);
			return SysResult.ok(itemDesc);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}

	//实现商品更新操作
	@RequestMapping("/update")
	public SysResult updateItem(Item item,String desc){
		try {
			itemService.updateItem(item,desc);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	/**
	 * String args = request.getParameter("ids");
		String[] arrayArgs = args.split(",");
		Long[] longAgs = new Long[arrayArgs.length];
		for(int i=0;i<longAgs.length;i++) {
			longAgs[i] = Long.parseLong(arrayArgs[i]);
		}
	 * @param request
	 * @param ids
	 * @return
	 */
	//实现商品下架
	@RequestMapping("/instock")
	public SysResult instock(Long[] ids) {
		try {
			int status = 2;	//下架			
			itemService.updateStatus(ids,status);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}


	//实现商品上架处理 /item/reshelf
	@RequestMapping("/reshelf")
	public SysResult reshelf(Long[] ids) {
		try {
			int status = 1;	//上架			
			itemService.updateStatus(ids,status);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	
	//实现商品的删除
	@RequestMapping("/delete")
	public SysResult deleteItems(Long[] ids) {
		try {
			
			itemService.deleteItems(ids);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	
	
	
	
	
	
}
