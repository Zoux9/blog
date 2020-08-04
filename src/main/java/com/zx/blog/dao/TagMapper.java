package com.zx.blog.dao;

import com.zx.blog.entity.Blog;
import com.zx.blog.entity.Tag;
import com.zx.blog.entity.Type;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author zouxu
 * @date 2020/3/20 20:23
 */
@Repository
public interface TagMapper {

	List<Tag> getListTag();

	int saveTag(Tag tag);

	int deleteTag(Long id);

	int updateTag(Tag tag);

	Tag getByTagId(Long id);

	Tag getTagByName(String tagName);

	int updateBlog(Blog blog);

	Integer tagCount();
}
