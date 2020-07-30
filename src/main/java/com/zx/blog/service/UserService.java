package com.zx.blog.service;

import com.zx.blog.entity.User;
import com.zx.blog.dto.SevenDaysDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	List<SevenDaysDto> findSevenDaysUser();

	Integer countUser();

	User getUserByName(String name);

	User getUserById(Long id);
}
