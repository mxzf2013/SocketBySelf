package com.mxzf.liyun.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 
 * @Title: ImageUtil
 * @Dscription: 图片处理工具
 * @author Deleter
 * @date 2017年5月21日 上午2:41:17
 * @version 1.0
 */
public class ImageUtil {
    public static final long MAX_SIZE = 100 * 1024;// 200k

    /**
     * 缩放图片
     * 
     * @param filePath
     *            图片所在的路径
     * @return String 图片路径
     */
    public static String toSmall(String filePath) {
	String resultPath = filePath;
	try {
	    File file = new File(filePath);
	    if (file.length() > MAX_SIZE) {
		BufferedImage originalImage = ImageIO.read(file);
		BufferedImage thumbnail = Thumbnails.of(originalImage)
			.scale(0.25f).asBufferedImage();
		resultPath = getRootPath(filePath) + System.currentTimeMillis()
			+ ".jpg";
		ImageIO.write(thumbnail, "jpg", new FileOutputStream(new File(
			resultPath)));
	    } else {
		resultPath = filePath;
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return resultPath;
    }

    public static String getThumbImage(String filePath, int width, int height) {
	String resultPath = filePath;
	try {
	    File file = new File(filePath);
	    BufferedImage originalImage = ImageIO.read(file);
	    BufferedImage thumbnail = Thumbnails.of(originalImage)
		    .size(width, height).asBufferedImage();
	    resultPath = getRootPath(filePath) + System.currentTimeMillis()
		    + ".jpg";
	    ImageIO.write(thumbnail, "jpg", new FileOutputStream(new File(
		    resultPath)));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return resultPath;
    }

    private static String getRootPath(String filePath) {
	return filePath.substring(0, filePath.lastIndexOf("/")) + "/";
    }

    public static void main(String[] args) {
	String path = "c:/liyun/asdasd/asdasd/a.jpg";
	String newPath = path.substring(0, path.lastIndexOf("/")) + "/";
	System.out.println(newPath);
    }
}
