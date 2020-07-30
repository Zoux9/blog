package com.zx.blog.service;

import com.github.pagehelper.PageInfo;
import com.zx.blog.entity.Blog;
import com.zx.blog.dto.SevenDaysDto;

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

	PageInfo<Blog> getListBlog(int offset, int count);

	List<Blog> getViewsRanking();

	/**
	 * 修改文章阅读量
	 * @param blogId
	 * @return
	 */
	Integer updateBlogView(Long blogId);

	/**
	 * 获得某篇文章的访问量
	 * @param BlogId
	 * @return
	 */
	Integer getBlogViewsByBlogId(Long BlogId);
	/**
	 * 首页头条
	 * @return
	 */
	List<Blog> getBlogHeadline();

	List<Blog>  getRecommend();

	List<Blog>  getCarousel();

	List<Blog> getBlogNotice();

	List<Blog> searchTitleAndContent(String keyboard);

	List<Blog> getBlogByTypeId(Long typeId);

	List<Blog> getBlogTime();

	List<Blog> getAbout();

	List<Blog> getBlogByTagId(Long tagId);

	List<SevenDaysDto> findSevenDaysBlog();

	Integer countBlog();


}
