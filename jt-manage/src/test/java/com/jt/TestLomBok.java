package com.jt;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.pojo.User;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class TestLomBok {
	
	//测试lomBok
	public void testLomBok() {
		User user = new User();
		user.setId(100)
			.setName("葵花宝典")
			.setAge(19)
			.setSex("其他");
			
	}
	
	
	
	
	
	
	
	
	
	

}
