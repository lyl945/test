package com.surong.leadloan.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewParent;

/**
 * @author �޽�
 */
public class SwitchButton extends View implements OnTouchListener,
		OnClickListener {

	// ���ؿ���ʱ�ı������ر�ʱ�ı�����������ť
	private Bitmap switch_on_Bkg, switch_off_Bkg, slip_Btn;
	private Rect on_Rect, off_Rect;

	// �Ƿ����ڻ���
	private boolean isSlipping = false;
	// ��ǰ����״̬��trueΪ������falseΪ�ر�
	private boolean isSwitchOn = false;

	// ��ָ����ʱ��ˮƽ���X����ǰ��ˮƽ���X
	private float previousX, currentX,flagX;

	// ���ؼ�����
	private OnSwitchListener onSwitchListener;
	// �Ƿ������˿��ؼ�����
	private boolean isSwitchListenerOn = false;

	public SwitchButton(Context context) {
		super(context);
		init();
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setOnTouchListener(this);
		setOnClickListener(this);
	}

	public void setImageResource(int switchOnBkg, int switchOffBkg, int slipBtn) {
		switch_on_Bkg = BitmapFactory.decodeResource(getResources(),
				switchOnBkg);
		switch_off_Bkg = BitmapFactory.decodeResource(getResources(),
				switchOffBkg);
		slip_Btn = BitmapFactory.decodeResource(getResources(), slipBtn);

		// �Ұ��Rect����������ť���Ұ��ʱ��ʾ���ؿ���
		on_Rect = new Rect(switch_off_Bkg.getWidth() - slip_Btn.getWidth(), 0,
				switch_off_Bkg.getWidth(), slip_Btn.getHeight());
		// ����Rect����������ť������ʱ��ʾ���عر�
		off_Rect = new Rect(0, 0, slip_Btn.getWidth(), slip_Btn.getHeight());
	}

	public void setSwitchState(boolean switchState) {
		isSwitchOn = switchState;
	}

	protected boolean getSwitchState() {
		return isSwitchOn;
	}

	protected void updateSwitchState(boolean switchState) {
		isSwitchOn = switchState;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		// ������ť��������
		float left_SlipBtn;

		// ��ָ���������ߵ�ʱ���ʾ����Ϊ�ر�״̬���������Ұ�ߵ�ʱ���ʾ����Ϊ����״̬
		if (currentX < (switch_on_Bkg.getWidth() / 2)) {
			canvas.drawBitmap(switch_off_Bkg, matrix, paint);
		} else {
			canvas.drawBitmap(switch_on_Bkg, matrix, paint);
		}

		// �жϵ�ǰ�Ƿ����ڻ���
		if (isSlipping) {
			if (currentX > switch_on_Bkg.getWidth()) {
				left_SlipBtn = switch_on_Bkg.getWidth() - slip_Btn.getWidth();
			} else {
				left_SlipBtn = currentX - slip_Btn.getWidth() / 2;
			}
		} else {
			// ��ݵ�ǰ�Ŀ���״̬���û�����ť��λ��
			if (isSwitchOn) {
				left_SlipBtn = on_Rect.left;
			} else {
				left_SlipBtn = off_Rect.left;
			}
		}

		// �Ի�����ť��λ�ý����쳣�ж�
		if (left_SlipBtn < 0) {
			left_SlipBtn = 0;
		} else if (left_SlipBtn > switch_on_Bkg.getWidth()
				- slip_Btn.getWidth()) {
			left_SlipBtn = switch_on_Bkg.getWidth() - slip_Btn.getWidth();
		}

		// Bitmap��ͼƬ����left:ƫ����ߵ�λ�ã�top�� ƫ�ƶ�����λ��
		// drawBitmap(Bitmap bitmap, float left, float top, Paint paint)
		canvas.drawBitmap(slip_Btn, left_SlipBtn+2, 0, paint);
		/*
		 * 2����ͼƬ���Ӻ��޶���ʾ����
		 * 	
		 * drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint)�� Rect
		 * src: �Ƕ�ͼƬ���вýأ����ǿ�null����ʾ���ͼƬ RectF dst����ͼƬ��Canvas��������ʾ������
		 * ����src���src�Ĳý���Ŵ� С��src���src�Ĳý�����С��
		 */
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(switch_on_Bkg.getWidth(),
				switch_on_Bkg.getHeight());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		// ����
		case MotionEvent.ACTION_MOVE:
			currentX = event.getX();
			System.out.println("move11111111111");
			break;

		// ����
		case MotionEvent.ACTION_DOWN:
			flagX = event.getX();
			System.out.println("ACTION_DOWN11111111111");
			attemptClaimDrag();
			if (event.getX() > switch_on_Bkg.getWidth()
					|| event.getY() > switch_on_Bkg.getHeight()) {
				return false;
			}

			isSlipping = true;
			previousX = event.getX();
			currentX = previousX;
			break;

		// �ɿ�
		case MotionEvent.ACTION_UP:
			
			System.out.println("ACTION_UP11111111111");
			isSlipping = false;
			// �ɿ�ǰ���ص�״̬
			boolean previousSwitchState = isSwitchOn;

			if (event.getX() >= (switch_on_Bkg.getWidth() / 2)) {
				isSwitchOn = true;
			} else {
				isSwitchOn = false;
			}

			// ��������˼�����������ô˷���
			if (isSwitchListenerOn && (previousSwitchState != isSwitchOn)) {
				onSwitchListener.onSwitched(this,isSwitchOn);
			}
			break;

		default:
			break;
		}

		// ���»��ƿؼ�
		invalidate();
		return true;
	}

	ViewParent mparent;

	/**
	 * Tries to claim the user's drag motion, and requests disallowing any
	 * ancestors from stealing events in the drag.
	 */
	private void attemptClaimDrag() {
		mparent = getParent();
		if (mparent != null) {
			mparent.requestDisallowInterceptTouchEvent(true);
		}
	}

	public void setOnSwitchListener(OnSwitchListener listener) {
		onSwitchListener = listener;
		isSwitchListenerOn = true;
	}

	public interface OnSwitchListener {
		abstract void onSwitched(View view,boolean isSwitchOn);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		boolean previousSwitchState = isSwitchOn;

		if (isSwitchOn) {
			isSwitchOn = false;
		} else {
			isSwitchOn = true;
		}
		// ���»��ƿؼ�
		invalidate();
		// ��������˼�����������ô˷���
		if (isSwitchListenerOn && (previousSwitchState != isSwitchOn)) {
			onSwitchListener.onSwitched(this,isSwitchOn);
		}
	}
	public void setChange(int flag){
		if(flag==1){
			isSwitchOn = true;
			currentX = (switch_on_Bkg.getWidth() / 2);
		}else{
			isSwitchOn = false;
			currentX = 0;
		}
		
		invalidate();
	}
}
