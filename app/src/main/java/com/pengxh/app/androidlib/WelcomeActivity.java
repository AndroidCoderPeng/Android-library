package com.pengxh.app.androidlib;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/2/18 21:51
 */
public class WelcomeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int PERMISSIONS_CODE = 999;
    private static final String[] USER_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否有权限，如果版本大于5.1才需要判断（即6.0以上），其他则不需要判断。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (EasyPermissions.hasPermissions(this, USER_PERMISSIONS)) {
                startMainActivity();
            } else {
                EasyPermissions.requestPermissions(WelcomeActivity.this, "", PERMISSIONS_CODE, USER_PERMISSIONS);
            }
        } else {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        startMainActivity();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
