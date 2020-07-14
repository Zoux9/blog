package com.zx.blog.service;


import com.zx.blog.entity.Type;

import java.util.List;

/**
 * @author zouxu
 * @date 2020/3/20 20:25
 */

public interface TypeService {

	List<Type> getListType();

	int saveType(Type type);

	int deleteType(Long id);

	int updateType(Type type);

	Type getTypeById(Long id);

	Type getTypeByName(String typeName);

	Integer typeCount();
}
