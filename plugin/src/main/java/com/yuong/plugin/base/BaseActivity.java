package com.yuong.plugin.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.yuong.transfer.IActivityInterface;

public abstract class BaseActivity extends Activity implements IActivityInterface {
    protected Activity appActivity;
    @Override
    public void insertAppContext(Activity appActivity) {
        this.appActivity = appActivity;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRestart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }

    @Override
    public void setContentView(int resId) {
        appActivity.setContentView(resId);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return appActivity.findViewById(id);
    }

    @Override
    public void startActivity(Intent intent) {
        Intent newIntent = new Intent();
        newIntent.putExtra("className", intent.getComponent().getClassName());
        appActivity.startActivity(newIntent);
    }

    @Override
    public ComponentName startService(Intent service) {
        Intent newService = new Intent();
        newService.putExtra("className", service.getComponent().getClassName());
        return appActivity.startService(newService);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return appActivity.registerReceiver(receiver, filter);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        appActivity.sendBroadcast(intent);
    }
}
