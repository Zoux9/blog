package com.zx.blog.vo;

import com.alibaba.fastjson.JSONObject;
import com.zx.blog.service.BaseErrorService;
import com.zx.blog.service.impl.BaseErrorServiceImpl;

public class ResultBody {
	/**
	 * 响应代码
	 */
	private String code;

	/**
	 * 响应消息
	 */
	private String message;

	/**
	 * 响应结果
	 */
	private Object result;

	public ResultBody() {
	}

	public ResultBody(BaseErrorService errorInfo) {
		this.code = errorInfo.getResultCode();
		this.message = errorInfo.getResultMsg();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * 成功
	 * 
	 * @return
	 */
	public static ResultBody success() {
		return success(null);
	}

	/**
	 * 成功
	 * @param data
	 * @return
	 */
	public static ResultBody success(Object data) {
		ResultBody rb = new ResultBody();
		rb.setCode(BaseErrorServiceImpl.SUCCESS.getResultCode());
		rb.setMessage(BaseErrorServiceImpl.SUCCESS.getResultMsg());
		rb.setResult(data);
		return rb;
	}

	/**
	 * 失败
	 */
	public static ResultBody error(BaseErrorService errorInfo) {
		ResultBody rb = new ResultBody();
		rb.setCode(errorInfo.getResultCode());
		rb.setMessage(errorInfo.getResultMsg());
		rb.setResult(null);
		return rb;
	}

	/**
	 * 失败
	 */
	public static ResultBody error(String code, String message) {
		ResultBody rb = new ResultBody();
		rb.setCode(code);
		rb.setMessage(message);
		rb.setResult(null);
		return rb;
	}

	/**
	 * 失败
	 */
	public static ResultBody error( String message) {
		ResultBody rb = new ResultBody();
		rb.setCode("-1");
		rb.setMessage(message);
		rb.setResult(null);
		return rb;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}