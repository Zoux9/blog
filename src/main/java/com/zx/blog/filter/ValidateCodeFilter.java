package com.zx.blog.filter;

import com.zx.blog.handler.MyAuthenticationFailureHandler;
import com.zx.blog.util.RedisComponentUtils;
import com.zx.blog.exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class ValidateCodeFilter extends OncePerRequestFilter {


	@Autowired
	private RedisComponentUtils redisService;

	@Autowired
	private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
	                                FilterChain filterChain) throws ServletException, IOException {
		String defaultFilterProcessUrl = "/admin/login";
		if (StringUtils.equalsIgnoreCase(defaultFilterProcessUrl, httpServletRequest.getRequestURI())
				&& StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "POST")) {
			try {
				validateCode(new ServletWebRequest(httpServletRequest));
			} catch (ValidateCodeException e) {
				myAuthenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
			}
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private void validateCode(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
		String cKey = servletWebRequest.getParameter("cKey");
		String captcha = servletWebRequest.getParameter("captcha");
		//判断验证码是否还存在
		if (redisService.exists(cKey)) {
			if (redisService.get(cKey).equals(captcha)) {
				//验证通过之后删除对应的key
				redisService.remove(cKey);
			} else {
				throw new ValidateCodeException("验证码错误！");
			}
		} else {
			throw new ValidateCodeException("验证码不存在!");
		}

	}

}