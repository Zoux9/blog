package com.zx.blog.service;

import com.zx.blog.entity.User;
import com.zx.blog.vo.SevenDays;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	List<SevenDays> findSevenDaysUser();

	Integer userCount();

	User getUserByName(String name);

	User getUserById(Long id);
}
