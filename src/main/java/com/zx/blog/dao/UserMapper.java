package com.zx.blog.dao;

import com.zx.blog.entity.User;

import com.zx.blog.vo.SevenDays;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

	User getUserByName(String userName);

	List<SevenDays> findSevenDaysUser();

	Integer userCount();

	User getUserById(Long id);
}
