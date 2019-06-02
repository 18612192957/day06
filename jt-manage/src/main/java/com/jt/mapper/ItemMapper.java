package com.jt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;

public interface ItemMapper extends BaseMapper<Item>{
	@Select("select * from tb_item order by updated desc limit #{start},#{rows}")
	List<Item> selectItemByPage(@Param("start")Integer start,@Param("rows")Integer rows);
	
	/**
	 *   该方法为测试方法,为复习准备
	 * @param idsList
	 *   关于mybatis取值要求:
	 * 	mybatis中要求传参必须为单值.
	 * 	如果需要进行多值的传递.则必须将多值封装为单值.
	 * 	1.封装为pojo对象
	 *  2.可以封装为集合 array  list
	 *  3.可以封装为map  Map对象   利用@Param("key")
	 */
	void deteleItems(List<Long> idsList);

}
