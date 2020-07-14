package com.zx.blog.dao;

import com.zx.blog.entity.SysLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zouxu
 * @date 2020/3/31 19:47
 */
@Repository
public interface SysLogMapper {

	void save(SysLog sysLog);

	List<SysLog> findAll();
}

