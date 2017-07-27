package com.mxzf.liyun.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Base64Util {

	public static final int MAX_SIZE = 200 * 1024 * 1024;// 200MB

	/**
	 * 二进制到文本
	 * 
	 * @param filePath
	 *            文件所在的路径
	 * @return String
	 */
	public static String getContent(String filePath) {
		byte[] data = compress(loadFile(filePath));
		return new String(Base64.getEncoder().encode(data));
	}

	/**
	 * 文本到二进制
	 * 
	 * @param content
	 *            Base64密文
	 * @return byte[]
	 */
	public static byte[] decodeContent(String content) {
		byte[] buff = Base64.getDecoder().decode(content);
		return uncompress(buff);
	}

	/**
	 * 加载本地文件,并转换为byte数组
	 * 
	 * @return
	 */
	private static byte[] loadFile(String filePath) {
		File file = new File(filePath);

		if (file.length() > MAX_SIZE) {
			throw new OutOfMemoryError("文件太大，转换压缩后再试!");
		}

		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		byte[] data = null;

		try {
			fis = new FileInputStream(file);
			baos = new ByteArrayOutputStream((int) file.length());

			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}

			data = baos.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}

				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * 对byte[]进行压缩
	 * 
	 * @param 要压缩的数据
	 * @return 压缩后的数据
	 */
	private static byte[] compress(byte[] data) {

		if (data == null)
			return null;
		System.out.println("before:" + data.length);

		GZIPOutputStream gzip = null;
		ByteArrayOutputStream baos = null;
		byte[] newData = null;

		try {
			baos = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(baos);

			gzip.write(data);
			gzip.finish();
			gzip.flush();

			newData = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				gzip.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("after:" + newData.length);
		return newData;
	}

	/**
	 * 对byte[]进行解压缩
	 * 
	 * @param 压缩后的数据
	 * @return 压缩前的数据
	 */
	private static byte[] uncompress(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
}