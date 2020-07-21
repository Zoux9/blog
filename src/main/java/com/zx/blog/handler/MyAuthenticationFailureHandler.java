package com.zx.blog.handler;

import com.alibaba.fastjson.JSON;
import com.zx.blog.vo.ValidateCodeException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
		httpServletResponse.setContentType("application/json;charset=utf-8");
		PrintWriter out = httpServletResponse.getWriter();
		Map<String, Object> error = new HashMap<>(16);
		error.put("error", e.getMessage());
		// 验证码自定义异常的处理
		if (e instanceof ValidateCodeException) {
			error.put("error", e.getMessage());
			//Security内置的异常处理
		} else if (e instanceof LockedException) {
			error.put("error", "账户被锁定请联系管理员!");
		} else if (e instanceof CredentialsExpiredException) {
			error.put("error", "密码过期请联系管理员!");
		} else if (e instanceof AccountExpiredException) {
			error.put("error", "账户过期请联系管理员!");
		} else if (e instanceof DisabledException) {
			error.put("error", "账户被禁用请联系管理员!");
		} else if (e instanceof BadCredentialsException) {
			error.put("error", "用户名密码输入错误,请重新输入!");
		}
		out.write(JSON.toJSONString(error));
		out.flush();
		out.close();

	}

	@Bean
	public MyAuthenticationFailureHandler getMyAuthenticationFailureHandler() {
		return new MyAuthenticationFailureHandler();
	}
}