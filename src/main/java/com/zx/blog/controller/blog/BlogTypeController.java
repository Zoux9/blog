package com.zx.blog.controller.blog;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.blog.entity.Blog;
import com.zx.blog.entity.Type;
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
public class BlogTypeController {

	@Autowired
	private BlogService blogService;

	/**
	 * 分类
	 *
	 * @param typeId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
	public String getTypeBlog(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "4", value = "pageSize") Integer pageSize,
	                          @PathVariable Long typeId, Model model) {
		PageHelper.startPage(pageNum, pageSize);
		List<Blog> blogList = blogService.getBlogByTypeId(typeId);
		PageInfo<Blog> blogPageInfo = new PageInfo<>(blogList);
		model.addAttribute("blogList", blogPageInfo);
		model.addAttribute("typeId", typeId);

		return "blog/type";
	}
}
