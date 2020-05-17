package com.yuong.plugin.base;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.yuong.transfer.IServiceInterface;

public class BaseService extends Service implements IServiceInterface {
    protected Service appService;
    @Override
    public void insertAppService(Service appService) {
        this.appService = appService;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void onDestroy() {
    }

}
