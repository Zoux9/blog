package com.zx.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * @author zouxu
 * @date 2020/4/2 23:50
 */
public class SevenDaysDto {

	@JsonFormat(pattern = "yyyy-MM-dd")
	private String clickDate;
	private Integer count;

	public String getClickDate() {
		return clickDate;
	}

	public void setClickDate(String clickDate) {
		this.clickDate = clickDate;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
