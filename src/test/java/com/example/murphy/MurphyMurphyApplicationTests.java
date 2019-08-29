package com.example.murphy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import top.lishaobo.example.entity.User;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MurphyMurphyApplicationTests {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired(required = false)
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		RedisSerializer stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);
		//redisTemplate.setValueSerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		//redisTemplate.setHashValueSerializer(stringSerializer);
		this.redisTemplate = redisTemplate;
	}


	@Test
	public void contextLoads() {

	}

	@Test
	public void redisTester() {
		Jedis jedis = new Jedis("localhost", 6379, 100000);
		int i = 0;
		try {
			long start = System.currentTimeMillis();// 开始毫秒数
			while (true) {
				long end = System.currentTimeMillis();
				if (end - start >= 1000) {// 当大于等于1000毫秒（相当于1秒）时，结束操作
					break;
				}
				i++;
				jedis.set("test" + i, i + "");
			}
		} finally {// 关闭连接
			jedis.close();
		}
		// 打印1秒内对Redis的操作次数
		System.out.println("redis每秒操作：" + i + "次");
	}

	@Test
	public void redisTest() {
		stringRedisTemplate.opsForValue().set("aaa", "111");
		Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
	}

	@Test
	public void test() {
		User user = new User();
		user.setName("andersen");


		//传值
		redisTemplate.opsForValue().set("user1", user);
		//取值
		User user1 = (User) redisTemplate.opsForValue().get("user1");
		System.out.println(user1.getName());
	}

	@Test
	public void listPushResitTest() {
		// leftPush依次由右边添加
		stringRedisTemplate.opsForList().rightPush("myList", "1");
		stringRedisTemplate.opsForList().rightPush("myList", "2");
		stringRedisTemplate.opsForList().rightPush("myList", "A");
		stringRedisTemplate.opsForList().rightPush("myList", "B");
		// leftPush依次由左边添加
		stringRedisTemplate.opsForList().leftPush("myList", "0");
	}

	@Test
	public void listGetListResitTest() {
		// 查询类别所有元素
		List<String> listAll = stringRedisTemplate.opsForList().range("myList", 0, -1);
		System.out.println(listAll.toString());
		// 查询前3个元素
		List<String> list = stringRedisTemplate.opsForList().range("myList", 0, 3);
		System.out.println(list.toString());
	}

	@Test
	public void listRemoveOneResitTest() {
		// 删除先进入的B元素
		stringRedisTemplate.opsForList().remove("myList", 1, "B");
	}

	@Test
	public void listRemoveAllResitTest() {
		// 删除所有A元素
		stringRedisTemplate.opsForList().remove("myList", 0, "A");
	}

}
