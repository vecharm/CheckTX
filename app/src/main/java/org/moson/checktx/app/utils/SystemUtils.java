package org.moson.checktx.app.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (C), 2016-2021
 * Author: 超人迪加
 * Date: 6/18/21 6:30 PM
 */
public class SystemUtils {

    public final static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA);

    public static List<PackageInfo> getAppList(Context context) {
        PackageManager pm = context.getPackageManager();
        // Return a List of all packages that are installed on the device.
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        List<PackageInfo> packages2User = new ArrayList<>();
        for (PackageInfo packageInfo : packages) {
            // 判断系统/非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
            {
                packages2User.add(packageInfo);
            }
        }
        return packages2User;
    }

    //时间戳转字符串
    public static String getStrTime(long timeStamp) {
        String timeString = null;
        //设置地区
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        timeString = sdf.format(new Date(timeStamp));//单位秒
        return timeString;
    }
}
