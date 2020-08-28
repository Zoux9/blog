package com.zx.blog.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.zx.blog.config.RsaKeyProperties;
import com.zx.blog.dto.LoginRequest;
import com.zx.blog.exception.ValidateCodeException;
import com.zx.blog.handler.MyAuthenticationFailureHandler;
import com.zx.blog.util.CookieUtils;
import com.zx.blog.util.JwtUtils;
import com.zx.blog.util.RedisComponentUtils;
import groovy.util.logging.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zouxu
 * @date 2020/8/8 1:10
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final RedisComponentUtils redisService;
	private final AuthenticationManager authenticationManager;
	private final RsaKeyProperties rsaKeyProperties;
	private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;

	public JwtAuthenticationFilter(RedisComponentUtils redisService, AuthenticationManager authenticationManager, RsaKeyProperties rsaKeyProperties, MyAuthenticationFailureHandler myAuthenticationFailureHandler) {
		this.redisService = redisService;
		this.authenticationManager = authenticationManager;
		this.rsaKeyProperties = rsaKeyProperties;
		this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
		super.setFilterProcessesUrl("/auth/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
	                                            HttpServletResponse response) throws AuthenticationException {

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			log.info("======================登陆信息拦截======================");
			// 获取登录的信息
			InputStream is = request.getInputStream();
			String reqJson = IOUtils.toString(is, StandardCharsets.UTF_8);
			log.info(reqJson);
			if (!"".equals(reqJson)) {
				log.info("开始获取登陆表单信息");
				LoginRequest user = objectMapper.readValue(reqJson, LoginRequest.class);
				if (user == null){
					log.error("登陆表单信息填写错误，登陆失败");
					return null;
				}
				String cKey = user.getcKey();
				//判断验证码是否还存在
				try {
					log.info("开始检验验证码信息");
					if (redisService.exists(cKey)) {
						if (redisService.get(cKey).equals(user.getCaptcha())) {
							//验证通过之后删除对应的key
							redisService.remove(cKey);
						} else {
							log.error("验证码错误！");
							throw new ValidateCodeException("验证码错误！");
						}
					} else {
						log.error("该验证码不存在!");
						throw new ValidateCodeException("该验证码不存在!");
					}
				} catch (ValidateCodeException e) {
					try {
						myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
						return null;
					} catch (ServletException servletException) {
						servletException.printStackTrace();
					}
				}
				// 这部分和attemptAuthentication方法中的源码是一样的，
				// 只不过由于这个方法源码的是把用户名和密码这些参数的名字是死的，所以我们重写了一下
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						user.getUsername(), user.getPassword());
				return authenticationManager.authenticate(authentication);
			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		try {
			Map<String, Object> map = new HashMap<>(2);
			String name = authResult.getName();
			map.put("username", name);

			String token = JwtUtils.generateToken(map, this.rsaKeyProperties.getPrivateKey(), this.rsaKeyProperties.getExpire());
			if (StringUtils.isNotBlank(token)) {
				CookieUtils.setCookie(request, response, this.rsaKeyProperties.getCooikeName(), token, this.rsaKeyProperties.getExpire() * 60);
				PrintWriter out = response.getWriter();
				Map<String, Object> resultMap = new HashMap<>(3);
				resultMap.put("code", HttpServletResponse.SC_OK);
				out.write(new ObjectMapper().writeValueAsString(resultMap));
				out.flush();
				out.close();
			}
		} catch (Exception outEx) {
			outEx.printStackTrace();
		}
	}

}
