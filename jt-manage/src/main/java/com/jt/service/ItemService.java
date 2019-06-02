package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUIData;

public interface ItemService {

	EasyUIData findItemByPage(Integer page, Integer rows);

	void saveItem(Item item, String desc);

	ItemDesc findItemDescById(Long itemId);

	void updateItem(Item item, String desc);

	void updateStatus(Long[] ids, Integer status);

	void deleteItems(Long[] ids);
	
	//前台通过id获取商品信息
	Item findItemById(Long itemId);

}
