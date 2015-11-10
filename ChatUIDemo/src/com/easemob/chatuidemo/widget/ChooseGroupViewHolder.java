package com.easemob.chatuidemo.widget;

import android.view.View;
import android.view.View.OnClickListener;

import com.easemob.chatuidemo.R;
import com.pj.core.BaseActivity;
import com.pj.core.viewholders.ViewHolder;

public class ChooseGroupViewHolder extends ViewHolder implements
        View.OnClickListener {
    private OnClickListener listener;

    public ChooseGroupViewHolder(BaseActivity activity) {
        super(activity);
        // TODO Auto-generated constructor stub
        setLayoutResource(R.layout.groups);
    }

    @Override
    protected void onApplyView(View arg0) {
        // TODO Auto-generated method stub
        assignClickListener(this, R.id.group_friend, R.id.group_workmate);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public OnClickListener getListener() {
        return listener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (listener != null) {
            listener.onClick(v);

            dismissBottomMenu();
        }
    }

}
