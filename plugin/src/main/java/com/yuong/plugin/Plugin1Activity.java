package com.yuong.plugin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yuong.plugin.base.BaseActivity;

public class Plugin1Activity extends BaseActivity {
    private static final String TAG = Plugin1Activity.class.getSimpleName();
    private boolean isRegister;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreateActivity");
        setContentView(R.layout.activity_plugin1);
        TextView textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.btn_jump);
        String value = savedInstanceState.getString("value");
        textView.setText(value != null ? value : "");
        Toast.makeText(appActivity, value, Toast.LENGTH_SHORT).show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appActivity, Plugin2Activity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_start_plugin_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(appActivity, PluginService.class));
            }
        });
        findViewById(R.id.btn_register_plugin_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRegister) {
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("yuongzw");
                    registerReceiver(new PluginReceiver(), filter);
                    isRegister = true;
                }
            }
        });
        findViewById(R.id.btn_send_plugin_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("yuongzw");
                intent.putExtra("str", "我是从插件发送的广播");
                sendBroadcast(intent);
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStartActivity");
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {
        Log.d(TAG, "onResumeActivity");
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {
        Log.d(TAG, "onPauseActivity");
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {
        Log.d(TAG, "onStopActivity");
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRestart() {
        Log.d(TAG, "onRestartActivity");
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroyActivity");
    }
}
