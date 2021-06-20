package org.moson.checktx.app.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

public class GreenDaoContext extends ContextWrapper {

    private final String targetPackageName;

    public GreenDaoContext(Context base, String targetPackageName) {
        super(base);
        this.targetPackageName = targetPackageName;
    }

    /**
     * 获得数据库路径，如果不存在，则自动创建
     */
    @Override
    public File getDatabasePath(String name) {
        File filePath = new File(getFilesDir() + File.separator + targetPackageName + File.separator);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        return new File(filePath, name);
    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        Log.i("MQTTHelper", "openOrCreateDatabase 2.3");
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see ContextWrapper#openOrCreateDatabase(String, int,
     * SQLiteDatabase.CursorFactory,
     * DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        Log.i("MQTTHelper", "openOrCreateDatabase 4.0");
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }
}