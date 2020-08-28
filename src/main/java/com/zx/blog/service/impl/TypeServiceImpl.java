package com.zx.blog.service.impl;

import com.zx.blog.dao.TypeMapper;
import com.zx.blog.entity.Type;
import com.zx.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author zouxu
 * @date 2020/3/20 20:26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TypeServiceImpl implements TypeService {

	private final TypeMapper typeMapper;

	@Autowired
	public TypeServiceImpl(TypeMapper typeMapper) {
		this.typeMapper = typeMapper;
	}


	@Override
	public List<Type> getListType() {
		return typeMapper.getListType();
	}

	@Override
	public int saveType(Type type) {
		return typeMapper.saveType(type);
	}

	@Override
	public int deleteType(Long id) {
		return typeMapper.deleteType(id);
	}

	@Override
	public int updateType(Type type) {
		return typeMapper.updateType(type);
	}

	@Override
	public Type getTypeById(Long id) {
		return typeMapper.getByTypeId(id);
	}

	@Override
	public Type getTypeByName(String typeName) {
		return typeMapper.getTypeByName(typeName);
	}

	@Override
	public Integer countType() {
		return typeMapper.typeCount();
	}

}
