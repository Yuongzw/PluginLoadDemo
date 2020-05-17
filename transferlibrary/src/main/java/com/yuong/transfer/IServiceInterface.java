package com.yuong.transfer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public interface IServiceInterface {
    /**
     * 插入宿主的Service
     *
     * @param appService
     */
    void insertAppService(Service appService);

    public IBinder onBind(Intent intent);

    public void onCreate();

    public int onStartCommand(Intent intent, int flags, int startId);

    public boolean onUnbind(Intent intent);

    public void onDestroy();
}
