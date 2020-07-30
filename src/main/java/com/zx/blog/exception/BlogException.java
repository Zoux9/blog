package com.zx.blog.exception;

import com.zx.blog.service.BaseErrorService;

public class BlogException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 错误码
	 */
	protected String errorCode;
	/**
	 * 错误信息
	 */
	protected String errorMsg;

	public BlogException() {
		super();
	}

	public BlogException(BaseErrorService errorInfoInterface) {
		super(errorInfoInterface.getResultCode());
		this.errorCode = errorInfoInterface.getResultCode();
		this.errorMsg = errorInfoInterface.getResultMsg();
	}

	public BlogException(BaseErrorService errorInfoInterface, Throwable cause) {
		super(errorInfoInterface.getResultCode(), cause);
		this.errorCode = errorInfoInterface.getResultCode();
		this.errorMsg = errorInfoInterface.getResultMsg();
	}

	public BlogException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public BlogException(String errorCode, String errorMsg) {
		super(errorCode);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public BlogException(String errorCode, String errorMsg, Throwable cause) {
		super(errorCode, cause);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}


	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String getMessage() {
		return errorMsg;
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

}