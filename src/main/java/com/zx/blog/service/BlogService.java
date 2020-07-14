package com.zx.blog.service;

import com.zx.blog.entity.Blog;
import com.zx.blog.vo.SevenDays;

import java.util.List;

/**
 * @author zouxu
 * @date 2020/3/22 20:37
 */
public interface BlogService {

	int save(Blog blog);

	List<Blog> adminBlogInfo();

	List<Blog> searchBlog(Blog query);

	int deleteBlog(Long id);

	Blog getBlogById(Long id);

	Blog getBlogConvert(Long id);

	List<Blog> getListBlog();

	List<Blog> getViewsRanking();

	List<Blog>  getRecommend();

	List<Blog>  getCarousel();

	List<Blog> getBlogNotice();

	List<Blog> searchTitleAndContent(String keyboard);

	List<Blog> getBlogByTypeId(Long typeId);

	List<Blog> getAbout();

	List<Blog> getBlogByTagId(Long tagId);

	List<SevenDays> findSevenDaysBlog();

	Integer blogCount();


}
