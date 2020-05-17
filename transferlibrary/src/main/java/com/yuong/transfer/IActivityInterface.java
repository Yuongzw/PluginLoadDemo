package com.yuong.transfer;

import android.app.Activity;
import android.os.Bundle;

/**
 * 用来模拟Activity的声明周期
 */
public interface IActivityInterface {

    /**
     * 插入宿主的Activity
     *
     * @param appActivity
     */
    void insertAppContext(Activity appActivity);

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onRestart();

    void onDestroy();

}
