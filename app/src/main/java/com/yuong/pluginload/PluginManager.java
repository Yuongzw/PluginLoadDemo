package com.yuong.pluginload;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static final String TAG = PluginManager.class.getSimpleName();
    private static PluginManager instance;
    private Context context;
    private DexClassLoader dexClassLoader;
    private Resources pluginResource;

    private PluginManager(Context context) {
        this.context = context;
    }

    public static PluginManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager(context);
                }
            }
        }
        return instance;
    }

    public void loadPlugin(final Handler handler, final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(path);
                    if (!file.exists()) {
                        Log.e(TAG, "插件不存在");
                        return;
                    }
                    File pluginDir = context.getDir("plugin", Context.MODE_PRIVATE);
                    //加载插件的class
                    dexClassLoader = new DexClassLoader(path, pluginDir.getAbsolutePath(), null, context.getClassLoader());
                    //加载插件的资源文件
                    //1、获取插件的AssetManager
                    AssetManager pluginAssetManager = AssetManager.class.newInstance();
                    Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
                    addAssetPath.setAccessible(true);
                    addAssetPath.invoke(pluginAssetManager, path);
                    //2、获取宿主的Resources
                    Resources appResources = context.getResources();
                    //实例化插件的Resources
                    pluginResource = new Resources(pluginAssetManager, appResources.getDisplayMetrics(), appResources.getConfiguration());
                    if (dexClassLoader != null && pluginResource != null) {
                        handler.sendEmptyMessage(666);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();

    }

    @SuppressLint("PrivateApi")
    public void parsePlugin(String pluginPath) {
        try {
            File file = new File(pluginPath);
            if (!file.exists()) {
                Log.e(TAG, "插件不存在");
                return;
            }
            //1、解析插件包  public Package parsePackage(File packageFile, int flags)
            Class<?> mPackageParserClass = Class.forName("android.content.pm.PackageParser");
            Object mPackageParser = mPackageParserClass.newInstance();
            Method parsePackageMethod = mPackageParserClass.getMethod("parsePackage", File.class, int.class);
            Object mPackage = parsePackageMethod.invoke(mPackageParser, file, PackageManager.GET_ACTIVITIES);

            //2、获取Package类下的   public final ArrayList<Activity> receivers = new ArrayList<Activity>(0); 广播集合
            Field mReceiversField = mPackage.getClass().getDeclaredField("receivers");
            ArrayList<Object> receivers = (ArrayList<Object>) mReceiversField.get(mPackage);

            //3、遍历所有的静态广播
            //Activity 该Activity 不是四大组件里面的activity，而是一个Java bean对象，用来封装清单文件中的activity和receiver
            for (Object mActivity : receivers) {
                //4、获取该广播的全类名 即 <receiver android:name=".PluginStaticReceiver"> android:name属性后面的值
                //  /**
                //     * Public name of this item. From the "android:name" attribute.
                //     */
                //    public String name;

                // public static final ActivityInfo generateActivityInfo(Activity a, int flags,
                //            PackageUserState state, int userId)
                //先获取到 ActivityInfo类
                Class<?> mPackageUserStateClass = Class.forName("android.content.pm.PackageUserState");
                Object mPackageUserState = mPackageUserStateClass.newInstance();

                Method generateActivityInfoMethod = mPackageParserClass.getMethod("generateActivityInfo", mActivity.getClass(),
                        int.class, mPackageUserStateClass, int.class);
                //获取userId
                Class<?> mUserHandleClass = Class.forName("android.os.UserHandle");
                //public static @UserIdInt int getCallingUserId()
                int userId = (int) mUserHandleClass.getMethod("getCallingUserId").invoke(null);

                //执行此方法 由于是静态方法 所以不用传对象
                ActivityInfo activityInfo = (ActivityInfo) generateActivityInfoMethod.invoke(null, mActivity, 0, mPackageUserState, userId);
                String receiverClassName = activityInfo.name;
                Class<?> receiverClass = getClassLoader().loadClass(receiverClassName);
                BroadcastReceiver receiver = (BroadcastReceiver) receiverClass.newInstance();

                //5、获取 intent-filter  public final ArrayList<II> intents;这个是intent-filter的集合
                //静态内部类反射要用 $+类名
                //getField(String name)只能获取public的字段，包括父类的；
                //而getDeclaredField(String name)只能获取自己声明的各种字段，包括public，protected，private。
                Class<?> mComponentClass = Class.forName("android.content.pm.PackageParser$Component");
                Field intentsField = mActivity.getClass().getField("intents");
                ArrayList<IntentFilter> intents = (ArrayList<IntentFilter>) intentsField.get(mActivity);
                for (IntentFilter intentFilter : intents) {
                    //6、注册广播
                    context.registerReceiver(receiver, intentFilter);
                }

            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Resources getResource() {
        return pluginResource;
    }

    public DexClassLoader getClassLoader() {
        return dexClassLoader;
    }
}
