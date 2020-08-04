package com.zx.blog.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.zx.blog.util.FileCopyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author zouxu
 * @date 2020/4/6 14:44
 */
@Controller
@RequestMapping("/admin")
public class ImageUploadController {
	@ResponseBody
	@RequestMapping(value = "/imageUpload", method = RequestMethod.POST,headers="content-type=multipart/form-data")
	public List<JSONObject> imageUpload(HttpServletRequest request, HttpServletResponse response,
	                        @RequestParam(value = "images", required = false) MultipartFile[] images) {
		List<JSONObject> jsonObjectList = new ArrayList<>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setHeader("Content-Type", "application/json");

			for (MultipartFile file : images) {
				JSONObject jsonObject = new JSONObject();
				File targetFile = null;
				String url = "";
				String fileName = file.getOriginalFilename();

				if (StringUtils.isNotBlank(fileName)) {
					//图片访问的URI
					String returnUrl = request.getScheme() + "://" + request.getServerName() + request.getContextPath() + "/upload/images/";
					//文件临时存储位置
					String path = request.getSession().getServletContext().getRealPath("") + "upload" + File.separator + "images";

					//文件后缀
					String fileSuffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
					//新的文件名
					fileName = System.currentTimeMillis() + "_" + new Random().nextInt(1000) + fileSuffix;

					//先判断文件是否存在
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String fileAdd = sdf.format(new Date());
					//获取文件夹路径
					path = path + File.separator + fileAdd + File.separator;
					File file1 = new File(path);

					//如果文件夹不存在则创建
					if (!file1.exists() && !file1.isDirectory()) {
						if (!file1.mkdirs()) {
							throw new IOException("文件夹创建失败,路径为：" + file1);
						}
					}
					//将图片存入文件夹
					targetFile = new File(file1, fileName);

					//将上传的文件写到服务器上指定的文件。
					file.transferTo(targetFile);
					String projectPath = System.getProperty("user.dir");
					//文件复制
					String src = path + fileName;
					//根据自己系统的resource 目录所在位置进行自行配置
					String destDir = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "upload" + File.separator + "images" + File.separator + fileAdd + File.separator;
					FileCopyUtil.copyFile(src, destDir, fileName);

					url = returnUrl + fileAdd + "/" + fileName;

					jsonObject.put("code", 1);
					jsonObject.put("message", "图片上传成功");
					jsonObject.put("url", url);
					jsonObjectList.add(jsonObject);
				}
			}

		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 0);
			jsonObject.put("message", "图片上传失败");
			jsonObjectList.add(jsonObject);
		}
		return jsonObjectList;
	}


}
