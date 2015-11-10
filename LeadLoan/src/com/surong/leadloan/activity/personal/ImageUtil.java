package com.surong.leadloan.activity.personal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;

/**
 * ͼƬ���ع�����
	// ��һ�μ���ͼƬ��     ����������ͼƬ-->���ش�ͼ-->����Сͼ-->���ػ���Сͼ-->�ڴ滺��-->����
    // �Ժ� �����ж� �ڴ�Сͼ���ڴ滺�棩, û�� �� �ж�  ����Сͼ ���ڣ� ����Сͼ���ڣ��ͼ��أ����浽 �ڴ棬�ͼ���
    // ��  ����Сͼ �����ڣ��ͼ��� ��ͼ-->Сͼ-->���ػ���Сͼ-->�ڴ滺��-->����
 * @author Administrator
 */
public class ImageUtil {
	
	private Activity activity;
	// HashMap�����Ԫ�� ռ���ڴ�Ļ��������Զ����յ�
	// �����û��棬���ǵ����������������ڴ����˵�ʱ�� ���Զ�����
	private static HashMap<String, SoftReference<Bitmap>> imagesCache = new HashMap<String, SoftReference<Bitmap>>();
	
	public ImageUtil(Activity activity) {
		this.activity = activity;
	}
	
	/**
	 * 1 ���ػ���ͼƬ
	 * @param imagePath
	 * @return
	 */
	public Bitmap loadCacheImage(String imagePath) {
		Bitmap bitmap = null;
		if(imagesCache.containsKey(imagePath)) {
			SoftReference<Bitmap> softReference =  imagesCache.get(imagePath);
			bitmap = softReference.get();
			if(null != bitmap) {
				return bitmap;
			}
		} 
		bitmap = loadImage(imagePath);
		imagesCache.put(imagePath, new SoftReference<Bitmap>(bitmap));
		return bitmap;
	}
	
	
	/**
	 * 2 �ӱ��ػ����ȡͼƬ��Դ
	 * @param imagePath
	 * @return
	 */
	public Bitmap loadImage(String imagePath) {
		Bitmap bitmap = null;
		// ��ȡ����ͼƬ�����
		String cacheImagePath = getCacheImagePath(imagePath);
		File cacheFile = new File(cacheImagePath);
		
		// ��  ����Сͼ �����ڣ��ͼ��ش�ͼ�� Сͼ-->��ɱ���Сͼ
		if(!cacheFile.exists()) {
			// ��ȡ��ͼ�� Сͼ
			bitmap = loadBigImage(imagePath);
			// �� Сͼ ���浽����SD��
			saveFileByBitmap(bitmap, cacheImagePath);
		}
		// �ж�  ����Сͼ �Ƿ���ڣ� ����Сͼ���ڣ���ֱ�Ӽ���
		else {
			try {
				bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	
	/**
	 * �� �ڴ�Сͼ ���浽����SD��
	 * @param bitmap
	 * @param cacheImagePath
	 */
	private void saveFileByBitmap(Bitmap bitmap, String newImagePath) {
		File file = new File(newImagePath);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
				fileOutputStream.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ȡ����ͼƬ��·��
	 * @param imagePath
	 * @return
	 */
	private String getCacheImagePath(String imagePath) {
		// Ѱ�һ����ļ���
		String cacheDir = new File(imagePath).getParentFile().getAbsolutePath() + "/cache/";
		// �����ļ��в����ڣ��ʹ���һ�������ļ���
		if(!new File(cacheDir).exists()) {
			new File(cacheDir).mkdirs();
		}
		String fileName = new File(imagePath).getName();
		String newImagePath = cacheDir + fileName + ".cache";
		System.out.println("����ͼƬ·����" + newImagePath);
		return newImagePath;
	}

	/**
	 * ����ԭʼͼƬ �� ��Ļ��С��Сͼ
	 * @param imagePath
	 */
	public Bitmap loadBigImage(String imagePath) {
		Bitmap bitmap = null;
		
//		// ��ȡ��Ļ�Ŀ��
//    	DisplayMetrics displayMetrics = new DisplayMetrics();
//    	getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//    	int screenWidth = displayMetrics.widthPixels;
		
		Display display = activity.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		System.out.println("width: " + width + "  height: " + height );
		
		// Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // ����Ϊtrue, �Ժ����ͼƬ��ʱ�� ֻ�ܶ�ȡ��ͼƬ�� ��Ⱥ͸߶ȣ� �����ͼƬ����Ϊ��
		// ����options.inJustDecodeBoundsΪtrue֮�� ���ص�bitmap��null�ģ� options�������� ��͸�
		BitmapFactory.decodeFile(imagePath, options);
		
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;
		System.out.println("imageWidth: " + imageWidth + "  imageWidth: " + imageWidth );
		
		// ������ �͸߶ȵı���
		int scaleX =(int)Math.ceil((imageWidth/(float)width));
		int scaleY =(int)Math.ceil((imageHeight/(float)height));
		if(scaleX > 1 && scaleY > 1) {
			options.inSampleSize = (scaleX > scaleY) ? scaleX : scaleY;
		}
		
		// Ϊ�˽�ʡ�ڴ棬��������һЩ�ֶ�
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;	// ������ɫ������
		options.inPurgeable = true;
		options.inInputShareable = true;
		
		// ֻ������Ϊfalse,��ȡ����Bitmap�Ų���Ϊ��
		options.inJustDecodeBounds = false;
		try {
			bitmap = BitmapFactory.decodeStream(new FileInputStream(imagePath), null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}



}
