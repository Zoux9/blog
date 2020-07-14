package com.zx.blog.service.impl;

import com.zx.blog.dao.BlogMapper;
import com.zx.blog.dao.UserMapper;
import com.zx.blog.entity.Blog;
import com.zx.blog.entity.Tag;
import com.zx.blog.entity.User;
import com.zx.blog.service.BlogService;
import com.zx.blog.service.TagService;
import com.zx.blog.util.MarkDownUtil;
import com.zx.blog.util.MyBeanUtils;
import com.zx.blog.util.RedisComponentUtils;
import com.zx.blog.vo.BlogException;
import com.zx.blog.vo.SevenDays;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author zouxu
 * @date 2020/3/22 20:38
 */
@Service
@Transactional
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogMapper blogMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private TagService tagService;

	@Autowired
	private RedisComponentUtils redisService;

	@Override
	public int save(Blog blog) {
		Integer count = 0;
		if (blog.getId() == null) {

			blog.setCreateTime(new Date());
			blog.setUpdateTime(new Date());
			blog.setViews(0);

			//设置用户ID
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = userMapper.getUserByName(userDetails.getUsername());
			blog.setUserId(user.getId());

			count = blogMapper.save(blog);

			//插入中间表
			setBlogAndTag(blog, count);
		} else {

			Blog BlogInfo = blogMapper.getBlogById(blog.getId());

			if (BlogInfo != null) {
				BeanUtils.copyProperties(blog, BlogInfo, MyBeanUtils.getNullPropertyNames(blog));

				//删除中间表
				blogMapper.deleteBlogAndTag(BlogInfo.getId());

				//设置用户ID
				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				User user = userMapper.getUserByName(userDetails.getUsername());

				BlogInfo.setUserId(user.getId());
				BlogInfo.setUpdateTime(new Date());

				try {
					//Redis缓存一致性:双删
					String key = "blog:" + blog.getId() + ":info";
					redisService.remove(key);
					count = blogMapper.updateBlog(BlogInfo);
					setBlogAndTag(BlogInfo, count);
					Thread.sleep(500);
					redisService.remove(key);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				throw new RuntimeException();
			}
		}
		return count;
	}

	private Integer setBlogAndTag(Blog blog, Integer count) {
		List<Tag> tags = blog.getTags();
		for (Tag tag : tags) {
			Map<String, Long> map = new HashMap<>();
			map.put("tagId", tag.getId());
			map.put("blogId", blog.getId());
			count = blogMapper.saveBlogAndTag(map);
		}
		return count;
	}

	@Override
	public List<Blog> adminBlogInfo() {
		return blogMapper.adminBlogInfo();
	}

	@Override
	public List<Blog> searchBlog(Blog blog) {
		return blogMapper.searchBlog(blog);
	}


	@Override
	public int deleteBlog(Long id) {
		blogMapper.deleteBlogAndTag(id);
		return blogMapper.deleteBlog(id);
	}

	@Override
	public Blog getBlogById(Long id) {
		return blogMapper.getBlogById(id);
	}

	public Blog getBlogConvertDB(Long id) {
		Blog blog = blogMapper.getBlogById(id);

		if (blog == null) {
			throw new BlogException("404", "文章不存在");
		}
		//阅读量
		blog.setViews(blog.getViews() + 1);
		blogMapper.updateBlog(blog);

		Blog b = new Blog();
		BeanUtils.copyProperties(blog, b);
		String content = b.getContent();
		b.setContent(MarkDownUtil.markdownToHtmlExtensions(content));
		return b;
	}


	/**
	 * redis缓存
	 *
	 * @param id
	 * @return
	 */
	@Override
	public Blog getBlogConvert(Long id) {
		//设置缓存Key
		String key = "blog:" + id + ":info";
		Blog blog = null;
		//读取缓存
		blog = (Blog) redisService.get(key);

		if (blog != null) {
			//缓存有则返回
			return blog;
		} else {
			//没有则查询数据库
			blog = getBlogConvertDB(id);
			if (blog != null) {
				redisService.set(key, blog);
			}
		}
		return blog;
	}


	@Override
	public List<Blog> getListBlog() {
		return blogMapper.getListBlog();
	}

	@Override
	public List<Blog> getViewsRanking() {
		return blogMapper.getViewsRanking();
	}

	@Override
	public List<Blog> getRecommend() {
		return blogMapper.getRecommend();
	}

	@Override
	public List<Blog> getCarousel() {
		return blogMapper.getCarousel();
	}

	@Override
	public List<Blog> getBlogNotice() {
		return blogMapper.getBlogNotice();
	}

	@Override
	public List<Blog> searchTitleAndContent(String keyboard) {
		return blogMapper.searchTitleAndContent(keyboard);
	}

	@Override
	public List<Blog> getBlogByTypeId(Long typeId) {
		return blogMapper.getBlogByTypeId(typeId);
	}

	@Override
	public List<Blog> getAbout() {
		return blogMapper.getAbout();
	}

	@Override
	public List<Blog> getBlogByTagId(Long tagId) {
		return blogMapper.getBlogByTagId(tagId);
	}

	@Override
	public List<SevenDays> findSevenDaysBlog() {
		List<SevenDays> sevenDaysBlog = blogMapper.findSevenDaysBlog();
		sevenDaysBlog.sort(Comparator.comparing(SevenDays::getClickDate).reversed());
		return sevenDaysBlog;
	}

	@Override
	public Integer blogCount() {
		return blogMapper.blogCount();
	}


}
