package org.moson.checktx.app.ui.appinfo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.moson.checktx.R;
import org.moson.checktx.app.bean.StackBean;
import org.moson.checktx.app.constant.ApplicationX;
import org.moson.checktx.app.utils.DangerPermEnum;
import org.moson.checktx.app.utils.SystemUtils;

import de.robv.android.xposed.XposedBridge;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import me.jingbin.library.decoration.SpacesItemDecoration;

public class AppInfoFragment extends Fragment {


    private static final String TAG = AppInfoFragment.class.getSimpleName();

    private AppInfoViewModel mViewModel;

    private String packageName;

    private BaseRecyclerAdapter<DangerPermEnum> permAdapter;
    private BaseRecyclerAdapter<StackBean> logAdapter;

    private ByRecyclerView permRv;
    private ByRecyclerView logRv;

    public static AppInfoFragment newInstance() {
        return new AppInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString("title");
            packageName = getArguments().getString("packageName");
        }
        if (getActivity() instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(title);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.app_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        permRv = view.findViewById(R.id.permRv);
        logRv = view.findViewById(R.id.logRv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        permAdapter = new BaseRecyclerAdapter<DangerPermEnum>(R.layout.app_info_perm_item) {
            @Override
            protected void bindView(BaseByViewHolder<DangerPermEnum> holder, DangerPermEnum bean, int position) {
                holder.setText(R.id.tvPerm, bean.getName());
                holder.setText(R.id.tvDesc, bean.getDesc());
            }
        };
        permRv.setAdapter(permAdapter);
        if (getActivity() != null) {
            permRv.addItemDecoration(new SpacesItemDecoration(getActivity()));
        }

        logAdapter = new BaseRecyclerAdapter<StackBean>(R.layout.app_info_log_item) {
            @Override
            protected void bindView(BaseByViewHolder<StackBean> holder, StackBean bean, int position) {
                holder.setText(R.id.tvCreateTime, bean.method + "(" + SystemUtils.getStrTime(bean.createtime) + ")");
                holder.setText(R.id.tvFront, bean.front ? "前台" : "后台");
                holder.setText(R.id.tvStackInfo, bean.stackInfo);
            }
        };
        logRv.setAdapter(logAdapter);
        if (getActivity() != null) {
            logRv.addItemDecoration(new SpacesItemDecoration(getActivity()));
        }

        mViewModel = ViewModelProviders.of(this).get(AppInfoViewModel.class);
        mViewModel.getPermLiveData().observe(this, dangerPermEnums -> {
            permAdapter.setNewData(dangerPermEnums);
        });
        mViewModel.getLogLiveData().observe(this, stackBeans -> {
            logAdapter.setNewData(stackBeans);
        });
        mViewModel.loadDangerPerm(packageName);
        mViewModel.loadLog(packageName);
    }

}
