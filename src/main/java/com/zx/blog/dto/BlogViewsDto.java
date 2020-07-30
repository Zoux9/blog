package com.zx.blog.dto;

/**
 * @author zouxu
 * @date 2020/7/24 1:54
 */
public class BlogViewsDto {
	private Long id;
	private Integer views;

	public BlogViewsDto(Long id, Integer views) {
		this.id = id;
		this.views = views;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}
}
