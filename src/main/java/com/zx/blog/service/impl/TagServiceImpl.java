package com.zx.blog.service.impl;

import com.zx.blog.dao.TagMapper;
import com.zx.blog.entity.Blog;
import com.zx.blog.entity.Tag;
import com.zx.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zouxu
 * @date 2020/3/20 20:26
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

	private final TagMapper tagMapper;

	@Autowired
	public TagServiceImpl(TagMapper tagMapper) {
		this.tagMapper = tagMapper;
	}


	@Override
	public List<Tag> getListTag() {
		return tagMapper.getListTag();
	}

	@Override
	public List<Tag> listTagByIds(String ids) {

		List<Tag> list = new ArrayList<>();
		if (!"".equals(ids) && ids != null){
			String[] idsArray = ids.split(",");
			for (int i = 0; i < idsArray.length; i++) {
				Tag tag = tagMapper.getByTagId(Long.valueOf(idsArray[i]));
				list.add(tag);
			}
		}
		return list;
	}


	@Override
	public int saveTag(Tag tag) {
		return tagMapper.saveTag(tag);
	}

	@Override
	public int deleteTag(Long id) {

		return tagMapper.deleteTag(id);
	}

	@Override
	public int updateTag(Tag tag) {
		return tagMapper.updateTag(tag);
	}

	@Override
	public Tag getTagById(Long id) {
		return tagMapper.getByTagId(id);
	}

	@Override
	public Tag getTagByName(String tagName) {
		return tagMapper.getTagByName(tagName);
	}


	@Override
	public Integer countTag() {
		return tagMapper.tagCount();
	}




}
