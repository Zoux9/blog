package com.zx.blog.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.blog.entity.Tag;
import com.zx.blog.service.TagService;
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
 * @date 2020/3/20 18:45
 * 分类
 */
@Controller
@RequestMapping("/admin")
public class TagController {

	private final TagService tagService;

	public TagController(TagService tagService) {
		this.tagService = tagService;
	}


	/**
	 * 分页查询标签
	 *
	 * @param pageNum
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	public String tagsPage(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize, Model model) {
		//设置起始页数与显示数量
		PageHelper.startPage(pageNum, pageSize);
		List<Tag> tagList = tagService.getListTag();

		PageInfo<Tag> tagPageInfo = new PageInfo<>(tagList);
		model.addAttribute("tagPage", tagPageInfo);

		return "admin/tag";
	}

	@ResponseBody
	@RequestMapping(value = "/tagList", method = RequestMethod.GET)
	public List<JSONObject> tagsPage() {
		List<JSONObject> tagsJsonList = new ArrayList<>();
		List<Tag> tagList = tagService.getListTag();

		for (Tag tag : tagList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name",tag.getName());
			jsonObject.put("value",tag.getId());
			tagsJsonList.add(jsonObject);
		}

		return tagsJsonList;
	}

	@RequestMapping(value = "/tag/add", method = RequestMethod.GET)
	public String addTag(Model model) {
		model.addAttribute("tag", new Tag());
		return "admin/tag_add";
	}

	/**
	 * 添加标签
	 *
	 * @param tag
	 * @return
	 */
	@RequestMapping(value = "/tag/add", method = RequestMethod.POST)
	public String save(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {

		//查询数据库中标签是否重复
		Tag name = tagService.getTagByName(tag.getName());
		if (name != null) {
			result.rejectValue("name", "nameError", "该标签名称重复");
		}

		if (result.hasErrors()) {
			return "admin/tag_add";
		}

		int countTag = tagService.saveTag(tag);

		if (countTag > 0) {
			setMessageAndSuccess(attributes, true);
		} else {
			setMessageAndSuccess(attributes, false);
		}

		return "redirect:/admin/tags";
	}


	/**
	 * 更新标签信息
	 *
	 * @param tag
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value = "/tag/update", method = RequestMethod.POST)
	public String updateTag(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {

		//查询数据库中分类是否重复
		Tag tagByName = tagService.getTagByName(tag.getName());
		if (tagByName.getName().equals(tag.getName()) && tagByName.getState().equals(tag.getState())) {
			result.rejectValue("name", "nameError", "该标签名称重复");
		}

		if (result.hasErrors()) {
			return "admin/tag_update";
		}

		int countType = tagService.updateTag(tag);
		if (countType > 0) {
			setMessageAndSuccess(attributes, true);
		} else {
			setMessageAndSuccess(attributes, false);
		}

		return "redirect:/admin/tags";
	}


	/**
	 * 标签数据回显
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tag/update/{id}", method = RequestMethod.GET)
	public String getTagById(@PathVariable Long id, Model model) {
		Tag tag = tagService.getTagById(id);

		if (tag != null) {
			model.addAttribute("message", "回显标签数据成功,请继续修改!");
			model.addAttribute("success", true);
			model.addAttribute("tag", tag);

		} else {
			model.addAttribute("message", "回显标签数据失败,请尝试修改!");
			model.addAttribute("success", false);
		}

		return "admin/tag_update";
	}


	/**
	 * 删除标签
	 *
	 * @param id
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value = "/tag/delete/{id}", method = RequestMethod.GET)
	public String deleteTag(@PathVariable("id") Long id, RedirectAttributes attributes) {

		int countDel = tagService.deleteTag(id);
		if (countDel > 0) {
			setMessageAndSuccess(attributes, true);
		} else {
			setMessageAndSuccess(attributes, false);
		}
		return "redirect:/admin/tags";
	}

	/**
	 * 抽取
	 *
	 * @param attributes
	 * @param flog
	 */
	private void setMessageAndSuccess(RedirectAttributes attributes, boolean flog) {
		if (flog) {
			attributes.addFlashAttribute("message", "标签操作成功");
			attributes.addFlashAttribute("success", true);
		} else {
			attributes.addFlashAttribute("message", "标签操作成功");
			attributes.addFlashAttribute("success", false);
		}
	}

}
