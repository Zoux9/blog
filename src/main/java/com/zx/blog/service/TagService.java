package com.zx.blog.service;

import com.zx.blog.entity.Blog;
import com.zx.blog.entity.Tag;

import java.util.List;

/**
 * @author zouxu
 * @date 2020/3/20 20:25
 */

public interface TagService {

	List<Tag> getListTag();

	List<Tag> listTagByIds(String ids);

	int saveTag(Tag tag);

	int deleteTag(Long id);

	int updateTag(Tag tag);

	Tag getTagById(Long id);

	Tag getTagByName(String tagName);

	Integer countTag();
}
