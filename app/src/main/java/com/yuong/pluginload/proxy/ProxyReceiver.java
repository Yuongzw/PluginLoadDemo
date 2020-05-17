package com.yuong.pluginload.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yuong.pluginload.PluginManager;
import com.yuong.transfer.IReceiverInterface;

public class ProxyReceiver extends BroadcastReceiver {
    private String pluginReceiverClassName;

    public ProxyReceiver(String pluginReceiverClassName) {
        this.pluginReceiverClassName = pluginReceiverClassName;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("yuongzw".equals(intent.getAction())) {
            try {
                Class<?> pluginReceiverClass = PluginManager.getInstance(context).getClassLoader().loadClass(pluginReceiverClassName);
                IReceiverInterface pluginReceiver = (IReceiverInterface) pluginReceiverClass.newInstance();
                pluginReceiver.onReceive(context, intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
