package org.moson.checktx.app.ui.home;

import android.content.pm.PackageInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.moson.checktx.app.constant.ApplicationX;
import org.moson.checktx.app.service.MainService;
import org.moson.checktx.app.utils.SystemUtils;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<PackageInfo>> packageInfoLiveData = new MutableLiveData<>();

    public void loadData() {
        SystemUtils.executorService.submit(() -> {
            List<PackageInfo> appList = SystemUtils.getAppList(ApplicationX.getInstance());
            for (PackageInfo info : appList) {
                MainService.createDAOSession(info.packageName);
            }
            packageInfoLiveData.postValue(appList);
        });
    }

    public LiveData<List<PackageInfo>> getPackageInfoLiveData() {
        return packageInfoLiveData;
    }
}
