package com.zx.blog.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author zouxu
 * @date 2020/4/6 16:24
 */
public class FileCopyUtil {

	/**
	 * 文件复制
	 * @param src
	 * @param destDir
	 * @param fileName
	 * @throws IOException
	 */
	public static void copyFile(String src,String destDir,String fileName) throws IOException {
		FileInputStream in=new FileInputStream(src);
		File fileDir = new File(destDir);
		if(!fileDir.isDirectory()){
			fileDir.mkdirs();
		}
		File file = new File(fileDir,fileName);

		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream out=new FileOutputStream(file);
		int c;
		byte buffer[]=new byte[1024];
		while((c=in.read(buffer))!=-1){
			for(int i=0;i<c;i++){
				out.write(buffer[i]);
			}

		}
		in.close();
		out.close();
	}
}
