package com.jt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jt.pojo.User;

@SpringBootApplication		 //标识主启动类
@MapperScan("com.jt.mapper") //将Mybatis的mapper接口交给spring容器管理
public class SpringBootRun {
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootRun.class, args);
	}
}
