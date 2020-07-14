package com.zx.blog.controller.admin;

import com.zx.blog.annotation.SystemLog;
import com.zx.blog.entity.Link;
import com.zx.blog.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



/**
 * @author zouxu
 * @date 2020/3/26 14:17
 */
@Controller
@RequestMapping("/admin")
public class AdminLinkController {

	@Autowired
	private LinkService linkService;


	/**
	 * 获取所有友链信息
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getListLink", method = RequestMethod.GET)
	public String getListBlog(Model model) {
		model.addAttribute("adminLinks", linkService.getListLink());

		return "admin/link";
	}


	@RequestMapping(value = "/addLink", method = RequestMethod.GET)
	public String addLink() {
		return "admin/link_add";
	}


	/**
	 * 友链信息更新
	 * @param link
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value = "/link/update", method = RequestMethod.POST)
	public String updateLink(Link link, RedirectAttributes attributes) {

		int countType = linkService.updateLink(link);

		if (countType > 0) {
			setMessageAndSuccess(attributes, true);
		} else {
			setMessageAndSuccess(attributes, false);
		}

		return "redirect:/admin/getListLink";
	}


	/**
	 * 友链数据回显
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/link/update/{id}", method = RequestMethod.GET)
	public String getTagById(@PathVariable Long id, Model model) {
		Link link = linkService.getLinkById(id);

		if (link != null) {
			model.addAttribute("message", "回显友链数据成功,请继续修改!");
			model.addAttribute("success", true);
			model.addAttribute("link", link);
		} else {
			model.addAttribute("message", "回显友链数据失败,请尝试修改!");
			model.addAttribute("success", false);
		}

		return "admin/link_update";
	}

	/**
	 * 申请
	 *
	 * @param link
	 * @return
	 */
	@SystemLog(description = "后台友链添加")
	@RequestMapping(value = "/applyLink", method = RequestMethod.POST)
	public String applyLink(Link link, RedirectAttributes attributes) {

		Integer count = linkService.applyLink(link);

		if (count > 0) {
			setMessageAndSuccess(attributes, true);
		} else {
			setMessageAndSuccess(attributes, false);
		}

		return "redirect:/admin/getListLink";
	}

	/**
	 * 抽取
	 *
	 * @param attributes
	 * @param flog
	 */
	private void setMessageAndSuccess(RedirectAttributes attributes, boolean flog) {
		if (flog) {
			attributes.addFlashAttribute("message", "友链操作成功");
			attributes.addFlashAttribute("success", true);
		} else {
			attributes.addFlashAttribute("message", "友链操作失败");
			attributes.addFlashAttribute("success", false);
		}
	}

}
