package com.zx.blog.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

public class ObjectUtil {

	public static String mapToString(Map<String, String[]> paramMap) {
		if (paramMap == null) {
			return "";
		}
		Map<String, Object> params = new HashMap<>(16);
		for (Map.Entry<String, String[]> param : paramMap.entrySet()) {

			String key = param.getKey();
			String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
			String obj = StrUtil.endWithIgnoreCase(param.getKey(), "password") ? "你是看不见我的" : paramValue;
			params.put(key, obj);
		}
		return JSON.toJSONString(params);
	}

	public static String mapToStringAll(Map<String, String[]> paramMap) {

		if (paramMap == null) {
			return "";
		}
		Map<String, Object> params = new HashMap<>(16);
		for (Map.Entry<String, String[]> param : paramMap.entrySet()) {

			String key = param.getKey();
			String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
			params.put(key, paramValue);
		}
		return JSON.toJSONString(params);
	}

	public static <T> Map<String, Object> beanToMap(T bean) {
		Map<String, Object> map = new HashMap<>(16);
		if (bean != null) {
			BeanMap beanMap = BeanMap.create(bean);
			for (Object key : beanMap.keySet()) {
				map.put(key + "", beanMap.get(key));
			}
		}
		return map;
	}

	public static Map<String, Object> getStringToMap(String str) {
		str = str.substring(1, str.length() - 1);
		//根据逗号截取字符串数组
		String[] str1 = str.split(",");
		//创建Map对象
		Map<String, Object> map = new HashMap<>(16);
		//循环加入map集合
		for (String s : str1) {
			//根据":"截取字符串数组
			String[] str2 = s.split("=");
			//str2[0]为KEY,str2[1]为值
			map.put(str2[0], str2[1]);
		}
		return map;
	}

}