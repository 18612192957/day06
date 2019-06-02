package com.jt.service;

import java.util.List;

import com.jt.pojo.User;

public interface UserService {
	
	//查询商品列表数据
	List<User> findAll();
}
