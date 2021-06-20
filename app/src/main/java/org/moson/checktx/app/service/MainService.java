package org.moson.checktx.app.service;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import org.moson.checktx.ICheckTx;
import org.moson.checktx.app.bean.StackBean;
import org.moson.checktx.app.constant.ApplicationX;
import org.moson.checktx.app.dao.DaoMaster;
import org.moson.checktx.app.dao.DaoSession;
import org.moson.checktx.app.utils.GreenDaoContext;
import org.moson.checktx.app.utils.MySQLiteOpenHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (C), 2016-2021
 * Author: 超人迪加
 * Date: 6/18/21 6:08 PM
 */
public class MainService extends Service {

    private static final String TAG = "MainService";

    private final static Map<String, DaoSession> mMapDaoSession = new ConcurrentHashMap<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyCheckTx();
    }

    static class MyCheckTx extends ICheckTx.Stub {

        @Override
        public void setPermission(String packageName, String[] permissions) throws RemoteException {

        }

        @Override
        public void setStackInfo(String packageName, String method, String stackInfo, boolean front) throws RemoteException {
            createDAOSession(packageName);
            DaoSession daoSession = getDaoSession(packageName);
            if (daoSession != null) {
                StackBean stackBean = new StackBean();
                stackBean.method = method;
                stackBean.front = front;
                stackBean.createtime = System.currentTimeMillis();
                stackBean.stackInfo = stackInfo;
                daoSession.getStackBeanDao().insert(stackBean);
            }
        }
    }

    public static void createDAOSession(String packageName) {
        if (mMapDaoSession.get(packageName) == null) {
            Log.i(TAG, "createDAOSession[Thread.currentThread().getName()]: " + Thread.currentThread().getName());
            MySQLiteOpenHelper mHelper = new MySQLiteOpenHelper(new GreenDaoContext(ApplicationX.getInstance(), packageName), "db_log.db", null);
            SQLiteDatabase db = mHelper.getWritableDatabase();
            DaoMaster mDaoMaster = new DaoMaster(db);
            DaoSession mDaoSession = mDaoMaster.newSession();
            mMapDaoSession.put(packageName, mDaoSession);
        }
    }

    public static DaoSession getDaoSession(String packageName) {
        return mMapDaoSession.get(packageName);
    }
}
