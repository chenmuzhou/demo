package com.example.myapplication;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Process;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 *
 */

public class MyApplication extends Application{

    private MyBroadcastreceiver myBroadcastreceiver;
    public static UserInfo userInfo;



    @Override
    public void onCreate() {
        // 程序创建的时候执行
        super.onCreate();
        //okhttp初始化，感觉没什么用
        OkhttpTool.getOkhttpTool();
        //log显示开关
        LogUtils.showLog = false;

        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(Process.myPid());

        IntentFilter inf = new IntentFilter();
        inf.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        myBroadcastreceiver = new MyBroadcastreceiver();
        registerReceiver(myBroadcastreceiver, inf);

    }
    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        unregisterReceiver(myBroadcastreceiver);
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行（回收内存）
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        super.onLowMemory();
    }

    class MyBroadcastreceiver extends BroadcastReceiver {

        /**
         * TYPE_NONE        = -1
         * TYPE_MOBILE      = 0
         * TYPE_WIFI        = 1
         * TYPE_MOBILE_MMS  = 2
         * TYPE_MOBILE_SUPL = 3
         * TYPE_MOBILE_DUN  = 4
           * TYPE_MOBILE_HIPRI = 5
         * TYPE_WIMAX       = 6
         * TYPE_BLUETOOTH   = 7
         * TYPE_DUMMY       = 8
         * TYPE_ETHERNET    = 9
         * TYPE_MOBILE_FOTA = 10
         * TYPE_MOBILE_IMS  = 11
         * TYPE_MOBILE_CBS  = 12
         * TYPE_WIFI_P2P    = 13
         * TYPE_MOBILE_IA = 14
         * TYPE_MOBILE_EMERGENCY = 15
         * TYPE_PROXY = 16
         * TYPE_VPN = 17
         * <p>
         * <p>
         * <p>
         * 擦，不知道为什么，Toast打的出来，log信息打不出来，可能是as的bug，不过经过测试这个功能没什么问题
         */

        //
        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager com = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = com.getActiveNetworkInfo();
            if (ni != null && ni.isAvailable()) {
                switch (ni.getTypeName()) {
                    case "MOBILE":
                        //CustomToast.showToast(getApplicationContext(), "你当前网络为移动网络，请注意流量的使用", Toast.LENGTH_SHORT);
                        break;
                    case "WIFI":
                      //  CustomToast.showToast(getApplicationContext(), "你当前网络为WIFI网络，请放心使用", Toast.LENGTH_SHORT);
                        break;
                }
//                CustomToast.showToast(getApplicationContext(), "连接模式==========================" + ni.getTypeName(), Toast.LENGTH_SHORT);
            } else {
              //  CustomToast.showToast(getApplicationContext(), "当前无网络，请检查后重试!", Toast.LENGTH_SHORT);
            }

        }
    }

}
