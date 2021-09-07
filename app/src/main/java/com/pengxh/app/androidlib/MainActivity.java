package com.pengxh.app.androidlib;

import android.util.Log;

import com.pengxh.app.multilib.base.BaseNormalActivity;
import com.pengxh.app.multilib.widget.KeyBoardView;
import com.pengxh.app.multilib.widget.dialog.AlertControlDialog;
import com.pengxh.app.multilib.widget.dialog.AlertMessageDialog;
import com.pengxh.app.multilib.widget.dialog.BottomActionSheet;
import com.pengxh.app.multilib.widget.dialog.InputDialog;

import butterknife.BindView;

public class MainActivity extends BaseNormalActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.keyBoardView)
    KeyBoardView keyBoardView;


    @Override
    public int initLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        keyBoardView.setKeyboardClickListener(new KeyBoardView.KeyboardClickListener() {
            @Override
            public void onClick(String value) {
                switch (value) {
                    case "3":
                        new InputDialog.Builder()
                                .setContext(MainActivity.this)
                                .setTitle("提示")
                                .setHintMessage("这里是默认内容")
                                .setNegativeButton("取消")
                                .setPositiveButton("确定")
                                .setOnDialogButtonClickListener(new InputDialog.OnDialogButtonClickListener() {
                                    @Override
                                    public void onConfirmClick(String value) {
                                        Log.d(TAG, "onConfirmClick: " + value);
                                    }

                                    @Override
                                    public void onCancelClick() {

                                    }
                                }).build().show();
                        break;
                    case "6":
                        new AlertMessageDialog.Builder()
                                .setContext(MainActivity.this)
                                .setTitle("提示")
                                .setMessage("这里是默认内容")
                                .setPositiveButton("知道了")
                                .setOnDialogButtonClickListener(new AlertMessageDialog.OnDialogButtonClickListener() {
                                    @Override
                                    public void onConfirmClick() {

                                    }
                                }).build().show();
                        break;
                    case "9":
                        new AlertControlDialog.Builder()
                                .setContext(MainActivity.this)
                                .setTitle("提示")
                                .setMessage("这里是默认内容")
                                .setNegativeButton("取消")
                                .setPositiveButton("确定")
                                .setOnDialogButtonClickListener(new AlertControlDialog.OnDialogButtonClickListener() {
                                    @Override
                                    public void onConfirmClick() {

                                    }

                                    @Override
                                    public void onCancelClick() {

                                    }
                                }).build().show();
                        break;
                }
            }

            @Override
            public void onDelete() {
                new BottomActionSheet.Builder()
                        .setContext(MainActivity.this)
                        .setActionItemTitles(new String[]{"这里是默认内容", "这里是默认内容", "这里是默认内容"})
                        .setOnActionSheetListener(new BottomActionSheet.OnActionSheetListener() {
                            @Override
                            public void onActionItemClick(int position) {
                                Log.d(TAG, "onActionItemClick: " + position);
                            }
                        }).build().show();
            }
        });
    }
}