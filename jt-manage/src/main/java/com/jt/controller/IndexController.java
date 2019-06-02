package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {
	
	/**
	 * 能否实现页面的通用跳转???
	 * 原则: 用户的请求路径就是跳转页面名称
	 * 思路:如果能够动态的获取url中的参数,则直接跳转页面
	 * 实现策略:restFul风格
	 * 
	 * 实现步骤:
	 * 	1.url中的参数必须使用{} 进行包裹
	 * 	2.参数的位置必须固定的.因为后台通过位置接收参数
	 *  3.必须使用@PathVariable注解转化
	 */
	@RequestMapping("/page/{moduleName}")
	public String item_add(@PathVariable String moduleName) {
		
		return moduleName;
	}
	
	
	//为了测试负载均衡添加该方法
	@RequestMapping("/getMsg")
	@ResponseBody
	public String getMsg() {
		
		return "我是tomcat服务器:8091!!!!!!!";
	}
	
	
	
	
	
}
