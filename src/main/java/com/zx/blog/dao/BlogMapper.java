package com.zx.blog.dao;

import com.zx.blog.entity.Blog;
import com.zx.blog.dto.BlogViewsDto;
import com.zx.blog.dto.SevenDaysDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author zouxu
 * @date 2020/3/22 20:39
 */
@Repository
public interface BlogMapper {

	int save(Blog blog);

	List<Blog> adminBlogInfo();

	List<Blog> searchBlog(Blog blog);

	List<Blog> searchTitleAndContent(String keyboard);

	int deleteBlog(Long id);

	Blog getBlogById(Long id);

	List<Blog> getBlogHeadline();

	List<Blog> getListBlog();

	List<Blog> getViewsRanking();

	List<Blog> getRecommend();

	List<Blog> getCarousel();

	List<Blog> getBlogNotice();

	List<Blog> getBlogByTypeId(Long typeId);

	List<Blog> getAbout();

	List<Blog> getBlogByTagId(Long tagId);

	List<SevenDaysDto> findSevenDaysBlog();

	Integer blogCount();

	void saveBlogAndTag(Map<String, Long> map);

	Integer deleteBlogAndTag(Long blogId);

	Integer updateBlog(Blog blog);

	void batchUpdateBlogViews(@Param("list") List<BlogViewsDto> dtoList);
}
