package com.pengxh.app.multilib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.pengxh.app.multilib.R;
import com.pengxh.app.multilib.utils.SizeUtil;

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2021-09-05 19:25:42
 * @description: 普通提示对话框对话框
 */
public class AlertMessageDialog extends Dialog {

    private Context ctx;
    private String title;
    private String message;
    private String positiveBtn;
    private OnDialogButtonClickListener listener;

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveBtn;
        private OnDialogButtonClickListener listener;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveButton(String name) {
            this.positiveBtn = name;
            return this;
        }

        public Builder setOnDialogButtonClickListener(OnDialogButtonClickListener listener) {
            this.listener = listener;
            return this;
        }

        public AlertMessageDialog build() {
            return new AlertMessageDialog(this);
        }
    }

    private AlertMessageDialog(Builder builder) {
        super(builder.context, R.style.DialogStyle);
        this.ctx = builder.context;
        this.title = builder.title;
        this.message = builder.message;
        this.positiveBtn = builder.positiveBtn;
        this.listener = builder.listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configDialogLayout();
        setContentView(R.layout.dialog_message);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        TextView dialogTitleView = findViewById(R.id.dialogTitleView);
        TextView dialogMessageView = findViewById(R.id.dialogMessageView);
        Button dialogConfirmButton = findViewById(R.id.dialogConfirmButton);

        if (!TextUtils.isEmpty(title)) {
            dialogTitleView.setText(title);
        }

        if (!TextUtils.isEmpty(message)) {
            dialogMessageView.setText(message);
        }

        if (!TextUtils.isEmpty(positiveBtn)) {
            dialogConfirmButton.setText(positiveBtn);
            dialogConfirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onConfirmClick();
                        dismiss();
                    }
                }
            });
        }
    }

    private void configDialogLayout() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (SizeUtil.getScreenWidth(ctx) * 0.8);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    public interface OnDialogButtonClickListener {
        void onConfirmClick();
    }
}
