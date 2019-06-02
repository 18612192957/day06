package com.jt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.Dog;
import com.jt.pojo.User;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;

public class TestRedis {
	
	//1.测试String类型
	@SuppressWarnings("deprecation")
	@Test
	public void testString() throws InterruptedException {
		String host = "192.168.35.130";
		int port = 6379;
		Jedis jedis = new Jedis(host, port);
		jedis.set("1901","redis单台测试");
		jedis.pexpire("1901", 10000);
		Thread.sleep(2000);
		System.out.println("当前key还能存活"
		+ jedis.ttl("1901")+"秒");
		System.out.println(jedis.get("1901"));
	}
	
	
	/**
	 * 2.测试hash 保存对象,但是返回值需要手动转化
	 * setnx作用:
	 * 	 当进行setnx操作时
	 * 		如果redis缓存中没有该数据,则进行set赋值操作.
	 *      如果已经有该数据.则set操作省略.
	 * 作用:在高并发下,减少脏数据.   
	 */
	
	@Test
	public void testHash() {
		String host = "192.168.35.130";
		int port = 6379;
		Jedis jedis = new Jedis(host, port);
		//测试前,先删除数据
		jedis.hsetnx("student", "id", "101"); 
		jedis.hsetnx("student", "age", "18");
		jedis.hsetnx("student", "age", "20");
		/*
		 * jedis.hset("student", "id", "101"); jedis.hset("student", "age", "18");
		 * jedis.hset("student", "age", "20");
		 */
		System.out.println
		(jedis.hget("student", "id")+"学号 |"
		+jedis.hget("student", "age")+"年龄");
		Map<String,String> map = jedis.hgetAll("student");
		System.out.println(map);
	}
	
	@Test
	public void testTx() {
		String host = "192.168.35.130";
		int port = 6379;
		Jedis jedis = new Jedis(host, port);
		Transaction transaction  = jedis.multi();	//开启事务
		try {
		transaction.set("qqq", "qqqqq");
		transaction.set("www", "wwwwww");
		transaction.exec();	//事务提交
		} catch (Exception e) {
			e.printStackTrace();
			transaction.discard();	//事务回滚
		}
	}
	
	//对象如何与redis进行交互????
	//1.有对象的格式要求  2.本身就是String串   JSON
	@SuppressWarnings("unchecked")//压制警告
	@Test
	public void testObject() throws IOException {
		String host = "192.168.35.130";
		int port = 6379;
		Jedis jedis = new Jedis(host, port);
		
		//实现对象与json互转 ObjectMapper对象
		ObjectMapper mapper = new ObjectMapper();
		
		//1.对象转化为json
		User user = new User();
		user.setId(1).setAge(19).setName("tomcat猫").setSex("公");
		String userJSON = mapper.writeValueAsString(user);
		System.out.println(userJSON);
		
		//2.json串转化为对象
		User user2 = mapper.readValue(userJSON, User.class);
		System.out.println(user2);
		
		//3.将List集合与json互转
		List<User> userList = new ArrayList<User>();
		User user3 = new User();
		user3.setId(1).setAge(19).setName("tomcat猫").setSex("公");
		User user4 = new User();
		user4.setId(2).setAge(20).setName("tomcat猫").setSex("公");
		userList.add(user3);
		userList.add(user4);
		//3.list集合转化为json串
		String listJSON = mapper.writeValueAsString(userList);
		System.out.println(listJSON);
		
		//4.json串转化为List集合
		List<User> userList2 = mapper.readValue(listJSON,userList.getClass());
		System.out.println(userList2);
	}
	
	//测试json转化调用get方法
	@Test
	public void testGet() throws JsonProcessingException {
		Dog dog = new Dog(111, "狗狗狗");
		ObjectMapper object = new ObjectMapper();
		String json = 
				object.writeValueAsString(dog);
		System.out.println("获取json:"+json);
	}
	
	
	
	//实现redis分片 用户使用redis是一个整体
	@Test
	public void testShards() {
		//将多台redis放入集合中
		List<JedisShardInfo> shards = new ArrayList<>();
		JedisShardInfo info1 = new JedisShardInfo("192.168.35.130", 6379);
		JedisShardInfo info2 = new JedisShardInfo("192.168.35.130", 6380);
		JedisShardInfo info3 = 
				new JedisShardInfo("192.168.35.130", 6381);
		shards.add(info1);
		shards.add(info2);
		shards.add(info3);
		ShardedJedis jedis = new ShardedJedis(shards);
		
		for(int i=0;i<10;i++) {
			jedis.set(""+i, ""+i);
		}
		
		/*
		 * jedis.set("1901", "tomcat猫"); System.out.println(jedis.get("1901"));
		 */
	}
	
	
	/*实现redis哨兵请求  IP:端口*/
	@Test
	public void testSentinel() {
		String masterName = "mymaster";	//主节点变量名称
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.35.130:26379");
		JedisSentinelPool pool = 
				new JedisSentinelPool(masterName, sentinels);
		Jedis jedis = pool.getResource();
		jedis.set("1901", "哨兵搭建完成!!!!");
		System.out.println(jedis.get("1901"));
		jedis.close();	//关闭连接
	}
	
	@Test
	public void testRedisCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.35.130",7000));
		nodes.add(new HostAndPort("192.168.35.130",7001));
		nodes.add(new HostAndPort("192.168.35.130",7002));
		nodes.add(new HostAndPort("192.168.35.130",7003));
		nodes.add(new HostAndPort("192.168.35.130",7004));
		nodes.add(new HostAndPort("192.168.35.130",7005));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("1901", "redis集群搭建完成!!!!");
		System.out.println(jedisCluster.get("1901"));
	}
	
	
	
	
	
	
	
	
	
	
	
}
