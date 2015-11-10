package com.surong.leadload.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

public class ecodeingView extends LinearLayout{
	
	EditText qr_text;
	String TAG="..";
	int QR_WIDTH=200,QR_HEIGHT=200;
	ImageView qr_image;
	Button bt;
	Bitmap bitmap;
	TextView qr_result;
	public ecodeingView(Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		qr_image=new ImageView(context);
		qr_text=new EditText(context);
		qr_result=new TextView(context);
		bt=new Button(context);
		bt.setText("anniu");
		bt.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				createImage();
				
			}
		});
		
		addView(qr_text);
		addView(qr_image);
		addView(bt);
		addView(qr_result);
		
	}
	// ���QRͼ
    private void createImage() {
        try {
            // ��Ҫ����core��
            QRCodeWriter writer = new QRCodeWriter();

            String text = qr_text.getText().toString();

            Log.i(TAG, "��ɵ��ı���" + text);
            if (text == null || "".equals(text) || text.length() < 1) {
                return;
            }

            // ��������ı�תΪ��ά��
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);

            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }

                }
            }

             bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            
            System.out.println(Environment.getExternalStorageDirectory());

            
            qr_image.setImageBitmap(bitmap);
            try {
				saveMyBitmap(bitmap, "code");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
 // ����QRͼƬ
    private void scanningImage() {
    	  
 
    	
    	Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

        // ��ô������ͼƬ
        Bitmap bitmap = ((BitmapDrawable) qr_image.getDrawable()).getBitmap();
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result;
        try {
        	
            result = reader.decode(bitmap1);
            result=reader.decode(bitmap1, hints );
            // �õ������������
            qr_result.setText(result.getText());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }
    public void saveMyBitmap(Bitmap bm,String bitName) throws IOException {
        File f = new File("/mnt/sdcard/" + bitName + ".png");
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
                fOut.flush();
        } catch (IOException e) {
                e.printStackTrace();
        }
        try {
                fOut.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
}
}
