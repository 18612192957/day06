package com.jt.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.User;
public interface UserMapper extends BaseMapper<User>{
	
	//添加查询操作
	List<User> findAll();
}
