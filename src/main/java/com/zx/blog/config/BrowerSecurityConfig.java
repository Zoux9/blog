package com.zx.blog.config;

import com.zx.blog.filter.JwtAuthenticationFilter;
import com.zx.blog.filter.JwtVerifyFilter;
import com.zx.blog.handler.MyAuthenticationFailureHandler;
import com.zx.blog.service.UserService;
import com.zx.blog.util.RedisComponentUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BrowerSecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserService userService;

	private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;

	private final RsaKeyProperties rsaKeyProperties;

	private final RedisComponentUtils redisComponentUtils;

	public BrowerSecurityConfig(UserService userService, MyAuthenticationFailureHandler myAuthenticationFailureHandler, RsaKeyProperties rsaKeyProperties, RedisComponentUtils redisComponentUtils) {
		this.userService = userService;
		this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
		this.rsaKeyProperties = rsaKeyProperties;
		this.redisComponentUtils = redisComponentUtils;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
				// 所有用户均可访问的资源
				.antMatchers("/**/css/**", "/**/fonts/**", "/**/js/**", "/**/images/**", "/**/lib/**").permitAll()
				// 指定路径下的资源需要验证了的用户才能访问
				.antMatchers("/admin/**").authenticated()
				.and()
				.addFilter(new JwtAuthenticationFilter(redisComponentUtils, super.authenticationManagerBean(), rsaKeyProperties, myAuthenticationFailureHandler))
				.addFilter(new JwtVerifyFilter(super.authenticationManagerBean(),rsaKeyProperties))
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.formLogin()
				// 指定登录页面,授予所有用户访问登录页面
				.loginPage("/admin/login")
				.and()
				.logout()
				.logoutSuccessUrl("/admin/login").permitAll()
				.and()
				// 防止web页面的Frame被拦截
				.headers().frameOptions().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
	}

}
