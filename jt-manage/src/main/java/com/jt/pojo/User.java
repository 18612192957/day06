package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data						//添加get/set/toString
@Accessors(chain = true)	//开启链式加载
@TableName
public class User {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private String name;
	private Integer age;
	private String sex;
	
}
