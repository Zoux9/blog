package com.zx.blog.controller.blog;

import com.zx.blog.annotation.SystemLog;
import com.zx.blog.entity.Link;
import com.zx.blog.service.LinkService;
import com.zx.blog.exception.BlogException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author zouxu
 * @date 2020/3/26 14:17
 */
@Controller
public class BlogLinkController {

	private final LinkService linkService;

	@Autowired
	public BlogLinkController(LinkService linkService) {
		this.linkService = linkService;
	}


	/**
	 * 首页友链
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getShowLink", method = RequestMethod.GET)
	public String getShowLink(Model model) {
		model.addAttribute("links", linkService.getShowLink());

		return "blog/index";
	}


	/**
	 * 申请友链页面
	 */
	@RequestMapping(value = "/link", method = RequestMethod.GET)
	public String link() {

		return "blog/link";
	}

	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String success() {

		return "blog/success";
	}

	/**
	 * 申请
	 *
	 * @param link
	 * @return
	 */
	@SystemLog(description = "提交友链信息")
	@RequestMapping(value = "/applyLink", method = RequestMethod.POST)
	public String applyLink(Link link) {
		Integer count = linkService.applyLink(link);
		if (count <= 0) {
			throw new BlogException("500", "未申请成功,请稍后再试");
		}

		return "redirect:/success";
	}


}
