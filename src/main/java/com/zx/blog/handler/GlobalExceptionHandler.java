package com.zx.blog.handler;

import com.zx.blog.service.impl.BaseErrorServiceImpl;
import com.zx.blog.exception.BlogException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


	/**
	 * 处理自定义的业务异常
	 *
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = BlogException.class)
	public String bizExceptionHandler(HttpServletRequest req, BlogException e, Model model) {
		logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
		model.addAttribute("code", e.getErrorCode());
		model.addAttribute("resultMsg", e.getErrorMsg());
		return "error/error";
	}

	/**
	 * 处理空指针的异常
	 *
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = NullPointerException.class)
	public String exceptionHandler(HttpServletRequest req, NullPointerException e, Model model) {
		logger.error("发生空指针异常！原因是:", e);
		model.addAttribute("code", BaseErrorServiceImpl.BODY_NOT_MATCH.getResultCode());
		model.addAttribute("resultMsg", BaseErrorServiceImpl.BODY_NOT_MATCH.getResultMsg());
		return "error/error";

	}

	/**
	 * 处理其他异常
	 *
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public String exceptionHandler(HttpServletRequest req, Exception e, Model model) {
		logger.error("未知异常！原因是:", e);
		model.addAttribute("code", BaseErrorServiceImpl.INTERNAL_SERVER_ERROR.getResultCode());
		model.addAttribute("resultMsg", BaseErrorServiceImpl.INTERNAL_SERVER_ERROR.getResultMsg());
		return "error/error";
	}
}
