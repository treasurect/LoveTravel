package com.treasure.lovetravel.ui.activity;

import android.widget.ImageView;

import com.treasure.lovetravel.R;
import com.treasure.lovetravel.utils.SharedPreferencesUtil;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.image)
    ImageView image;

    @Override
    protected void loadContentLayout() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {
        //加载广告时间
        image.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean login_state = SharedPreferencesUtil.getInstance().getBoolean("login_state", false);
                if (!login_state) {
                    LoginActivity.start(SplashActivity.this);
                } else {
                    MainActivity.start(SplashActivity.this);
                }
                SplashActivity.this.finish();
            }
        }, 1500);
    }
}
