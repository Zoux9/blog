package com.zx.blog.service;

import com.zx.blog.entity.Link;

import java.util.List;

/**
 * @author zouxu
 * @date 2020/4/6 22:03
 */
public interface LinkService {

	/**
	 * 友链申请
	 */
	Integer applyLink(Link link);

	/**
	 * 友链状态更新
	 */
	Integer updateLink(Link link);

	/**
	 * 查询全部友链
	 */
	List<Link> getListLink();

	/**
	 * 前端友链显示
	 */
	List<Link> getShowLink();

	Link getLinkById(Long id);
}
