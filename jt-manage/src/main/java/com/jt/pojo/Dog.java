package com.jt.pojo;

import lombok.Data;

public class Dog {
	
	private Integer id;
	private String name;
	public Dog(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
