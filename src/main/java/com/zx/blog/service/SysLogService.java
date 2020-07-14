package com.zx.blog.service;

import com.zx.blog.entity.SysLog;

import java.util.List;

public interface SysLogService {
	void save(SysLog sysLog);

	List<SysLog> findAll();
}
