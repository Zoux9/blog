package com.zx.blog.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.blog.annotation.SystemLog;
import com.zx.blog.entity.Blog;
import com.zx.blog.entity.Type;
import com.zx.blog.service.BlogService;
import com.zx.blog.service.TagService;
import com.zx.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zouxu
 * @date 2020/3/22 20:41
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

	private final BlogService blogService;

	private final TypeService typeService;

	private final TagService tagService;

	public BlogController(BlogService blogService, TypeService typeService, TagService tagService) {
		this.blogService = blogService;
		this.typeService = typeService;
		this.tagService = tagService;
	}


	/**
	 * 后台文章列表
	 *
	 * @param pageNum
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/blogs", method = RequestMethod.GET)
	public String blogsPage(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize, Model model) {

		//设置起始页数与显示数量
		PageHelper.startPage(pageNum, pageSize);
		List<Blog> blogList = blogService.adminBlogInfo();
		List<Type> typeList = typeService.getListType();
		PageInfo<Blog> blogPageInfo = new PageInfo<>(blogList);

		setBlogAndType(model, typeList, blogPageInfo);
		return "admin/blog";
	}

	@SystemLog(description = "搜索后台文章")
	@RequestMapping(value = "/blogs/search", method = RequestMethod.POST)
	public String blogsSearch(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize,
	                          Blog blog, Model model) {
		//设置起始页数与显示数量
		PageHelper.startPage(pageNum, pageSize);
		List<Type> typeList = typeService.getListType();
		List<Blog> searchBlog = blogService.searchBlog(blog);
		PageInfo<Blog> blogPageInfo = new PageInfo<>(searchBlog);

		setBlogAndType(model, typeList, blogPageInfo);
		return "admin/blog";
	}

	@RequestMapping(value = "/blog/add", method = RequestMethod.GET)
	public String addBlog(Model model) {
		model.addAttribute("blog", new Blog());

		setTypeAndTag(model);
		return "admin/blog_add";
	}


	@RequestMapping(value = "/blog/add", method = RequestMethod.POST)
	public String save(@Valid Blog blog, RedirectAttributes attributes) {
		//设置分类
		blog.setType(typeService.getTypeById(blog.getTypeId()));
		//设置标签
		blog.setTags(tagService.listTagByIds(blog.getTagIds()));

		//保存文章
		int count = blogService.save(blog);
		if (count > 0) {
			setMessageAndSuccess(attributes, true);
		} else {
			setMessageAndSuccess(attributes, false);
		}

		return "redirect:/admin/blogs";
	}


	@RequestMapping(value = "/blog/delete/{id}", method = RequestMethod.GET)
	public String deleteTag(@PathVariable("id") Long id, RedirectAttributes attributes) {

		int countDel = blogService.deleteBlog(id);
		if (countDel > 0) {
			setMessageAndSuccess(attributes, true);
		} else {
			setMessageAndSuccess(attributes, false);
		}
		return "redirect:/admin/blogs";
	}


	/**
	 * 文章数据回显
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/blog/update/{id}", method = RequestMethod.GET)
	public String getBlogById(@PathVariable Long id, Model model) {
		//中间表
		setTypeAndTag(model);
		//获取blog
		Map<String, Object> blog = blogService.getBlogById(id);
		if (blog != null) {
			model.addAttribute("message", "回显文章数据成功,请继续修改!");
			model.addAttribute("success", true);

			model.addAttribute("ids", blog.get("ids"));
			model.addAttribute("blog", blog.get("blog"));
		} else {
			model.addAttribute("message", "回显文章数据失败,请尝试修改!");
			model.addAttribute("success", false);
		}

		return "admin/blog_update";
	}

	@ResponseBody
	@RequestMapping(value = "/blog/query/{id}", method = RequestMethod.GET)
	public JSONObject getBlogById(@PathVariable Long id) {
		Map<String, Object> blogMap = blogService.getBlogById(id);
		JSONObject jsonObject = new JSONObject();
		if (blogMap == null){
			return null;
		}
		Blog blog = (Blog) blogMap.get("blog");
		jsonObject.put("flag",blog.getFlag());
		jsonObject.put("type",blog.getType().getId());
		jsonObject.put("tag",blogMap.get("ids"));
		return jsonObject;
	}

	private void setTypeAndTag(Model model) {
		model.addAttribute("types", typeService.getListType());
		model.addAttribute("tags", tagService.getListTag());
	}


	/**
	 * 抽取
	 *
	 * @param attributes
	 * @param flog
	 */
	private void setMessageAndSuccess(RedirectAttributes attributes, boolean flog) {
		if (flog) {
			attributes.addFlashAttribute("message", "文章操作成功");
			attributes.addFlashAttribute("success", true);
		} else {
			attributes.addFlashAttribute("message", "文章操作成功");
			attributes.addFlashAttribute("success", false);
		}
	}


	private void setBlogAndType(Model model, List<Type> typeList, PageInfo<Blog> blogPageInfo) {
		model.addAttribute("blogPage", blogPageInfo);
		model.addAttribute("types", typeList);
	}
}
