package com.yuong.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PluginStaticReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("plugin_static_receiver".equals(intent.getAction())) {
            Toast.makeText(context, "我是插件的静态广播，我收到了：" + intent.getStringExtra("str"), Toast.LENGTH_SHORT).show();
        }
    }
}
