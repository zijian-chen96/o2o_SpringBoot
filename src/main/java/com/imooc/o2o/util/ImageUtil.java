package com.imooc.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.imooc.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	//private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	
	private static String basePath = PathUtil.getImgBasePath();
			
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final Random r = new Random();

	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	
	private static final String watermark = "/PinkPig.png";

	/**
	 * CommonsMultipartFile transfer to File class
	 * 
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
	}

	/**
	 * create small image and return the new image relative path
	 * 
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
		// create unique file name
		String realFileName = getRandomFileName();
		// get the extension name
		String extension = getFileExtension(thumbnail.getImageName());
		// if target path is nor exits, it will auto create
		makeDirPath(targetAddr);
		// get file save relative addr(include file name)
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is :" + relativeAddr);
		// get file final save path
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
		
		// use the thumbnail to create watermark image
		try {
			Thumbnails.of(thumbnail.getImage()).forceSize(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + watermark)), 0.25f)
					.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		// return the image full path
		return relativeAddr;
	}
	
	/**
	 * create detail image and return the new image relative path
	 * 
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		// create unique file name
		String realFileName = getRandomFileName();
		// get the extension name
		String extension = getFileExtension(thumbnail.getImageName());
		// if target path is nor exits, it will auto create
		makeDirPath(targetAddr);
		// get file save relative addr(include file name)
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is :" + relativeAddr);
		// get file final save path
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
		
		// use the thumbnail to create watermark image
		try {
			Thumbnails.of(thumbnail.getImage()).forceSize(337, 640)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + watermark)), 0.25f)
					.outputQuality(0.9f).toFile(dest);
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		// return the image full path 
		return relativeAddr;
	}

	/**
	 * create the all target folders on target path ex:
	 * /home/projectdev/image/xxx.jpg, -> will create home, projectdev, image
	 * folders
	 * 
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

	}

	/**
	 * get file extension name
	 * 
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * generate new filename - yyyymmddhhmmss + 5 random numbers
	 * 
	 * @return
	 */
	public static String getRandomFileName() {
		// get 5 random numbers
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}

	/**
	 * if storePath is file path, and it will delete the file
	 * if storePath is a folder path, and it will delete all files under the folder path
	 * 
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		if (fileOrPath.exists()) {
			if (fileOrPath.isDirectory()) {
				File files[] = fileOrPath.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}

	public static void main(String[] args) throws IOException {
		Thumbnails.of(new File("/Users/chen/projectimage/murshroom.jpg")).forceSize(200, 200)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + watermark)), 0.25f)
				.outputQuality(0.8f).toFile("/Users/chen/projectimage/murshroomnew.jpg");

	}

}
