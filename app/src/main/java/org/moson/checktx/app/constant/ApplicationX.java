package org.moson.checktx.app.constant;

import android.app.Application;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import org.moson.checktx.app.utils.GreenDaoContext;
import org.moson.checktx.app.utils.MySQLiteOpenHelper;

/**
 * Copyright (C), 2016-2021
 * Author: 超人迪加
 * Date: 6/18/21 5:41 PM
 */
public class ApplicationX extends Application {
    private static ApplicationX instance;

    public PackageManager packageManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        packageManager = getPackageManager();
    }

    public static ApplicationX getInstance() {
        return instance;
    }
}
