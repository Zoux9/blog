package com.zx.blog.controller.admin;

import com.zx.blog.entity.User;
import com.zx.blog.service.UserService;
import com.zx.blog.exception.BlogException;
import com.zx.blog.util.ObjectUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;


@Controller
@RequestMapping("/admin")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

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
		String name = auth.getName();
		Map<String, Object> userMap = ObjectUtil.getStringToMap(name);
		User user = userService.getUserByName(String.valueOf(userMap.get("username")));
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
