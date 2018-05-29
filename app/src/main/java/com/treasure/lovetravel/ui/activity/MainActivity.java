package com.treasure.lovetravel.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.treasure.lovetravel.R;
import com.treasure.lovetravel.bean.UserInfo;
import com.treasure.lovetravel.helper.Contacts;
import com.treasure.lovetravel.ui.fragment.FindFragment;
import com.treasure.lovetravel.ui.fragment.MessageFragment;
import com.treasure.lovetravel.ui.fragment.MineFragment;
import com.treasure.lovetravel.ui.fragment.StrategyFragment;
import com.treasure.lovetravel.utils.LogUtil;
import com.treasure.lovetravel.utils.SharedPreferencesUtil;
import com.treasure.lovetravel.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.strategy_img)
    ImageView strategyImg;
    @BindView(R.id.strategy_txt)
    TextView strategyText;
    @BindView(R.id.find_img)
    ImageView findImg;
    @BindView(R.id.find_txt)
    TextView findText;
    @BindView(R.id.message_img)
    ImageView mesImg;
    @BindView(R.id.message_txt)
    TextView mesText;
    @BindView(R.id.mine_img)
    ImageView mineImg;
    @BindView(R.id.mine_txt)
    TextView mineText;

    private StrategyFragment strategyFragment;
    private FindFragment findFragment;
    private MessageFragment messageFragment;
    private MineFragment mineFragment;
    private Fragment lastFragment;
    private FragmentManager fm;
    private int lastId;

    @Override
    protected void loadContentLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        getUserInfo();

        findFragment = new FindFragment();
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container, findFragment,"FindFragment").commit();
        lastFragment = findFragment;

        findImg.setSelected(true);
        findText.setSelected(true);
        lastId = findImg.getId();
    }

    //获取用户信息，保存到本地，保证数据的最新
    private void getUserInfo() {
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo("objectId", SharedPreferencesUtil.getInstance().getString(Contacts.USER_ID));
        query.setLimit(1);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> object, BmobException e) {
                if (e == null) {
                    if (object != null && object.get(0) != null) {
                        UserInfo userInfo = object.get(0);
                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_ID, userInfo.getObjectId());
                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_NAME, userInfo.getUser_name());
                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_NICK, userInfo.getUser_nick());
                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_PWD, userInfo.getUser_pwd());
                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_IMAGE, userInfo.getUser_image());
                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_SEX, userInfo.getUser_sex());
                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_QQ, userInfo.getUser_qq());
                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_MAIL, userInfo.getUser_mail());
                        LogUtil.e("~~~~~~~~用户数据更新成功");
                    } else {
                        dissLoading();
                        LogUtil.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.strategy_layout, R.id.find_layout, R.id.message_layout,R.id.mine_layout,R.id.add_layout,R.id.container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.strategy_layout:
                if (strategyFragment == null) {
                    strategyFragment = new StrategyFragment();
                }

                if (lastFragment != strategyFragment) {
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    if (!strategyFragment.isAdded()) {
                        fragmentTransaction.add(R.id.container, strategyFragment, "StrategyFragment").hide(lastFragment).commit();
                    } else {
                        fragmentTransaction.show(strategyFragment).hide(lastFragment).commit();
                    }

                    lastFragment = strategyFragment;
                    changeIcon(R.id.strategy_img);
                }
                break;
            case R.id.find_layout:
                if (findFragment == null) {
                    findFragment = new FindFragment();
                }

                if (lastFragment != findFragment) {
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    if (!findFragment.isAdded()) {
                        fragmentTransaction.add(R.id.container, findFragment, "FindFragment").hide(lastFragment).commit();
                    } else {
                        fragmentTransaction.show(findFragment).hide(lastFragment).commit();
                    }

                    lastFragment = findFragment;
                    changeIcon(R.id.find_img);
                }
                break;
            case R.id.message_layout:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                }

                if (lastFragment != messageFragment) {
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    if (!messageFragment.isAdded()) {
                        fragmentTransaction.add(R.id.container, messageFragment, "MessageFragment").hide(lastFragment).commit();
                    } else {
                        fragmentTransaction.show(messageFragment).hide(lastFragment).commit();
                    }

                    lastFragment = messageFragment;
                    changeIcon(R.id.message_img);
                }
                break;
            case R.id.mine_layout:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }

                if (lastFragment != mineFragment) {
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    if (!mineFragment.isAdded()) {
                        fragmentTransaction.add(R.id.container, mineFragment, "MineFragment").hide(lastFragment).commit();
                    } else {
                        fragmentTransaction.show(mineFragment).hide(lastFragment).commit();
                    }

                    lastFragment = mineFragment;
                    changeIcon(R.id.mine_img);
                }
                break;
            case R.id.container:
                break;
            case R.id.add_layout:
                ToastUtils.showToast("ADD");
                break;
        }

    }

    /**
     * 改变底部按钮颜色
     */
    public void changeIcon(int selectedId) {
        if (selectedId != lastId) {
            switch (selectedId) {
                case R.id.strategy_img:
                    changeTxt(strategyImg, strategyText);
                    break;
                case R.id.find_img:
                    changeTxt(findImg, findText);
                    break;
                case R.id.message_img:
                    changeTxt(mesImg, mesText);
                    break;
                case R.id.mine_img:
                    changeTxt(mineImg, mineText);
                    break;
            }
        }
    }

    private void changeTxt(ImageView imageView, TextView textView) {
        imageView.setSelected(true);
        textView.setSelected(true);
        switch (lastId) {
            case R.id.strategy_img:
                noSelected(strategyImg, strategyText);
                break;
            case R.id.find_img:
                noSelected(findImg, findText);
                break;
            case R.id.message_img:
                noSelected(mesImg, mesText);
                break;
            case R.id.mine_img:
                noSelected(mineImg, mineText);
                break;
        }
        lastId = imageView.getId();
    }

    private void noSelected(ImageView imageView, TextView textView) {
        imageView.setSelected(false);
        textView.setSelected(false);
    }

    @Subscribe
    public void onEventMainThread(String toPersonalPilePage) {
        if (toPersonalPilePage != null) {

        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
