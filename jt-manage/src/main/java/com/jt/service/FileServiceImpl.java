package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.EasyUIFile;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {
	
	@Value("${localDirPath}")
	private String localDirPath;  //本地磁盘根目录       D:xxxXXXXXXXXXXXXXX/yyyy/MM/dd/uuid.jpg
	@Value("${urlPath}")
	private String urlPath;		  //配置网络访问路径: http://image.jt.com/yyyy/MM/dd/uuid.jpg

	/**
	 * 1.文件校验
	 * 	 1.1 校验是否为正常的图片 png jpg gif xxxx
	 * 		开始位置 	^
	 * 		结束位置	$
	 * 		任意次    	*
	 * 		一次或者多次  +
	 * 		0或者1次         ?
	 * 		任意字符         .
	 * 	 1.2 校验是否为木马病毒.
	 * 2.准备多个文件夹 分文件保存.
	 * 	 按照时间进行文件的拆分. yyyy/MM/dd
	 * 3.修改文件名称
	 * 	 3.1 利用毫秒数+随机数(1000)
	 * 	 3.2 UUID能够保证在同一毫秒内,生成的值不同.
	 * 		 UUID是一个32位的16进制数
	 * 		 2^128种可能结果
	 * 4.动态获取配置文件数据,为属性赋值
	 * 	 4.1 spring容器加载properties配置文件
	 * 	 4.2 利用{@value}注解赋值
	 * 5.实现图片回显
	 * 	 5.1 上传的图片保存在本地磁盘中,但是其他用户无法
	 * 通过本地磁盘空间地址访问该图片.如何实现所有用户统一
	 * 访问???
	 * 	 5.2 通过网络虚拟路径,实现图片正确访问!!!
	 * 	
	 */
	@Override
	public EasyUIFile fileUpload(MultipartFile uploadFile) {
		EasyUIFile easyUIFile = new EasyUIFile();
		//1.获取图片的名称   abc.jpg/ABC.JPG
		String fileName = uploadFile.getOriginalFilename();
		//2.将名称统统小写   abc.jpg
		fileName = fileName.toLowerCase();
		//3.判断图片类型是否正确
		if(!fileName.matches("^.+\\.(png|jpg|gif)$")){
			//表示不是图片类型 
			easyUIFile.setError(1);
			return easyUIFile;
		}
		
		//4.校验是否为木马病毒 木马.exe.jpg
		//将图片信息转化为图片对象 之后获取其中的数据再判断
		try {
			BufferedImage image = ImageIO.read(uploadFile.getInputStream());
			int width = image.getWidth();
			int height = image.getHeight();
			if(width == 0 || height == 0) {
				//该文件不是图片
				easyUIFile.setError(1);
				return easyUIFile;
			}
			
			//如果程序执行到这里.表示校验成功!!!之后进行文件上传
			String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
			//5.D:/1-JT/jt-upload/2019/05/05
			String dirPath = localDirPath + datePath;
			File dirFile = new File(dirPath);
			if(!dirFile.exists()) {
				//如果文件夹不存在则创建文件夹
				dirFile.mkdirs();
			}
			
			//6.生成文件名称禁止重复 UUID+文件类型
			String uuid = UUID.randomUUID()
							  .toString()
							  .replace("-","");
							  //abc.jpg
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			String realFileName = uuid + fileType;
			//7.实现文件上传  D:/1-JT/jt-upload/2019/05/05/uuid.jpg
			File realFile = new File(dirPath +"/"+realFileName);
			uploadFile.transferTo(realFile);
			easyUIFile.setHeight(height)
					  .setWidth(width);
			
			//8.拼接url虚拟访问地址  
			//http://image.jt.com/yyyy/MM/dd/
			String realUrlPath = urlPath + datePath +"/" + realFileName;
			easyUIFile.setUrl(realUrlPath);
		} catch (Exception e) {
			e.printStackTrace();
			easyUIFile.setError(1);
		}
		return easyUIFile;
	}
}
