package com.zx.blog.dao;

import com.zx.blog.entity.User;

import com.zx.blog.dto.SevenDaysDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

	User getUserByName(String userName);

	List<SevenDaysDto> findSevenDaysUser();

	Integer userCount();

	User getUserById(Long id);
}
