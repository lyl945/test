package com.surong.leadloan.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressBars extends View {
	private int max;
	private Paint paint;
	private RectF oval;
	private float radius;
	private PointF mCenter = new PointF();
	private float progress = 0.0f;

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public float getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		invalidate();
	}

	public CircleProgressBars(Context context, float progress) {
		super(context);
		this.progress = progress;
	}

	public CircleProgressBars(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		oval = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		int minSide = Math.min(width, height);
		radius = minSide / 2.0f - 10 / 2.0f;

		float left = (width - radius * 2) / 2.0f;
		float top = (height - radius * 2) / 2.0f;
		mCenter.x = left + radius;
		mCenter.y = top + radius;

		paint.setAntiAlias(true);// 设置是否抗锯齿
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);// 帮助消除锯齿
		paint.setColor(Color.GRAY);// 设置画笔灰色
		paint.setStrokeWidth(10);// 设置画笔宽度
		paint.setStyle(Paint.Style.STROKE);// 设置中空的样式
		canvas.drawCircle(mCenter.x, mCenter.y, 80, paint);// 在中心为（100,100）的地方画个半径为55的圆，宽度为setStrokeWidth：10，也就是灰色的底边
		paint.setColor(Color.GREEN);// 设置画笔为绿色
		oval.set(mCenter.x - 80, mCenter.y - 80, mCenter.x + 80, mCenter.y + 80);// 设置类似于左上角坐标（45,45），右下角坐标（155,155），这样也就保证了半径为55
		canvas.drawArc(oval, -90, ((float) progress / max) * 360, false, paint);// 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心
		paint.reset();// 将画笔重置
		paint.setAntiAlias(true);// 设置是否抗锯齿
		paint.setStrokeWidth(2);// 再次设置画笔的宽度
		paint.setTextSize(35);// 设置文字的大小
		paint.setColor(Color.BLACK);// 设置画笔颜色
		if (progress == max) {
			paint.setTextSize(75);// 设置文字的大小
			paint.setColor(Color.GREEN);// 设置画笔颜色
			canvas.drawText("√", mCenter.x - 20, mCenter.y + 20, paint);
		} else {
			canvas.drawText(progress + "%", mCenter.x - 30, mCenter.y + 20,
					paint);
		}

	}
}