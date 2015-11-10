package com.surong.leadloan.start;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.surong.leadloan.R;
import com.surong.leadloan.ui.ViewPagerAdapter;

/**
*  引导界面
*
*
*/
public class GuideActivity extends Activity implements OnPageChangeListener {

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private Button button;
    private View view;
    // 底部小点图片
    private ImageView[] dots;
    // 记录当前选中位置
    private int currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dots);
        // 初始化页面
        initViews(savedInstanceState);
        // 初始化底部小点
        initDots();
    }
    private void initViews(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(this);
        LocalActivityManager manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
        views = new ArrayList<View>();
        // 初始化引导图片列表
        Intent intent1 = new Intent(this, WelcomeActivity.class);
        views.add(inflater.inflate(R.layout.welcome1, null));
        views.add(inflater.inflate(R.layout.welcome2, null));
        views.add(inflater.inflate(R.layout.welcome3, null));
        views.add(manager.startActivity("a", intent1).getDecorView());
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views, this);
        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
    }
    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[views.size()];
        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
        }
        dots[0].setImageResource(R.drawable.circle);
    }
    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
    	for (int i = 0; i < dots.length; i++) {
    		if (i == arg0) {
    			dots[i].setImageResource(R.drawable.circle);
			}
    		else{
    			dots[i].setImageResource(R.drawable.circlenor);
    		}
		}
    }
    
}