package com.zx.blog.service.impl;

import com.zx.blog.dao.SysLogMapper;
import com.zx.blog.entity.SysLog;
import com.zx.blog.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SysLogServiceImpl implements SysLogService {

	private final SysLogMapper sysLogMapper;

	@Autowired
	public SysLogServiceImpl(SysLogMapper sysLogMapper) {
		this.sysLogMapper = sysLogMapper;
	}

	@Override
	public void save(SysLog sysLog) {
		sysLogMapper.save(sysLog);
	}

	@Override
	public List<SysLog> findAll() {
		return sysLogMapper.findAll();
	}
}
