package org.moson.checktx.app.ui.appinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.moson.checktx.R;
import org.moson.checktx.app.bean.StackBean;
import org.moson.checktx.app.service.MainService;
import org.moson.checktx.app.utils.DangerPermEnum;
import org.moson.checktx.app.utils.SystemUtils;

import java.util.List;

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
    private FloatingActionButton fBtn;

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
        fBtn = view.findViewById(R.id.fBtn);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fBtn.setOnClickListener(v -> {
            if (getActivity() == null) {
                return;
            }
            new AlertDialog.Builder(getActivity())
                    .setTitle("温馨提示")
                    .setMessage("删除本地日志或通过邮件发送")
                    .setNegativeButton("删除", (dialog, which) -> {
                        if (MainService.getDaoSession(packageName) != null) {
                            SystemUtils.executorService.execute(() -> {
                                MainService.getDaoSession(packageName).getStackBeanDao().deleteAll();
                                mViewModel.loadLog(packageName);
                            });
                        }
                    })
                    .setPositiveButton("发送", (dialog, which) -> {
                        createEditDialog();
                    })
                    .create().show();
        });
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


    private void createEditDialog() {
        if (getActivity() == null) {
            return;
        }
        final EditText et = new EditText(getActivity());
        new AlertDialog.Builder(getActivity())
                .setTitle("请输入您的邮箱地址")
                .setView(et)
                .setPositiveButton("确定", (dialogInterface, i) -> {

                    List<StackBean> logAdapterDatas = logAdapter.getData();
                    StringBuilder strContent = new StringBuilder();
                    for (StackBean stackBean : logAdapterDatas) {
                        strContent.append(stackBean.method)
                                .append("(")
                                .append(SystemUtils.getStrTime(stackBean.createtime))
                                .append(")")
                                .append("\n")
                                .append(stackBean.front ? "前台" : "后台")
                                .append("\n")
                                .append(stackBean.stackInfo)
                                .append("\n");
                    }
                    String content = strContent.toString();
                    Log.i(TAG, "createEditDialog[content]: " + content);
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + et.getText().toString()));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "moson测试报告 " + packageName);
                    intent.putExtra(Intent.EXTRA_TEXT, content);
                    startActivity(intent);
                }).setNegativeButton("取消", null).show();
    }
}
