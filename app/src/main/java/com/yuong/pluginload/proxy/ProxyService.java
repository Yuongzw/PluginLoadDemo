package com.yuong.pluginload.proxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.yuong.pluginload.PluginManager;
import com.yuong.transfer.IServiceInterface;

import java.lang.reflect.Constructor;

public class ProxyService extends Service {

    public static String pluginServiceClassName = "";
    private IServiceInterface pluginService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPlugin();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (pluginService != null) {
           pluginService.onStartCommand(intent, flags, startId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initPlugin() {
        try {
            Class<?> pluginServiceClazz = PluginManager.getInstance(this).getClassLoader().loadClass(pluginServiceClassName);
            Constructor<?> constructor = pluginServiceClazz.getConstructor(new Class[]{});
            //获取某个service对象
            Object newInstance = constructor.newInstance(new Object[]{});
            pluginService = (IServiceInterface) newInstance;
            pluginService.insertAppService(this);
            pluginService.onCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (pluginService != null) {
            pluginService.onUnbind(intent);
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if (pluginService != null) {
            pluginService.onDestroy();
        }
        super.onDestroy();
    }
}
