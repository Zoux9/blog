package com.zx.blog.service.impl;

import com.zx.blog.dao.UserMapper;
import com.zx.blog.entity.User;
import com.zx.blog.service.UserService;
import com.zx.blog.vo.SevenDays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	/**
	 * 登录
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User userInfo = userMapper.getUserByName(username);

		if (userInfo != null) {
			org.springframework.security.core.userdetails.User user =new org.springframework.security.core.userdetails.User(userInfo.getUsername(), userInfo.getPassword(), true, true, true, true, getAuthority());

			return user;
		}
		return null;
	}

	public List<SimpleGrantedAuthority> getAuthority(/*List<Role> roles*/) {

		List<SimpleGrantedAuthority> list = new ArrayList<>();
//		for (Role role : roles) {
			list.add(new SimpleGrantedAuthority("ROLE_Admin"  /*role.getRoleName()*/));
//		}
		return list;
	}


	/**
	 * 查询最近七天注册的用户
	 * @return
	 */
	@Override
	public List<SevenDays> findSevenDaysUser() {
		List<SevenDays> sevenDaysUser = userMapper.findSevenDaysUser();
		sevenDaysUser.sort(Comparator.comparing(SevenDays::getClickDate).reversed());
		return sevenDaysUser;
	}

	/**
	 * 后台首页用户数
	 * @return
	 */
	@Override
	public Integer userCount() {
		return userMapper.userCount();
	}

	/**
	 * 后台用户信息显示
	 * @param name
	 * @return
	 */
	@Override
	public User getUserByName(String name) {
		return userMapper.getUserByName(name);
	}

	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	@Override
	public User getUserById(Long id) {
		return userMapper.getUserById(id);
	}
}
