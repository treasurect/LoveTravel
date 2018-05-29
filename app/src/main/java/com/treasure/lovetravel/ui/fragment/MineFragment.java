package com.treasure.lovetravel.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.treasure.lovetravel.R;
import com.treasure.lovetravel.helper.Contacts;
import com.treasure.lovetravel.ui.activity.PersonalInfoActivity;
import com.treasure.lovetravel.ui.activity.PersonalSettingActivity;
import com.treasure.lovetravel.ui.activity.PersonalSpaceActivity;
import com.treasure.lovetravel.utils.GlideUtils;
import com.treasure.lovetravel.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 18410 on 2017/8/1.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.head_img)
    ImageView headImg;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sex)
    ImageView sex;

    private String usernick;
    private String avatar;
    private int gender;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_mine);

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    public void onResume() {
        super.onResume();
        //先本地拿
        usernick = SharedPreferencesUtil.getInstance().getString(Contacts.USER_NICK);
        avatar = SharedPreferencesUtil.getInstance().getString(Contacts.USER_IMAGE);
        gender = Integer.parseInt(SharedPreferencesUtil.getInstance().getString(Contacts.USER_SEX));

        name.setText(usernick);
        GlideUtils.loadRoundImg(getContext(),avatar,headImg, R.mipmap.ic_travel,3);

        //性别
        setSex(gender);
    }

    @OnClick({R.id.headlayout, R.id.my_data, R.id.my_space, R.id.settings})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.headlayout:
                PersonalInfoActivity.start(getContext());
                break;
            case R.id.my_data:
                PersonalInfoActivity.start(getContext());
                break;
            case R.id.my_space:
                PersonalSpaceActivity.start(getContext());
                break;
            case R.id.settings:
                PersonalSettingActivity.start(getContext());
                break;
        }
    }


    private void setSex(int gender) {
        if (gender == 1) {
            sex.setBackgroundResource(R.mipmap.ic_sex_boy);
        } else if (gender == 2) {
            sex.setBackgroundResource(R.mipmap.ic_sex_girl);
        } else {
            sex.setBackgroundResource(R.mipmap.ic_sex_none);
        }
    }


}
