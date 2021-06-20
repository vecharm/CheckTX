package org.moson.checktx.app.ui.home;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import org.moson.checktx.R;
import org.moson.checktx.app.constant.ApplicationX;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import me.jingbin.library.decoration.SpacesItemDecoration;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;

    private BaseRecyclerAdapter<PackageInfo> recyclerAdapter;
    private ByRecyclerView rv;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerAdapter = new BaseRecyclerAdapter<PackageInfo>(R.layout.home_item) {

            @Override
            protected void bindView(BaseByViewHolder<PackageInfo> holder, PackageInfo bean, int position) {
                holder.setText(R.id.appName, bean.applicationInfo.loadLabel(ApplicationX.getInstance().packageManager).toString())
                        .setImageDrawable(R.id.ivRes, bean.applicationInfo.loadIcon(ApplicationX.getInstance().packageManager))
                        .setText(R.id.publicSourceDir, bean.applicationInfo.publicSourceDir)
                        .setText(R.id.packageName, bean.packageName)
                        .setText(R.id.versionName, bean.versionName + "(" + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ? bean.getLongVersionCode() : bean.versionCode) + ")");
            }
        };
        rv.setAdapter(recyclerAdapter);
        rv.addItemDecoration(new SpacesItemDecoration(getActivity()));
        rv.setOnItemClickListener((v, position) -> {
            PackageInfo packageInfo = recyclerAdapter.getData().get(position);
            String appName = packageInfo.applicationInfo.loadLabel(ApplicationX.getInstance().packageManager).toString();
            Bundle bundle = new Bundle();
            bundle.putString("title", appName);
            bundle.putString("packageName", packageInfo.packageName);
            Navigation.findNavController(v).navigate(R.id.home2appinfo, bundle);
        });

        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mViewModel.getPackageInfoLiveData().observe(this, packageInfos -> {
            recyclerAdapter.setNewData(packageInfos);
        });
        mViewModel.loadData();
    }

}
