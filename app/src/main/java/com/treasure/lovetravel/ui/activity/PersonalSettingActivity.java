package com.treasure.lovetravel.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.treasure.lovetravel.R;
import com.treasure.lovetravel.helper.Contacts;
import com.treasure.lovetravel.listener.GenderChangeListener;
import com.treasure.lovetravel.ui.views.OutLogDialog;
import com.treasure.lovetravel.utils.FileUtils;
import com.treasure.lovetravel.utils.SharedPreferencesUtil;
import com.treasure.lovetravel.utils.ToastUtils;
import com.treasure.lovetravel.utils.Utils;

import java.io.File;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalSettingActivity extends BaseActivity implements GenderChangeListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, PersonalSettingActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.cachenum)
    TextView cacheNum;
    @BindView(R.id.versionname)
    TextView versionn;

    private String persnalDir;
    private String versionName;
    private OutLogDialog outLogDialog;

    @Override
    protected void loadContentLayout() {
        setContentView(R.layout.activity_personal_setting);
        try {
            versionName = Utils.getVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        title.setText("设置");
        versionn.setText(versionName);
        initCache();
    }

    @Override
    protected void setListener() {
    }


    private void initCache() {
        if (persnalDir == null || persnalDir.equals("")) {
            persnalDir = Utils.getPersnalDir(Contacts.CACHE);
        }
        double cacheSize = FileUtils.getFileOrFilesSize(
                persnalDir,
                FileUtils.SIZETYPE_MB);

        BigDecimal bg = new BigDecimal(cacheSize);
        double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        cacheNum.setText(value + "M");
    }

    @OnClick({R.id.back, R.id.out_login, R.id.clearcache, R.id.about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.out_login:
                if (outLogDialog == null) {
                    outLogDialog = new OutLogDialog(this);
                    outLogDialog.setListener(this);
                }
                outLogDialog.show();
                break;
            case R.id.clearcache:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.clean_cache_confirm))
                        .setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FileUtils.deleteFile(new File(persnalDir));
                                initCache();
                                ToastUtils.showSingleToast(getString(R.string.clean_cache_success));
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.about:
//                Intent intent1 = new Intent(this, PersonalAboutHuLuActivity.class);
//                startActivity(intent1);
                ToastUtils.showToast(getString(R.string.app_info));
                break;
        }
    }

    @Override
    public void gerderClick(int id) {
        switch (id) {
            case R.id.cancle:
                outLogDialog.dismiss();
                break;
            case R.id.outlogin:
                outLogin();
                outLogDialog.dismiss();
                break;
        }
    }

    private void outLogin() {
        SharedPreferencesUtil.getInstance().removeAll();
        ToastUtils.showToast(getString(R.string.outlogin_success));
        LoginActivity.start(this);
    }

}
