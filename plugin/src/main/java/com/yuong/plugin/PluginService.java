package com.yuong.plugin;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.yuong.plugin.base.BaseService;

public class PluginService extends BaseService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("PluginService", "PluginService onCreate");
        Toast.makeText(appService, "PluginService onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(appService, "PluginService onStartCommand", Toast.LENGTH_SHORT).show();
        Log.d("PluginService", "PluginService onStartCommand");
        //开启耗时任务
        new Thread(new Runnable() {
            int count = 0;
            @Override
            public void run() {
                while (count < 10) {
                    Log.d("PluginService", "插件服务正在执行耗时任务");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
                appService.stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(appService, "PluginService onDestroy", Toast.LENGTH_SHORT).show();
        Log.d("PluginService", "PluginService onDestroy");
    }
}
