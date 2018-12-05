package com.cn.taiji.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cn.taiji.demo.pojo.User;
import com.cn.taiji.demo.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JpaRepositoryApplicationTests {
	private static final Logger log = LoggerFactory.getLogger(JpaRepositoryApplicationTests.class);
	@Inject
	 UserService userService;
	
	@Inject
	private ObjectMapper objectMapper;
	@Test
	public void contextLoads() {
		for(int i=0;i<10;i++) {
			User user=new User();
			user.setSid("6");
			user.setUser("xul");
			user.setPass("123456");
			user.setAge(5);
			userService.saveUser(user);
		}
		List<User> users=userService.findAllUsers();
		System.out.println(users.size());
	}

	
	@Ignore
    @Test
    public void deltest() {
		int id=3;
		userService.deleteUserById(id);;
		System.out.println("success");
		List<User> users=userService.findAllUsers();
		System.out.println(users.size());
	}
	@Ignore
    @Test
    public void updtest() {
		int id=2;
		userService.updateUserAge(id);
		User user=userService.findUserById(2);
		System.out.println(user);
	}
	
	@Ignore
    @Test
    public void seltest() {
		List<User> users=userService.findAllUsers();
		System.out.println(users.toString());
	}
	
	@Ignore
    @Test
    public void findByIdAndNameIsNullOrderByAgeAsc() {
		int id=4;
		String name="xu";
		List<User> users=userService.findByIdAndNameIsNullOrderByAgeAsc(id,name);
		System.out.println(users.toString());
	}
	

	@Ignore
	@Test
	public void testPage() {
		String map = "{\"page\" : 1,\"pageSize\" : 5, \"filter\":{ \"filters\":[{ \"field\" : \"deptName\", \"value\":\"机构8\"}]}}";
		Map searchParameters = new HashMap();
		try {
			searchParameters = objectMapper.readValue(map, new TypeReference<Map>() {
			});
		} catch (JsonParseException e) {
			log.error("JsonParseException{}:", e.getMessage());
		} catch (JsonMappingException e) {
			log.error("JsonMappingException{}:", e.getMessage());
		} catch (IOException e) {
			log.error("IOException{}:", e.getMessage());
		}

		Map mapDept = userService.getPage(searchParameters);

		System.out.println(mapDept.get("total"));

		System.out.println(mapDept.get("depts"));
	}
}
