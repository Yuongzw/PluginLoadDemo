package com.yuong.pluginload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.yuong.pluginload.proxy.ProxyActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "plugin.apk";
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 666) {
                isLoadSuccess = true;
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(MainActivity.this, "加载插件成功！", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(MainActivity.this, "加载插件失败，请检查插件是否存在！", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //是否加载完成
    private boolean isLoadSuccess = false;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void loadPlugin(View view) {
        //判断是否已经赋予权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, 666);
            } else {
                showProgress();
                PluginManager.getInstance(this).loadPlugin(handler, path);
            }
        } else {
            showProgress();
            PluginManager.getInstance(this).loadPlugin(handler, path);
        }

    }


    public void jumpPluginActivity(View view) {
        if (isLoadSuccess) {
            //获取插件包的Activity
            PackageManager packageManager = getPackageManager();
            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            //获取在manifest文件中注册的第一个activity
            ActivityInfo activity = packageArchiveInfo.activities[0];
            Intent intent = new Intent(this, ProxyActivity.class);
            intent.putExtra("className", activity.name);
            startActivity(intent);
        } else {
            Toast.makeText(this, "请先加载插件！", Toast.LENGTH_SHORT).show();
        }

    }

    private void showProgress() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.setTitle("加载插件中，请稍后。。。");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 666) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showProgress();
                PluginManager.getInstance(this).loadPlugin(handler, path);
            } else {
                Toast.makeText(MainActivity.this, "请打开读取SD卡权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void parsePlugin(View view) {
        PluginManager.getInstance(this).parsePlugin(path);
    }

    public void sendStaticReceiver(View view) {
        Intent intent = new Intent("plugin_static_receiver");
        intent.putExtra("str", "我是从宿主中发来的字符");
        sendBroadcast(intent);
    }
}
