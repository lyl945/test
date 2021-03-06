package com.easemob.chatuidemo.imageutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * SD卡的获取图片器
 * 
 * @author hugo
 * 
 */
public class SDFileFetcher implements Fetchable {

	private final static String TAG = "SDFileFetcher";

	private static final String WHOLESALE_CONV = ".cache";

	private static final int MB = 1024 * 1024;
	/**
	 * Cache容量
	 */
	private static final int CACHE_SIZE = 10;
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

	private String sdPath;

	public SDFileFetcher(String sdPath) {
		this.sdPath = sdPath;
		this.removeCache(getDirectory());
	}

	/**
	 * 
	 */
	@Override
	public Bitmap fetch(String photoUrl) {
		String path = getDirectory() + File.separator
				+ convertUrlToFileName(photoUrl);
		File file = new File(path);
		// 如果缓存中存在
		if (file.exists()) {
			Bitmap bmp = BitmapFactory.decodeFile(path);
			if (bmp == null) {
				file.delete();
			} else {
				updateFileTime(path);
				return bmp;
			}
		}
		return null;
	}

	@Override
	public void cache(String key, Bitmap bitmap) throws Exception {
		Log.d(TAG, "SDFileFetcher cache");
		if (bitmap == null) {
			return;
		}
		// 判断sdcard上的空间，如果空间不足，直接返回
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			return;
		}
		String filename = convertUrlToFileName(key);
		String dir = getDirectory();
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File file = new File(dir + File.separator + filename);
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			Log.w("FileNotFoundException", e);
		} catch (IOException e) {
			Log.w("IOException", e);
		}
	}

	/**
	 * 计算存储目录下的文件大小，
	 * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
	 * 那么删除40%最近没有被使用的文件
	 */
	private boolean removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return true;
		}
		if (!android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return false;
		}

		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(WHOLESALE_CONV)) {
				dirSize += files[i].length();
			}
		}

		if (dirSize > CACHE_SIZE * MB
				|| FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			int removeFactor = (int) ((0.4 * files.length) + 1);
			Arrays.sort(files, new FileLastModifSort());
			for (int i = 0; i < removeFactor; i++) {
				if (files[i].getName().contains(WHOLESALE_CONV)) {
					files[i].delete();
				}
			}
		}

		if (freeSpaceOnSd() <= CACHE_SIZE) {
			return false;
		}

		return true;
	}

	/** 修改文件的最后修改时间 **/
	public void updateFileTime(String path) {
		File file = new File(path);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	/**
	 * 计算sdcard上的剩余空间
	 * 
	 * @return
	 */
	private int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	/**
	 * 将url转成文件名
	 * 
	 * @param url
	 * @return
	 */
	private String convertUrlToFileName(String url) {

		byte[] hash = null;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					url.getBytes("UTF-8"));
		} catch (Exception e) {
			Log.e(TAG, "MD5 Error", e);
		}

		if (hash == null) {
			return url;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) {
				hex.append("0");
			}
			hex.append(Integer.toHexString(b & 0xFF));
		}

		return new StringBuilder(hex.toString()).append(WHOLESALE_CONV)
				.toString();

	}

	/**
	 * 获得缓存目录
	 * 
	 * @return
	 */
	private String getDirectory() {
		String dir = getSDPath() + "/" + sdPath;
		return dir;
	}

	/**
	 * 取SD卡路径
	 * 
	 * @return
	 */
	private String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory(); // 获取根目录
		}
		if (sdDir != null) {
			return sdDir.toString();
		} else {
			return "";
		}
	}

	/**
	 * 根据文件的最后修改时间进行排序
	 */
	private class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

}
