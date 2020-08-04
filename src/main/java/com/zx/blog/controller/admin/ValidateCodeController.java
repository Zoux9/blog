package com.zx.blog.controller.admin;

import com.wf.captcha.SpecCaptcha;
import com.zx.blog.util.RedisComponentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ValidateCodeController {

	private final RedisComponentUtils redisService;

	public ValidateCodeController(RedisComponentUtils redisService) {
		this.redisService = redisService;
	}

	@RequestMapping("/captcha")
	public Map<String,String> captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,String> result = new HashMap<>(3);

		SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
		String verCode = specCaptcha.text().toLowerCase();
		String key = UUID.randomUUID().toString();
		// 存入redis并设置过期时间为30分钟
		redisService.set(key, verCode, (long) 60);
		// 将key和base64返回给前端
		result.put("key", key);
		result.put("image", specCaptcha.toBase64());

		return result;
	}
}