package com.zx.blog.service.impl;

import com.zx.blog.dao.LinkMapper;
import com.zx.blog.entity.Link;
import com.zx.blog.service.LinkService;
import com.zx.blog.vo.BlogException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zouxu
 * @date 2020/4/6 22:03
 */
@Service
public class LinkServiceImpl implements LinkService {

	@Autowired
	private LinkMapper linkMapper;

	@Override
	public Integer applyLink(Link link) {
		Integer count = 0;

		if (StringUtils.isBlank(link.getLinkName())) {
			throw new BlogException("500", "名称参数错误");
		}
		if (StringUtils.isBlank(link.getLinkUrl())) {
			throw new BlogException("500", "URL参数错误");
		}

		link.setCreateTime(new Date());
		link.setUpdateTime(new Date());
		link.setLinkRank(99);

		if (link.getIsDeleted() == null) {
			link.setIsDeleted(false);
		} else {
			link.setIsDeleted(link.getIsDeleted());
		}
		count = linkMapper.applyLink(link);

		return count;
	}

	@Override
	public Integer updateLink(Link link) {
		Integer count = 0;
		if (link.getLinkName() != null && link.getLinkUrl() != null) {
			link.setUpdateTime(new Date());
			count = linkMapper.updateLink(link);
		} else {
			throw new BlogException("404", "友链不存在");
		}
		return count;
	}

	@Override
	public List<Link> getListLink() {
		return linkMapper.getListLink();
	}

	@Override
	public List<Link> getShowLink() {
		return linkMapper.getShowLink();
	}

	@Override
	public Link getLinkById(Long id) {
		if (id == null) {
			throw new BlogException("404","友链信息不存在");
		}
		return linkMapper.getLinkById(id);
	}
}
