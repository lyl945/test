package com.easemob.chatuidemo.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.easemob.chatuidemo.R;

public class MenuDialog extends Dialog {
    private LayoutInflater inflater;
    private TextView title;
    private ViewGroup body;
    private MenuDialogItemClickListener listener;

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (listener != null) {
                if (listener.onItemClick(v.getId(), (TextView) v)) {
                    dismiss();
                }
            } else {
                dismiss();
            }
        }
    };

    public MenuDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public MenuDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_menu);

        int m = getContext().getResources().getDimensionPixelOffset(
                R.dimen.dimen_16);
        int w = getContext().getResources().getDisplayMetrics().widthPixels - m
                * 2;
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = w;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setBackgroundDrawableResource(R.drawable.transparent);
        // TODO Auto-generated method stub
        inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        title = (TextView) findViewById(R.id.dlg_title_label);
        body = (ViewGroup) findViewById(R.id.dlg_item_container);
    }

    public String getTitle() {
        return title.getText().toString();
    }

    @Override
    public void setTitle(CharSequence sequence) {
        this.title.setText(sequence);
    }

    public void addItem(String text, int id) {
        addItem(text, id, null);
    }

    @SuppressWarnings("deprecation")
    public void addItem(String text, int id, Object tag) {

        // ������˲˵�,�Ȼ�һ������
        if (body.getChildCount() > 0) {
            inflater.inflate(R.layout.gray_line, body, true);
        }
        TextView v = (TextView) inflater.inflate(R.layout.dlg_menu_item, null);
        v.setId(id);
        v.setText(text);

        if (body.getChildCount() > 2) {
            // ��֮ǰ����һ����Ϊ��ͨ��,�ǵ�Ҫ������������
            body.getChildAt(body.getChildCount() - 2).setBackgroundResource(
                    R.drawable.clickable_mid_selector);
        }
        // ����ǵ�һ��
        if (body.getChildCount() < 1) {
            v.setBackgroundResource(R.drawable.clickable_top_selector);
        } else {
            v.setBackgroundResource(R.drawable.clickable_bottom_selector);
        }
        v.setClickable(true);
        v.setFocusable(true);
        v.setOnClickListener(clickListener);
        v.setTag(tag);

        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
                (int) getContext().getResources().getDimension(
                        R.dimen.plain_text_h));
        body.addView(v, params);
    }

    public void setOnItemClickListener(MenuDialogItemClickListener listener) {
        this.listener = listener;
    }

    public interface MenuDialogItemClickListener {
        /**
         * ����true��رնԻ���,���򲻹ر� PENGJU 2012-8-16 ����1:45:26
         * 
         * @param id
         * @param item
         * @return
         */
        public boolean onItemClick(int id, TextView item);
    }

    public void clearItems() {
        body.removeAllViews();
    }
}
