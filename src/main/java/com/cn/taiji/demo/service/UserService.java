package com.cn.taiji.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cn.taiji.demo.dao.UserRespository;
import com.cn.taiji.demo.pojo.User;

@Service
public class UserService  {
	@Inject
 private UserRespository userRespository;
 
  public final String roleIdKey = "5b66ecf45d634159a08468898b1b3217";
  // 保存用户
  @Transactional(propagation = Propagation.REQUIRED)
	public void saveUser(User user) {
		this.userRespository.saveAndFlush(user);
	}
  //查找所有用户
  @Transactional(propagation = Propagation.SUPPORTS)
	public List<User> findAllUsers() {
		List<User> users = this.userRespository.findAll();
		 
		System.out.println(users.size()+"--------");
		return users;
	}
  //根据Id查找用户
  public User findUserById(int id) {
//		User user = this.userRespository.findById(id);
	  User user = this.userRespository.findOne(id);
		return user;
	}
  //根据ID删除
  public void deleteUserById(int id) {
	  this.userRespository.deleteUserById(id);;
  };
  // 修改
  public void updateUserAge(int id) {
	  this.userRespository.updateUserAge(id);
  };
  
  // 不需要使用SQL语句
  public List<User> findByIdAndNameIsNullOrderByAgeAsc( int i,String name) {
	  List<User> users=this.userRespository.findByIdAndUserIsNullOrderByAgeAsc(i,name);
	  return users;
}
  //分页
  @Transactional(propagation = Propagation.SUPPORTS)
	public Map getPage(final Map searchParameters) {
		Map map = new HashMap();
		int page = 0;
		int pageSize = 10;
		Page<User> pageList;
		if (searchParameters != null && searchParameters.size() > 0 && searchParameters.get("page") != null) {
			page = Integer.parseInt(searchParameters.get("page").toString()) - 1;
		}
		if (searchParameters != null && searchParameters.size() > 0 && searchParameters.get("pageSize") != null) {
			pageSize = Integer.parseInt(searchParameters.get("pageSize").toString());
		}
		if (pageSize < 1)
			pageSize = 1;
		if (pageSize > 100)
			pageSize = 100;
		List<Map> orderMaps = (List<Map>) searchParameters.get("sort");
		List<Order> orders = new ArrayList<Order>();
		if (orderMaps != null) {
			for (Map m : orderMaps) {
				if (m.get("field") == null)
					continue;
				String field = m.get("field").toString();
				if (!StringUtils.isEmpty(field)) {
					String dir = m.get("dir").toString();
					if ("DESC".equalsIgnoreCase(dir)) {
						orders.add(new Order(Direction.DESC, field));
					} else {
						orders.add(new Order(Direction.ASC, field));
					}
				}
			}
		}
		PageRequest pageable;
		if (orders.size() > 0) {
			pageable = new PageRequest(page, pageSize, new Sort(orders));
		} else {
			Sort sort = new Sort(Direction.ASC, "age");
			pageable = new PageRequest(page, pageSize, sort);
		}
		Map filter = (Map) searchParameters.get("filter");
		if (filter != null) {
			final List<Map> filters = (List<Map>) filter.get("filters");
			Specification<User> spec = new Specification<User>() {
				@Override
				public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> pl = new ArrayList<Predicate>();
					for (Map f : filters) {
						String field = f.get("field").toString().trim();
						String value = f.get("value").toString().trim();
						if (value != null && value.length() > 0) {
							if ("deptName".equalsIgnoreCase(field)) {
								pl.add(cb.like(root.<String>get(field), value + "%"));
							} else if ("deptType".equalsIgnoreCase(field)) {
								pl.add(cb.like(root.<String>get(field), value + "%"));
							} else if ("deptUrl".equalsIgnoreCase(field)) {
								pl.add(cb.like(root.<String>get(field), value + "%"));
							} 
						}

					}
					// 查询出未删除的
					pl.add(cb.equal(root.<Integer>get("flag"), 1));
					return cb.and(pl.toArray(new Predicate[0]));
				}


			};
			pageList = userRespository.findAll(spec, pageable);

		} else {
			Specification<User> spec = new Specification<User>() {
				public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
					// 查询出未删除的
					list.add(cb.equal(root.<Integer>get("flag"), 1));
					return cb.and(list.toArray(new Predicate[0]));
				}
			};
			pageList = userRespository.findAll(spec, pageable);

		}
		map.put("total", pageList.getTotalElements());
		List<User> list = pageList.getContent();
		 
		map.put("depts", list);
		return map;
	}
  
  
}