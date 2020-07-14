package com.zx.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.blog.entity.SysLog;
import com.zx.blog.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class SysLogController {

	@Autowired
	private SysLogService sysLogService;

	/**
	 * 查询所有日志
	 *
	 * @return
	 */
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String findAll(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "12", value = "pageSize") Integer pageSize,
	                      Model model) {
		PageHelper.startPage(pageNum, pageSize);
		List<SysLog> sysLogList = sysLogService.findAll();
		PageInfo<SysLog> sysLogPageInfo = new PageInfo<>(sysLogList);
		model.addAttribute("sysLogsPage", sysLogPageInfo);
		return "admin/log";
	}
}
