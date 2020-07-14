package com.zx.blog.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Blog implements Serializable {

	private static final long serialVersionUID = -394962998699109009L;

	private Long id;
	private String title;               //标题
	private String content;             //内容
	private String firstPicture;        //首图
	private String flag;                //标记
	private Integer views;              //浏览次数
	private boolean appreciation;       //是否开启赞赏
	private boolean shareStatement;     //是否开启版权说明
	private boolean carousel;           //是否开启首页轮播
	private boolean notice;             //公告
	private boolean comment;            //是否开启评论
	private boolean published;          //是否发布文章
	private boolean recommend;          //是否推荐
	private Date createTime;            //文章创建时间
	private Date updateTime;            //文章更新时间

	//关联查询
	private Long typeId;
	private Long userId;

	//获取多个标签的id
	private String tagIds;              //标签ID
	private String description;         //简介

	private Type type;                  //分类
	private User user;                  //用户

	private List<Tag> tags = new ArrayList<>();

	private List<Comment> comments = new ArrayList<>();


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFirstPicture() {
		return firstPicture;
	}

	public void setFirstPicture(String firstPicture) {
		this.firstPicture = firstPicture;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public boolean isAppreciation() {
		return appreciation;
	}

	public void setAppreciation(boolean appreciation) {
		this.appreciation = appreciation;
	}

	public boolean isShareStatement() {
		return shareStatement;
	}

	public void setShareStatement(boolean shareStatement) {
		this.shareStatement = shareStatement;
	}

	public boolean isCarousel() {
		return carousel;
	}

	public void setCarousel(boolean carousel) {
		this.carousel = carousel;
	}

	public boolean isNotice() {
		return notice;
	}

	public void setNotice(boolean notice) {
		this.notice = notice;
	}

	public boolean isComment() {
		return comment;
	}

	public void setComment(boolean comment) {
		this.comment = comment;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Blog{" +
				"id=" + id +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", firstPicture='" + firstPicture + '\'' +
				", flag='" + flag + '\'' +
				", views=" + views +
				", appreciation=" + appreciation +
				", shareStatement=" + shareStatement +
				", carousel=" + carousel +
				", notice=" + notice +
				", comment=" + comment +
				", published=" + published +
				", recommend=" + recommend +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", typeId=" + typeId +
				", userId=" + userId +
				", tagIds='" + tagIds + '\'' +
				", description='" + description + '\'' +
				", type=" + type +
				", user=" + user +
				", tags=" + tags +
				", comments=" + comments +
				'}';
	}
}
