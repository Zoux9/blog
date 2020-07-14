package com.zx.blog.config;

import com.zx.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BrowerSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// 所有用户均可访问的资源
				.antMatchers("/**/css/**","/**/fonts/**", "/**/js/**", "/**/images/**", "/**/lib/**").permitAll()
				.antMatchers("/admin/**").authenticated()
				.and()
			 .formLogin()
				// 指定登录页面,授予所有用户访问登录页面
				.loginPage("/admin/login")
				.defaultSuccessUrl("/admin/index",true)
				.and()
				.csrf().disable()
			 .logout()
				.logoutSuccessUrl("/admin/login")
				.permitAll().
				and()
			 .headers()
				.frameOptions()
				.disable();
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
	}

}
