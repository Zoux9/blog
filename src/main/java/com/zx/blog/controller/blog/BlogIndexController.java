package com.zx.blog.controller.blog;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.blog.annotation.SystemLog;
import com.zx.blog.entity.Blog;
import com.zx.blog.service.BlogService;
import com.zx.blog.service.TagService;
import com.zx.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author zouxu
 * @date 2020/3/26 14:17
 */
@Controller
@RequestMapping
public class BlogIndexController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private TypeService typeService;

	@Autowired
	private TagService tagService;

	/**
	 * 博客首页
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public String index(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "8", value = "pageSize") Integer pageSize,
	                    Model model, HttpSession session) {

		PageHelper.startPage(pageNum, pageSize);
		List<Blog> listBlog = blogService.getListBlog();
		PageInfo<Blog> listBlogPage = new PageInfo<>(listBlog);
		model.addAttribute("blogs", listBlogPage);
		model.addAttribute("blogRecommend", blogService.getRecommend());
		model.addAttribute("blogCarousel", blogService.getCarousel());
		session.setAttribute("blogNotice", blogService.getBlogNotice());
		session.setAttribute("viewsRanking", blogService.getViewsRanking());
		session.setAttribute("types", typeService.getListType());
		session.setAttribute("tags", tagService.getListTag());

		return "blog/index";
	}

	/**
	 * 全部文章
	 *
	 * @param pageNum
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getListBlog(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "4", value = "pageSize") Integer pageSize,
	                          Model model) {
		PageHelper.startPage(pageNum, pageSize);
		List<Blog> listBlog = blogService.getListBlog();
		PageInfo<Blog> listBlogPage = new PageInfo<>(listBlog);
		model.addAttribute("listBlogPage", listBlogPage);

		return "blog/list";
	}

	@RequestMapping(value = "/searchType/{typeId}", method = RequestMethod.GET)
	public String searchType(@PathVariable Long typeId, Model model) {

		List<Blog> typeSearch = blogService.getBlogByTypeId(typeId);
		model.addAttribute("typeBlog", typeSearch);

		return "blog/index :: typeSearch";
	}


	/**
	 * 文章详情
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@SystemLog(description = "查看文章")
	@RequestMapping(value = "/blog/{id}", method = RequestMethod.GET)
	public String blog(@PathVariable Long id, Model model) {
		Blog blogConvert = blogService.getBlogConvert(id);
		model.addAttribute("blog", blogConvert);

		return "blog/info";
	}

	/**
	 * 搜索
	 *
	 * @param keyboard
	 * @param model
	 * @return
	 */
	@SystemLog(description = "文章所搜")
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchTitleAndContent(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1", value = "pageSize") Integer pageSize,
	                                    String keyboard, Model model) {
		PageHelper.startPage(pageNum, pageSize);
		List<Blog> searchBlog = blogService.searchTitleAndContent(keyboard);
		PageInfo<Blog> blogPageInfo = new PageInfo<>(searchBlog);
		model.addAttribute("blogPageInfo", blogPageInfo);
		//查询参数
		model.addAttribute("keyboard", keyboard);


		return "blog/search";
	}

}
