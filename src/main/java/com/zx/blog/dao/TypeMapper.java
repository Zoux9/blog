package com.zx.blog.dao;

import com.zx.blog.entity.Type;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zouxu
 * @date 2020/3/20 20:23
 */
@Repository
public interface TypeMapper {

	List<Type> getListType();

	int saveType(Type type);

	int deleteType(Long id);

	int updateType(Type type);

	Type getByTypeId(Long id);

	Type getTypeByName(String typeName);

	Integer typeCount();
}
