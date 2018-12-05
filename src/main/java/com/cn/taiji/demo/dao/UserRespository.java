package com.cn.taiji.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cn.taiji.demo.pojo.User;

public interface UserRespository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    //根据id删除
	@Query(value="select  u from User u where u.id=:id")
	List<User> findUserById(@Param("id") int id);
   
	//修改
	@Modifying
	@Query("update User u set u.age=100 where u.id=:id")
	void updateUserAge(@Param("id") int id);
	
	//删除
	@Modifying
	@Query(value = "delete from User u where u.id=:id ")
	 void deleteUserById(@Param("id") int id);
	
	//修改
	@Query(value = "update User u set u.user=:user where u.id=:id")
	@Modifying
	 void updateStormManageState(@Param("user") String user,@Param("id") int id);
	
	
	
	// 不需要使用SQL语句
	 List<User> findByIdAndUserIsNullOrderByAgeAsc(@Param("id") int i,@Param("user") String username);
	
}
