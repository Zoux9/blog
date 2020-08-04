package com.zx.blog.controller.admin;

import com.zx.blog.service.BlogService;
import com.zx.blog.service.TagService;
import com.zx.blog.service.TypeService;
import com.zx.blog.service.UserService;
import com.zx.blog.dto.SevenDaysDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author zouxu
 * @date 2020/4/1 22:03
 */
@Controller
@RequestMapping("/admin")
public class AdminIndexController {

	private final BlogService blogService;

	private final UserService userService;

	private final TypeService typeService;

	private final TagService tagService;

	public AdminIndexController(BlogService blogService, UserService userService, TypeService typeService, TagService tagService) {
		this.blogService = blogService;
		this.userService = userService;
		this.typeService = typeService;
		this.tagService = tagService;
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String adminIndex(Model model){
		model.addAttribute("blogCount",blogService.countBlog());
		model.addAttribute("userCount",userService.countUser());
		model.addAttribute("typeCount",typeService.countType());
		model.addAttribute("tagCount",tagService.countTag());
		return "admin/main";
	}

	@ResponseBody
	@RequestMapping(value = "/findSevenDaysBlog", method = RequestMethod.GET)
	public List<SevenDaysDto> findSevenDaysBlog(){
		return blogService.findSevenDaysBlog();
	}

	@ResponseBody
	@RequestMapping(value = "/findSevenDaysUser", method = RequestMethod.GET)
	public List<SevenDaysDto> findSevenDaysUser(){
		return userService.findSevenDaysUser();
	}

}
