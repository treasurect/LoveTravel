package com.treasure.lovetravel.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.treasure.lovetravel.R;
import com.treasure.lovetravel.listener.PhotoListener;
import com.treasure.lovetravel.utils.ScreenUtil;

/**
 * Created by 18410 on 2017/9/26.
 */

public class PhotoDialog extends Dialog {
    private Context context;
    private PhotoListener listener;
    public PhotoDialog(@NonNull Context context) {
        super(context, R.style.gender_change);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_photo);
        init();
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.gender_change);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ScreenUtil.getScreenWidth(context);
        attributes.height = ScreenUtil.dip2px(context,180);
        attributes.horizontalMargin = 100;
        window.setAttributes(attributes);
    }

    private void init() {
        findViewById(R.id.xiangji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.camera();
                dismiss();
            }
        });
        findViewById(R.id.xiangce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.album();
                dismiss();
            }
        });
        findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setLisener( PhotoListener listener){
        this.listener = listener;
    }
}
