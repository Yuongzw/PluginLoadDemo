package com.yuong.pluginload.proxy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.yuong.pluginload.PluginManager;
import com.yuong.transfer.IActivityInterface;

import java.lang.reflect.Constructor;

/**
 * 代理的Activity
 */
public class ProxyActivity extends Activity {

    private IActivityInterface pluginActivity1;
    private boolean isRegister;
    private ProxyReceiver proxyReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //真正的加载插件里面的Activity
        String className = getIntent().getStringExtra("className");
        try {
            Class<?> pluginActivity1Clazz = getClassLoader().loadClass(className);
            Constructor<?> constructor = pluginActivity1Clazz.getConstructor(new Class[]{});
            pluginActivity1 = (IActivityInterface) constructor.newInstance(new Object[]{});
            pluginActivity1.insertAppContext(this);
            Bundle bundle = new Bundle();
            bundle.putString("value", "我是宿主传递过来的字符串");
            pluginActivity1.onCreate(bundle);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        pluginActivity1.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        pluginActivity1.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pluginActivity1.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pluginActivity1.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pluginActivity1.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pluginActivity1.onDestroy();
        if (isRegister) {
            unregisterReceiver(proxyReceiver);
        }
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance(this).getResource();
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance(this).getClassLoader();
    }

    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        //自己跳自己
        Intent newIntent = new Intent(this, this.getClass());
        newIntent.putExtra("className", className);
        super.startActivity(newIntent);
    }

    @Override
    public ComponentName startService(Intent service) {
        String className = service.getStringExtra("className");
        ProxyService.pluginServiceClassName = className;
        //自己跳自己
        Intent newService = new Intent(this, ProxyService.class);
        newService.putExtra("className", className);
        return super.startService(newService);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        proxyReceiver = new ProxyReceiver(receiver.getClass().getName());
        isRegister = true;
        return super.registerReceiver(proxyReceiver, filter);
    }

}
