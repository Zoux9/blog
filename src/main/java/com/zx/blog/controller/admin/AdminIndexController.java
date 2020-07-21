package com.zx.blog.controller.admin;

import com.zx.blog.service.BlogService;
import com.zx.blog.service.TagService;
import com.zx.blog.service.TypeService;
import com.zx.blog.service.UserService;
import com.zx.blog.vo.SevenDays;
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

	@Autowired
	private BlogService blogService;

	@Autowired
	private UserService userService;

	@Autowired
	private TypeService typeService;

	@Autowired
	private TagService tagService;

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String adminIndex(Model model){
		model.addAttribute("blogCount",blogService.blogCount());
		model.addAttribute("userCount",userService.userCount());
		model.addAttribute("typeCount",typeService.typeCount());
		model.addAttribute("tagCount",tagService.tagCount());
		return "admin/main";
	}

	@ResponseBody
	@RequestMapping(value = "/findSevenDaysBlog", method = RequestMethod.GET)
	public List<SevenDays> findSevenDaysBlog(){
		return blogService.findSevenDaysBlog();
	}

	@ResponseBody
	@RequestMapping(value = "/findSevenDaysUser", method = RequestMethod.GET)
	public List<SevenDays> findSevenDaysUser(){
		return userService.findSevenDaysUser();
	}

}
