package com.treasure.lovetravel.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.treasure.lovetravel.R;
import com.treasure.lovetravel.listener.GenderChangeListener;
import com.treasure.lovetravel.utils.ScreenUtil;

/**
 * Created by 18410 on 2017/8/24.
 */

public class EditorDialog extends Dialog {
    private GenderChangeListener listener;
    private Context context;
    private EditText editText;

    public EditorDialog(@NonNull Context context) {
        super(context, R.style.gender_change);
        this.context = context;
    }


    public void setListener(GenderChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editor);
        init();
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.gender_change);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ScreenUtil.getScreenWidth(context);
        attributes.height = ScreenUtil.dip2px(context, 190);
        window.setAttributes(attributes);
    }

    private void init() {
        editText = ((EditText) findViewById(R.id.editor));
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.gerderClick(R.id.cancel);
            }
        });
        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.gerderClick(R.id.done);
            }
        });
    }

    public String getEditorText() {
        if (editText == null)
            return "";
        return editText.getText().toString().trim();
    }

    public void setEditorText(String text) {
        if (editText != null) {
            editText.setText(text);
        }
    }
}
