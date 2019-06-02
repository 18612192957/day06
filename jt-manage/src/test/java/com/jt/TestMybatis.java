package com.jt;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class TestMybatis {
	
	//Spring容器为mapper接口创建代理对象
	@Autowired
	private UserMapper userMapper;
	
	@Test
	public void testFindAll() {
		List<User> userList = 
				userMapper.findAll();
		System.out.println(userList);
	}
}
