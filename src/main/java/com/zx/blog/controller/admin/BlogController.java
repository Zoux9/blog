package com.zx.blog.controller.admin;

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

/**
 * @author zouxu
 * @date 2020/3/22 20:41
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private TypeService typeService;

	@Autowired
	private TagService tagService;


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
	public String save(@Valid Blog blog, BindingResult result, RedirectAttributes attributes) {
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
	public String getTagById(@PathVariable Long id, Model model) {
		//中间表
		setTypeAndTag(model);
		//获取blog
		Blog blog = blogService.getBlogById(id);
		if (blog != null) {
			model.addAttribute("message", "回显文章数据成功,请继续修改!");
			model.addAttribute("success", true);
			//前端回显标签转换
			List<Long> ids = new ArrayList<>();
			String tag = blog.getTagIds();
			if (tag != null) {
				String[] tagIds = tag.split(",");
				ids = new ArrayList<>();
				for (String s : tagIds) {
					ids.add(Long.valueOf(s));
				}
			}
			model.addAttribute("ids", ids);
			model.addAttribute("blog", blog);
		} else {
			model.addAttribute("message", "回显文章数据失败,请尝试修改!");
			model.addAttribute("success", false);
		}

		return "admin/blog_update";
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
