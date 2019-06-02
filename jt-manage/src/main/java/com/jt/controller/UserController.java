package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.pojo.User;
import com.jt.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("/findAll")
	public String findAll(Model model) {
		List<User> userList = 
				userService.findAll();
		//利用model使用request域添加数据到前台
		model.addAttribute("userList", userList);
		return "userList";
	}
}
