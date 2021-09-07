package com.pengxh.app.multilib.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pengxh.app.multilib.R;

public class KeyBoardView extends LinearLayout implements View.OnClickListener {

    private KeyboardClickListener listener = null;

    public KeyBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_keyboard, this);
        setChildViewOnclick(this);
    }

    /**
     * 设置键盘子View的点击事件
     */
    private void setChildViewOnclick(ViewGroup parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            if (view instanceof ViewGroup) {
                setChildViewOnclick((ViewGroup) view);
                continue;
            }
            view.setOnClickListener(this);
        }
    }

    public boolean dispatchKeyEventInFullScreen(KeyEvent event) {
        if (event == null) {
            return false;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (isShown()) {
                this.setVisibility(View.GONE);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            // 如果点击的是TextView
            String value = ((TextView) v).getText().toString();
            if (!TextUtils.isEmpty(value)) {
                if (listener != null) {
                    listener.onClick(value);
                }
            }
        } else if (v instanceof ImageView) {
            // 如果是图片那肯定点击的是删除
            if (listener != null) {
                listener.onDelete();
            }
        }
    }

    public void setKeyboardClickListener(KeyboardClickListener keyboardClickListener) {
        this.listener = keyboardClickListener;
    }

    public interface KeyboardClickListener {
        void onClick(String value);

        void onDelete();
    }
}
