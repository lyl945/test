package com.surong.leadloan.utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

public class BitmapUtil {

	/**
	 * 将bitmap转换成base64字符串
	 * 
	 * @param bitmap
	 * @return base64 字符串 100为
	 */

	public static String bitmaptoString(Bitmap bitmap, int bitmapQuality) {
		// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, bitmapQuality, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

	/**
	 * 将base64转换成bitmap图片
	 * 
	 * @param string
	 * @return bitmap
	 */

	public static Bitmap stringtoBitmap(String string) {

		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;

	}

	// 把bit转换成流
	public static String createFile(Bitmap photo) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] bs = stream.toByteArray(); // 将图片流以字符串形式存储下来
		FileOutputStream out = null;
		String picPath = null;
		try {
			picPath = Constans.IAMGES_PATH
					+ "/"
					+ (new SimpleDateFormat("yyyyMMddHHmmss"))
							.format(new Date()) + ".png";
			File fi = new File(Constans.IAMGES_PATH);
			if (!fi.exists()) {
				fi.mkdirs();
			}
			out = new FileOutputStream(picPath);
			out.write(bs);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return picPath;
	}

	// 设置图片的大小
	public static Bitmap setBitmap(Bitmap bitMap, int width1, int heght2) {
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		// 设置想要的大小
		int newWidth = width1;
		int newHeight = heght2;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
		return bitMap;
	}

}
