package com.zx.blog.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.zx.blog.dao.BlogMapper;
import com.zx.blog.dao.UserMapper;
import com.zx.blog.entity.Blog;
import com.zx.blog.entity.Tag;
import com.zx.blog.entity.User;
import com.zx.blog.handler.BloomFilterHelper;
import com.zx.blog.service.BlogService;
import com.zx.blog.util.MarkDownUtil;
import com.zx.blog.util.MyBeanUtils;
import com.zx.blog.util.RedisComponentUtils;
import com.zx.blog.exception.BlogException;
import com.zx.blog.dto.SevenDaysDto;
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
@Transactional(rollbackFor = Exception.class)
public class BlogServiceImpl implements BlogService {

	private final BlogMapper blogMapper;

	private final UserMapper userMapper;

	private final RedisComponentUtils redisService;

	@Autowired
	public BlogServiceImpl(BlogMapper blogMapper, UserMapper userMapper, RedisComponentUtils redisService) {
		this.blogMapper = blogMapper;
		this.userMapper = userMapper;
		this.redisService = redisService;
	}

	@Override
	public int save(Blog blog) {
		int count = 0;
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

			//写入布隆过滤器
			BloomFilterHelper<String> myBloomFilterHelper = new BloomFilterHelper<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 1000000, 0.0001);
			redisService.addByBloomFilter(myBloomFilterHelper, "blog_id_existed_bloom", blog.getId().toString());

