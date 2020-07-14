package com.zx.blog.entity;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Type implements Serializable {

    private static final long serialVersionUID = 5317164912946035898L;

    private Long id;
    @NotBlank(message = "分类名称不允许为空")
    private String name;  //分类名称
    private Long state;   //是否启用分类

    private List<Blog> blogs = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", blogs=" + blogs +
                '}';
    }
}