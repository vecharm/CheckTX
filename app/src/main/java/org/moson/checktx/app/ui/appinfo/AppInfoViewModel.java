package org.moson.checktx.app.ui.appinfo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.moson.checktx.app.bean.StackBean;
import org.moson.checktx.app.constant.ApplicationX;
import org.moson.checktx.app.service.MainService;
import org.moson.checktx.app.utils.DangerPermEnum;
import org.moson.checktx.app.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

public class AppInfoViewModel extends ViewModel {


    private MutableLiveData<List<DangerPermEnum>> permLiveData = new MutableLiveData<>();
    private MutableLiveData<List<StackBean>> logLiveData = new MutableLiveData<>();


    public void loadDangerPerm(String packageName) {
        String[] permissions = null;//获取权限列表
        try {
            permissions = ApplicationX.getInstance().getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                    .requestedPermissions;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (permissions == null) {
            return;
        }
        List<DangerPermEnum> dangerPermEnums = new ArrayList<>();
        for (String permission : permissions) {
            DangerPermEnum[] values = DangerPermEnum.values();
            for (DangerPermEnum value : values) {
                if (value.getName().equals(permission)) {
                    dangerPermEnums.add(value);
                }
            }
        }
        permLiveData.setValue(dangerPermEnums);
    }

    public void loadLog(String packageName) {
        SystemUtils.executorService.execute(() -> {
            if (MainService.getDaoSession(packageName) != null) {
                List<StackBean> stackBeans = MainService.getDaoSession(packageName).getStackBeanDao().loadAll();
                logLiveData.postValue(stackBeans);
            }
        });

    }

    public LiveData<List<DangerPermEnum>> getPermLiveData() {
        return permLiveData;
    }

    public LiveData<List<StackBean>> getLogLiveData() {
        return logLiveData;
    }
}