			//缓存分页
			if (blog.isPublished()){
				this.blogPage(blog);
			}
		} else {

			Blog blogInfo = blogMapper.getBlogById(blog.getId());

			if (blogInfo != null) {
				BeanUtils.copyProperties(blog, blogInfo, MyBeanUtils.getNullPropertyNames(blog));

				//删除中间表标签
				blogMapper.deleteBlogAndTag(blogInfo.getId());

				//设置用户ID
				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				User user = userMapper.getUserByName(userDetails.getUsername());

				blogInfo.setUserId(user.getId());
				blogInfo.setUpdateTime(new Date());

				try {
					//Redis缓存一致性:双删
					String key = "blog:" + blog.getId() + ":info";
					redisService.remove(key);

					//发布的文章保存,删除分页缓存
					if (!blog.isPublished()){
						redisService.remove(key, blog);
						redisService.delPage("blog", blog.getId().toString());
					}

					//更新文章内容
					count = blogMapper.updateBlog(blogInfo);
					//从写入标签
					setBlogAndTag(blogInfo, count);
					//加入缓存分页
					if (blog.isPublished()){
						this.blogPage(blog);
					}
					Thread.sleep(100);

					//发布的文章保存,删除分页缓存
					if (!blog.isPublished()){
						redisService.remove(key, blog);
						redisService.delPage("blog", blog.getId().toString());
					}
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

	/**
	 * 分页
	 * @param blog
	 */
	private void blogPage(Blog blog) {
		Blog blogPage = blogMapper.getBlogById(blog.getId());
		String hkey = blogPage.getId().toString();
		redisService.setPage("blog", hkey, Double.parseDouble(hkey), blogPage);

		//首页分类缓存
		String key = "blog_type::blog_type_id_" + blogPage.getType().getId();
		redisService.zAdd(key, blogPage, blogPage.getType().getId());
	}

	/**
	 * 保存文章与标签中间表
	 * @param blog
	 * @param count
	 */
	private void setBlogAndTag(Blog blog, Integer count) {
		List<Tag> tags = blog.getTags();
		for (Tag tag : tags) {
			Map<String, Long> map = new HashMap<>(3); // 2 / 0.75 + 1
			map.put("tagId", tag.getId());
			map.put("blogId", blog.getId());
			blogMapper.saveBlogAndTag(map);
		}
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
		int count = 0;
		try {
			//TypeId获取
			Blog blog = blogMapper.getBlogById(id);
			String key = "blog_type::blog_type_id_" + blog.getType().getId();

			//删缓存
			redisService.remove(key, blog);
			redisService.delPage("blog", id.toString());

			//删库
			blogMapper.deleteBlogAndTag(id);
			blogMapper.deleteBlog(id);
			Thread.sleep(100);

			//删缓存
			redisService.remove(key, blog);
			redisService.delPage("blog", id.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return count;
	}

	@Override
	public Map<String,Object> getBlogById(Long id) {
		Blog blog = blogMapper.getBlogById(id);
		Map<String,Object> map = new HashMap<>();
		if (blog != null) {
			//前端回显标签转换
			List<Long> ids = new ArrayList<>();
			String tag = blog.getTagIds();
			if (tag != null) {
				String[] tagIds = tag.split(",");
				ids = new ArrayList<>();
				for (String s : tagIds) {
					ids.add(Long.valueOf(s));
				}
			}
			map.put("ids", ids);
			map.put("blog", blog);
		}
		return map;
	}

	public Blog getBlogConvertDB(Long id) {
		BloomFilterHelper<String> myBloomFilterHelper = new BloomFilterHelper<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 1000000, 0.0001);
		boolean existed = redisService.includeByBloomFilter(myBloomFilterHelper, "blog_id_existed_bloom", id.toString());

		Blog blog = null;

		//检测布隆过滤器是否存在ID
		if (existed) {
			blog = blogMapper.getBlogById(id);
		}

		//异常返回
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
			Integer view = this.updateBlogView(blog.getId());
			blog.setViews(view);
			//缓存有则返回
			return blog;
		} else {
			//没有则查询数据库
			blog = this.getBlogConvertDB(id);
			if (blog != null) {
				redisService.set(key, blog, (long) (60 * 60 * 12));
			}
		}
		return blog;
	}


	@Override
	public PageInfo<Blog> getListBlog(int pageNum, int pageSize) {
		String key = "blog";

		List<Blog> blogList = new ArrayList<>();
		Set<Object> obj = redisService.getPage(key, pageNum, pageSize);
		if (!obj.isEmpty()) {
			if (obj instanceof HashSet<?>) {
				for (Object anObj : obj) {
					String blogId = (String) anObj;
					Blog blog = (Blog) redisService.hmGet(key, blogId);
					if (blog.isPublished()){
						blogList.add(blog);
					}
				}
			}
		} else {
			//没有缓存查询数据库
			blogList = blogMapper.getListBlog();
			if (!blogList.isEmpty()) {
				//数据添加到布隆过滤器
				BloomFilterHelper<String> myBloomFilterHelper = new BloomFilterHelper<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 1000000, 0.0001);
				for (Blog blog : blogList) {
					redisService.addByBloomFilter(myBloomFilterHelper, "blog_id_existed_bloom", blog.getId().toString());
					String hkey = blog.getId().toString();
					redisService.setPage(key, hkey, Double.valueOf(hkey), blog);
				}
			}
		}

		Page<Blog> blogPage = new Page<>();
		blogPage.setPageNum(pageNum);
		blogPage.setPageSize(pageSize);
		blogPage.setTotal(redisService.getPageSize("blog"));
		PageInfo<Blog> blogPageInfo = new PageInfo<>(blogPage);
		blogPageInfo.setList(blogList);
		return blogPageInfo;
	}


	@Override
	public List<Blog> getViewsRanking() {
		return blogMapper.getViewsRanking();
	}

	@Override
	public Integer updateBlogView(Long blogId) {
		Integer view = this.getBlogViewsByBlogId(blogId);
		if (view == null) {
			return null;
		}
		redisService.hput("blog_views::blog_views_id_" + blogId,String.valueOf(blogId), view + 1);
		return view;
	}

	/**
	 * 获得某篇文章的访问量
	 *
	 * @param blogId
	 * @return
	 */
	@Override
	public Integer getBlogViewsByBlogId(Long blogId) {
		Integer view = (Integer) redisService.hmGet("blog_views::blog_views_id_" + blogId, String.valueOf(blogId));
		if (view == null) {
			Blog blog = blogMapper.getBlogById(blogId);
			if (blog == null) {
				return null;
			}
			redisService.hput("blog_views::blog_views_id_" + blogId, String.valueOf(blogId), blog.getViews());
			return blog.getViews();
		}
		return view;
	}


	@Override
	public List<Blog> getBlogHeadline() {
		return blogMapper.getBlogHeadline();
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
		String key = "blog_type::blog_type_id_" + typeId;
		List<Blog> blogList = new ArrayList<>();
		//读取缓存
		Set<Object> obj = redisService.zRange(key, 0, -1);
		if (!obj.isEmpty()) {
			if (obj instanceof HashSet<?>) {
				for (Object anObj : obj) {
					Blog blog = (Blog) anObj;
					if (blog.isPublished()){
						blogList.add(blog);
					}
				}
			}
		} else {
			//没有则查询数据库
			blogList = blogMapper.getBlogByTypeId(typeId);
			if (blogList != null) {
				for (Blog blog : blogList) {
					redisService.zAdd(key, blog, blog.getId());
				}
			}
		}
		return blogList;

	}

	@Override
	public List<Blog> getBlogTime() {
		return blogMapper.getListBlog();
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
	public List<SevenDaysDto> findSevenDaysBlog() {
		List<SevenDaysDto> sevenDaysBlog = blogMapper.findSevenDaysBlog();
		if (sevenDaysBlog.isEmpty()) {
			return null;
		}
		return sevenDaysBlog;
	}

	@Override
	public Integer countBlog() {
		return blogMapper.blogCount();
	}


}
