package com.yuong.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.yuong.transfer.IReceiverInterface;

public class PluginReceiver extends BroadcastReceiver implements IReceiverInterface {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("str"), Toast.LENGTH_SHORT).show();
    }
}
