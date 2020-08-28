package com.zx.blog.filter;

import com.zx.blog.config.RsaKeyProperties;
import com.zx.blog.util.CookieUtils;
import com.zx.blog.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JwtVerifyFilter extends BasicAuthenticationFilter {
	private final RsaKeyProperties rsaKeyProperties;

	public JwtVerifyFilter(AuthenticationManager authenticationManager, RsaKeyProperties rsaKeyProperties) {
		super(authenticationManager);
		this.rsaKeyProperties = rsaKeyProperties;
	}

	/**
	 * 过滤请求
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
	                                FilterChain chain) throws IOException, ServletException {

		//请求体头中的cookie是否包含cookieName
		String token = CookieUtils.getCookieValue(request, rsaKeyProperties.getCooikeName());

		//cookie中是否包含token，不包含直接返回
		if (StringUtils.isBlank(token)) {
			SecurityContextHolder.clearContext();
		} else {
			//获取权限失败，会抛出异常
			UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
			//获取后，将Authentication写入SecurityContextHolder中供后序使用
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		chain.doFilter(request, response);
	}

	/**
	 * 通过token，获取用户信息
	 *
	 * @param request
	 * @return
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = CookieUtils.getCookieValue(request, rsaKeyProperties.getCooikeName());
		if (token != null) {
			//通过token解析出载荷信息
			try {
				Map<String, Object> map = JwtUtils.getInfoFromToken(token,
						rsaKeyProperties.getPublicKey());
				//不为null，返回
				if (!map.isEmpty()) {
					return new UsernamePasswordAuthenticationToken(map, null, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}