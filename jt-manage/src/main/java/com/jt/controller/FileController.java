package com.jt.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.multi.MultiPanelUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.EasyUIFile;

@Controller
public class FileController {
	
	@Autowired
	private FileService fileService;

	/*
	 * 	文件上传步骤
	 * 	1.获取文件名称
	 *  2.准备文件上传路径
	 *  3.判断文件夹是否存在
	 *  4.实现文件上传
	 * */
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws IllegalStateException, IOException {
		//获取的input标签中name属性的名称
		String name = fileImage.getName();
		//获取的是文件真实名称
		String fileName = fileImage.getOriginalFilename();
		Long size = fileImage.getSize();
		System.out.println(name);
		System.out.println(fileName);
		System.out.println(size);
		
		File dirFile = new File("E:/1-JT/jt-upload");
		
		if(!dirFile.exists()) {
			//如果文件夹不存在,则创建文件夹
			dirFile.mkdirs(); //创建多级目录
			//dirFile.mkdir();  //创建一级目录
		}
		//4.实现文件上传
		File file = new File("E:/1-JT/jt-upload/"+fileName);
		fileImage.transferTo(file);
		//默认的转发才会执行视图解析器代码
		return "redirect:/file.jsp";
	}
	
	//实现文件上传 富文本编辑器进行文件上传
	@RequestMapping("/pic/upload")
	@ResponseBody
	public EasyUIFile fileUpload(MultipartFile uploadFile) {
		
		return fileService.fileUpload(uploadFile);
	}
}