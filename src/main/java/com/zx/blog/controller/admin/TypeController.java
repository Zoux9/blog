package com.zx.blog.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.blog.entity.Type;
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
 * @date 2020/3/20 18:45
 * 分类
 */
@Controller
@RequestMapping("/admin")
public class TypeController {

	private final TypeService typeService;

	public TypeController(TypeService typeService) {
		this.typeService = typeService;
	}


	/**
	 * 分页查询分类
	 *
	 * @param pageNum
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/types", method = RequestMethod.GET)
	public String typesPage(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize, Model model) {
		//设置起始页数与显示数量
		PageHelper.startPage(pageNum, pageSize);
		List<Type> typeList = typeService.getListType();

		PageInfo<Type> typePageInfo = new PageInfo<>(typeList);
		model.addAttribute("typePage", typePageInfo);

		return "admin/type";
	}

	@ResponseBody
	@RequestMapping(value = "/typeList", method = RequestMethod.GET)
	public List<JSONObject> getTypeList() {
		List<JSONObject> jsonObjectList = new ArrayList<>();
		List<Type> typeList = typeService.getListType();

		if (typeList == null) {
			return null;
		}
		for (Type type : typeList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name",type.getName());
			jsonObject.put("value",type.getId());
			jsonObjectList.add(jsonObject);
		}
		return jsonObjectList;
	}


	@RequestMapping(value = "/type/add", method = RequestMethod.GET)
	public String addType(Model model) {
		model.addAttribute("type", new Type());
		return "admin/type_add";
	}

	/**
	 * 添加分类
	 *
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/type/add", method = RequestMethod.POST)
	public String save(@Valid Type type, BindingResult result, RedirectAttributes attributes) {

		//查询数据库中分类是否重复
		Type name = typeService.getTypeByName(type.getName());
		if (name != null) {
			result.rejectValue("name", "nameError", "该分类名称重复");
		}

		if (result.hasErrors()) {
			return "admin/type_add";
		}

		int countType = typeService.saveType(type);

		if (countType > 0) {
			setMessageAndSuccess(attributes, true);
		} else {
			setMessageAndSuccess(attributes, false);
		}

		return "redirect:/admin/types";
	}


	/**
	 * 更新分类信息
	 *
	 * @param type
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value = "/type/update", method = RequestMethod.POST)
	public String updateType(@Valid Type type, BindingResult result, RedirectAttributes attributes) {

		//查询数据库中分类是否重复
		Type typeByName = typeService.getTypeByName(type.getName());
		if (typeByName != null) {
			result.rejectValue("name", "nameError", "该分类名称重复");

		}

		if (result.hasErrors()) {
			return "admin/type_update";
		}

		int countType = typeService.updateType(type);
		if (countType > 0) {
			setMessageAndSuccess(attributes, true);
		} else {
			setMessageAndSuccess(attributes, false);
		}

		return "redirect:/admin/types";
	}

	/**
	 * 分类数据回显
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/type/update/{id}", method = RequestMethod.GET)
	public String getTypeById(@PathVariable Long id, Model model) {
		Type type = typeService.getTypeById(id);
		if (type != null) {
			model.addAttribute("message", "回显数据成功,请继续修改!");
			model.addAttribute("success", true);
			model.addAttribute("type", type);
		} else {
			model.addAttribute("message", "回显数据失败,请尝试修改!");
			model.addAttribute("success", false);
		}

		return "admin/type_update";
	}


	/**
	 * 删除分类
	 *
	 * @param id
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value = "/type/delete/{id}", method = RequestMethod.GET)
	public String deleteType(@PathVariable("id") Long id, RedirectAttributes attributes) {

		int countDel = typeService.deleteType(id);
		if (countDel > 0) {
			setMessageAndSuccess(attributes, true);
		} else {
			setMessageAndSuccess(attributes, false);
		}
		return "redirect:/admin/types";
	}

	/**
	 * 抽取
	 *
	 * @param attributes
	 * @param flog
	 */
	private void setMessageAndSuccess(RedirectAttributes attributes, boolean flog) {
		if (flog) {
			attributes.addFlashAttribute("message", "分类操作成功");
			attributes.addFlashAttribute("success", true);
		} else {
			attributes.addFlashAttribute("message", "分类操作成功");
			attributes.addFlashAttribute("success", false);
		}
	}
}
