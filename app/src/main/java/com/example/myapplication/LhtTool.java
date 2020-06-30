package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.LinearLayout;


import androidx.core.content.ContextCompat;



import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * 项目工具类，定义一些全局的工具方法
 */

public class LhtTool {

    //用于标志用户是否登录，某些操作会用到该字段
    public static boolean isLogin;
    public static boolean isLoginPage = false;
    public static boolean idDianPuLogin = false;
    public static boolean isMainActivity;
    public static boolean isChatActivity;
    private static int num;
    public static boolean firstClick = true;
    public static String isFaLogin = "0";
    public static boolean isLoginFa = true;
    public static int unReadMessage = 0;

    public static void sendMessage(Handler hd, IOException e, int what) {
        Bundle b = new Bundle();
        b.putString("e", e.toString());
        Message ms = new Message();
        ms.what = what;
        ms.setData(b);
        hd.sendMessage(ms);
    }

    //显示网络请求异常对话框

    /**
     * 显示网络请求异常的弹窗
     * @param con
     * @param msg
     */
    public static void showNetworkException(Context con, Message msg) {

        num++;
        if (num == 1) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(con)
                    .setMessage("网络异常，请刷新后重试~~")
                    .setCancelable(true);

            alertDialog.create();

        }
        num = 0;
    }



    /**
     * 判断是否使用数据流量还是wifi
     * @param con
     * @return
     */
    public static int getNetworkType(Context con) {

        if (null != con) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static int getAppVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        int versioncode = 1;
        try {
            versioncode = packageManager.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versioncode;
    }

    /**
     * 获取版本名
     * @param context
     * @return
     */
    public static String getClientVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String versionName = null;
        try {
            versionName = packageManager.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    /**
     * 通过反射得到标题栏高度
     */

    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }







    /**
     * @param phoneNum 手机号码
     * @return 验证是不是手机号码
     */
    public static boolean isMobile(String phoneNum) {
        Pattern p;
        Matcher m;
        boolean b;
        p = Pattern.compile("^[1][1-9][0-9]{9}$"); // 验证手机号
        m = p.matcher(phoneNum);
        b = m.matches();
        return b;
    }














    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }




    /**
     * 设置状态栏的颜色
     * @param activity
     * @param color
     */
    public static void setColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 获取状态栏的高度大小
     * @param activity
     * @param color
     * @return
     */
    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }







}
