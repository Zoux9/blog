package com.zx.blog.controller.admin;

import com.zx.blog.entity.User;
import com.zx.blog.service.UserService;
import com.zx.blog.exception.BlogException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/admin")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 后台登录页面
	 *
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {

		return "admin/login";
	}


	/**
	 * 后台首页
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public String adminIndex(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.getUserByName(auth.getName());
		model.addAttribute("user", user);
		return "admin/index";
	}

	/**
	 * 个人信息页
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getUserInfo/{id}", method = RequestMethod.GET)
	public String getUserInfo(@PathVariable("id") Long id, Model model) {
		if (id != null) {
			User user = userService.getUserById(id);
			model.addAttribute("user", user);
			return "admin/user_info";
		}
		throw new BlogException("404", "该用户不存在");
	}
}
