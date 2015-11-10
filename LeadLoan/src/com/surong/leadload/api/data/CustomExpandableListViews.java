package com.surong.leadload.api.data;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ExpandableListView;

public class CustomExpandableListViews extends ExpandableListView {  
    
    public CustomExpandableListViews(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
    }  
  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        // TODO Auto-generated method stub  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
  
        MeasureSpec.AT_MOST);  
  
        super.onMeasure(widthMeasureSpec, expandSpec);
    }  
}  
