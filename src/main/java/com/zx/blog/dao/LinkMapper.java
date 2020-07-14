package com.zx.blog.dao;

import com.zx.blog.entity.Link;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zouxu
 * @date 2020/4/6 22:10
 */
@Repository
public interface LinkMapper {

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

	/**
	 * 查询单个前端
	 * @param id
	 * @return
	 */
	Link getLinkById(Long id);
}
