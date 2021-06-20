package org.moson.checktx.xpose;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import org.moson.checktx.ICheckTx;
import org.moson.checktx.app.service.MainService;
import org.moson.checktx.app.utils.DangerPermEnum;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Copyright (C), 2016-2021
 * Author: 超人迪加
 * Date: 6/18/21 5:23 PM
 */
public class MainHook implements IXposedHookLoadPackage {
    Application mApplication;
    ICheckTx iCheckTx;

    //记录Activity的总个数
    private int actCount = 0;

    private boolean front = false;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        hookApplication();
    }


    private void hookApplication() {
        try {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    mApplication = (Application) param.thisObject;
                    if ((mApplication.getApplicationInfo().flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                        return;
                    }
                    String packageResourcePath = mApplication.getPackageResourcePath();
                    boolean isDataApp = packageResourcePath.startsWith("/data/app/");
                    if (!isDataApp) {
                        return;
                    }
                    if ("org.moson.checktx".equals(mApplication.getPackageName())
                            || "com.oasisfeng.greenify".equals(mApplication.getPackageName())
                            || mApplication.getPackageName().contains("xposed")
                            || mApplication.getPackageName().contains("magisk")
                            || mApplication.getPackageName().startsWith("com.google")
                            || mApplication.getPackageName().startsWith("com.android")
                    ) {
                        return;
                    }
                    registerActivityLifecycleCallbacks();
                    bindService();
                    hookApi();
                }
            });
        } catch (Exception e) {
            XposedBridge.log(mApplication.getPackageName() + "--bindServiceException");
            XposedBridge.log(e);
        }
    }

    private void registerActivityLifecycleCallbacks() {
        mApplication.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                if (activity instanceof FragmentActivity) {
                    registerFragmentLifecycleCallbacks((FragmentActivity) activity);
                }
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                if (actCount == 0) { //后台切换到前台
                    XposedBridge.log(mApplication.getPackageName() + "--前台");
                    front = true;
                }
                actCount++;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                actCount--;
                if (actCount == 0) { //前台切换到后台
                    XposedBridge.log(mApplication.getPackageName() + "--后台");
                    front = false;
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    private void registerFragmentLifecycleCallbacks(@NonNull FragmentActivity activity) {
        activity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentPreAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
                super.onFragmentPreAttached(fm, f, context);
            }

            @Override
            public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
                super.onFragmentAttached(fm, f, context);
            }

            @Override
            public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentPreCreated(fm, f, savedInstanceState);
            }

            @Override
            public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentCreated(fm, f, savedInstanceState);
            }

            @Override
            public void onFragmentActivityCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentActivityCreated(fm, f, savedInstanceState);
            }

            @Override
            public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState);
            }

            @Override
            public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentStarted(fm, f);
            }

            @Override
            public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentResumed(fm, f);
            }

            @Override
            public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentPaused(fm, f);
            }

            @Override
            public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentStopped(fm, f);
            }

            @Override
            public void onFragmentSaveInstanceState(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Bundle outState) {
                super.onFragmentSaveInstanceState(fm, f, outState);
            }

            @Override
            public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
            }

            @Override
            public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDestroyed(fm, f);
            }

            @Override
            public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDetached(fm, f);
            }
        }, true);
    }

    private void bindService() {
        ComponentName componentName = new ComponentName("org.moson.checktx", MainService.class.getName());
        Intent intent = new Intent();
        intent.setComponent(componentName);
        mApplication.bindService(intent, new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                XposedBridge.log(mApplication.getPackageName() + ".onServiceConnected[name]=" + name);
                iCheckTx = ICheckTx.Stub.asInterface(service);
                try {
                    String[] permissions = mApplication.getPackageManager()
                            .getPackageInfo(mApplication.getPackageName(), PackageManager.GET_PERMISSIONS)
                            .requestedPermissions;//获取权限列表
                    for (String permission : permissions) {
                        DangerPermEnum[] values = DangerPermEnum.values();
                        for (DangerPermEnum value : values) {
                            if (value.getName().equals(permission)) {
                                XposedBridge.log("permission=" + permission);
                            }
                        }
                    }
                    iCheckTx.setPermission(mApplication.getPackageName(), permissions);
                } catch (Exception e) {
                    XposedBridge.log(mApplication.getPackageName() + "--setPermissionException");
                    XposedBridge.log(e);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                XposedBridge.log(mApplication.getPackageName() + ".onServiceDisconnected[name]=" + name);
            }
        }, Service.BIND_AUTO_CREATE);
    }


    private void hookApi() {
        //TelephonyManager
        XposedBridge.hookAllMethods(TelephonyManager.class, "getDeviceId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                sendLog(param.method.getName());
            }
        });
        XposedBridge.hookAllMethods(TelephonyManager.class, "getLine1Number", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                sendLog(param.method.getName());
            }
        });
        XposedBridge.hookAllMethods(TelephonyManager.class, "getSimSerialNumber", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                sendLog(param.method.getName());
            }
        });
        XposedBridge.hookAllMethods(TelephonyManager.class, "getSubscriberId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                sendLog(param.method.getName());
            }
        });
        //https://blog.csdn.net/qq_43278826/article/details/95216504
        XposedBridge.hookAllMethods(TelephonyManager.class, "getImei", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                sendLog(param.method.getName());
            }
        });

        //sdcard
        XposedBridge.hookAllMethods(Environment.class, "getExternalStorageDirectory", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                sendLog(param.method.getName());
            }
        });

        //calendar phone
        XposedBridge.hookAllMethods(ContentResolver.class, "query", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Uri argUri = (Uri) param.args[0];
                String arg = argUri.toString();
                if (arg.contains("calendar")) {
                    sendLog("calendar");
                } else if (arg.contains("phone")) {
                    sendLog("phone");
                } else if (arg.contains("call")) {
                    sendLog("call");
                } else if (arg.contains("sms")) {
                    sendLog("sms");
                }
            }
        });
        XposedBridge.hookAllMethods(ContentResolver.class, "insert", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Uri argUri = (Uri) param.args[0];
                String arg = argUri.toString();
                if (arg.contains("calendar")) {
                    sendLog("calendar");
                } else if (arg.contains("phone")) {
                    sendLog("phone");
                } else if (arg.contains("call")) {
                    sendLog("call");
                } else if (arg.contains("sms")) {
                    sendLog("sms");
                }
            }
        });

        //LocationManager
        XposedBridge.hookAllMethods(LocationManager.class, "requestLocationUpdates", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                sendLog(param.method.getName());
            }
        });

        //AccountManager
        XposedBridge.hookAllMethods(AccountManager.class, "addAccountExplicitly", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                sendLog(param.method.getName());
            }
        });

        //Camera
        XposedBridge.hookAllMethods(Camera.class, "open", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                sendLog(param.method.getName());
            }
        });
        XposedBridge.hookAllMethods(Camera.class, "getNumberOfCameras", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                sendLog(param.method.getName());
            }
        });
    }

    private void sendLog(String method) {
        try {
            if (iCheckTx != null) {
                StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
                StringBuilder stackInfo = new StringBuilder();
                for (StackTraceElement element : stackTraces) {
                    if (element.toString().startsWith("dalvik.")
                            || element.toString().startsWith("java.")
                            || element.toString().startsWith("android.")
                            || element.toString().startsWith("org.moson.checktx")
                            || element.toString().startsWith("de.robv.android.xposed")
                            || element.toString().startsWith("EdHooker")
                    ) {
                        continue;
                    }
                    stackInfo
                            .append(element.toString())
                            .append("\n");
                }
                iCheckTx.setStackInfo(mApplication.getPackageName(), method, stackInfo.toString(), front);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}