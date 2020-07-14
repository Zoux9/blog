package com.zx.blog.service;

public interface BaseErrorService {

	/**
	 * 错误码
	 */
	String getResultCode();

	/**
	 * 错误描述
	 */
	String getResultMsg();
}