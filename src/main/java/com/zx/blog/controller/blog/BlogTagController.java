package com.zx.blog.controller.blog;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.blog.entity.Blog;
import com.zx.blog.service.BlogService;
import com.zx.blog.service.TagService;
import com.zx.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zouxu
 * @date 2020/3/26 14:17
 */
@Controller
public class BlogTagController {

	@Autowired
	private BlogService blogService;

	/**
	 * 标签
	 *
	 * @param tagId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tag/{tagId}", method = RequestMethod.GET)
	public String getTypeBlog(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "4", value = "pageSize") Integer pageSize,
	                          @PathVariable Long tagId, Model model) {
		PageHelper.startPage(pageNum, pageSize);
		List<Blog> blogList = blogService.getBlogByTagId(tagId);
		PageInfo<Blog> blogPageInfo = new PageInfo<>(blogList);
		model.addAttribute("blogList", blogPageInfo);
		model.addAttribute("tagId", tagId);

		return "blog/tag";
	}
}
