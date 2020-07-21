package com.zx.blog.controller.blog;

import com.github.pagehelper.PageInfo;
import com.zx.blog.entity.Blog;
import com.zx.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zouxu
 * @date 2020/3/29 22:43
 */
@Controller
public class TimeAndAboutController {

	@Autowired
	private BlogService blogService;

	/**
	 * 时间轴
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/time", method = RequestMethod.GET)
	public String time(Model model) {
		model.addAttribute("timeBlog", blogService.getBlogTime());
		return "blog/time";
	}

	/**
	 * 关于我
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about(Model model) {

		model.addAttribute("aboutBlog", blogService.getAbout());

		return "blog/about";
	}
}
