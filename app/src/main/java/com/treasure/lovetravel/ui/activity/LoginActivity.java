package com.treasure.lovetravel.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.treasure.lovetravel.R;
import com.treasure.lovetravel.bean.UserInfo;
import com.treasure.lovetravel.helper.Contacts;
import com.treasure.lovetravel.ui.views.LoadingDialog;
import com.treasure.lovetravel.utils.LogUtil;
import com.treasure.lovetravel.utils.PhoneUtils;
import com.treasure.lovetravel.utils.SharedPreferencesUtil;
import com.treasure.lovetravel.utils.StringUtils;
import com.treasure.lovetravel.utils.ToastUtils;
import com.treasure.lovetravel.utils.Utils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by 18410 on 2017/7/31.
 */

public class LoginActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.phonenum)
    EditText phonenum;
    @BindView(R.id.passwd)
    EditText passwd;
    @BindView(R.id.login_btn)
    TextView loginBtn;
    @BindView(R.id.phonehitn)
    TextView phonehitn;
    @BindView(R.id.pswhint)
    TextView pswhint;

    private boolean see = false;
    private String accout;
    private String psw;
    private LoadingDialog.Builder builder;
    private LoadingDialog loading;
    private Unbinder bind;
    private Context context;
    String uniquePsuedoID;

    @Override
    protected void loadContentLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        context = this;
        bind = ButterKnife.bind(this);
        // initVideo();
        AndPermission.with(this)
                .requestCode(200)
                .permission(Permission.PHONE, Permission.LOCATION, Permission.STORAGE, Permission.CAMERA)
                .callback(listener)
                .start();
        uniquePsuedoID = PhoneUtils.getUniquePsuedoID();
    }

    @Override
    protected void setListener() {
        loginBtn.setClickable(false);
        phonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (string.length() > 0 && phonehitn.getVisibility() == View.GONE) {
                    phonehitn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                if (string.length() == 0 && phonehitn.getVisibility() == View.VISIBLE) {
                    phonehitn.setVisibility(View.GONE);
                }
                String psw = passwd.getText().toString().trim();
//                if (s.length()>0&& psw.length()>0){
//                    btnHasInput();
//                }else {
//                    btnNoInput();
//                }
            }
        });
        passwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (string.length() > 0 && pswhint.getVisibility() == View.GONE) {
                    pswhint.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                if (string.length() == 0 && pswhint.getVisibility() == View.VISIBLE) {
                    pswhint.setVisibility(View.GONE);
                }
                String num = phonenum.getText().toString();
                if (s.length() > 0 && num.length() > 0) {
//                    loginBtn.setBackgroundResource(R.drawable.loginbtn);
//                    loginBtn.setAlpha((float)1);
                    loginBtn.setClickable(true);
                    loginBtn.setSelected(true);
                } else {
//                    loginBtn.setBackgroundResource(R.drawable.loginbtn_noinput);
//                    loginBtn.setAlpha((float)0.2);
                    loginBtn.setClickable(false);
                    loginBtn.setSelected(false);
                }
            }
        });

        builder = new LoadingDialog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(true);

        loading = builder.create();
    }

    @OnClick({R.id.seepasswd, R.id.fogetpass, R.id.register, R.id.root, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.seepasswd:
                if (see) {
                    passwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    see = false;
                } else {
                    passwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    see = true;
                }
                break;
            case R.id.fogetpass:
                ResetPassActivity.start(this);
                break;
            case R.id.register:
                RegisterActivity.start(this);
                break;

            case R.id.root:
                Utils.hideSoftInput(this, passwd);
                break;

            case R.id.login_btn:
                if (!Utils.isFastClick()) {
                    boolean clickable = loginBtn.isClickable();
                    if (clickable) {
                        accout = phonenum.getText().toString();
                        psw = passwd.getText().toString();
                        if (StringUtils.isBlank(accout)) {
                            ToastUtils.showSingleToast("手机号不能为空");
                            return;
                        } else if (StringUtils.isBlank(psw)) {
                            ToastUtils.showSingleToast("密码不能为空");
                            return;
                        } else if (psw.length() < 6) {
                            ToastUtils.showSingleToast("密码必须大于等于6位");
                            return;
                        } else if (accout.length() < 4) {
                            ToastUtils.showSingleToast("请输入正确账号");
                            return;
                        } else {
                            login(accout, psw);
                        }
                    }
                }

                break;
        }
    }

    //登录
    private void login(final String accout, final String psw) {
        showLoading();
        //先查询到objectID  再根据id进行psw对比
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo("user_name", accout);
        query.setLimit(1);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> object, BmobException e) {
                if (e == null) {
                    if (object != null && object.get(0) != null) {
                        final String objectId = object.get(0).getObjectId();
                        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
                        query.getObject(objectId, new QueryListener<UserInfo>() {

                            @Override
                            public void done(UserInfo object, BmobException e) {
                                if(e==null && object != null){
                                    if (object.getUser_pwd().equals(psw)){
                                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_ID, objectId);
                                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_NAME, accout);
                                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_NICK, accout);
                                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_PWD, psw);
                                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_IMAGE, object.getUser_image());
                                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_SEX, object.getUser_sex());
                                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_QQ, object.getUser_qq());
                                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_MAIL, object.getUser_mail());

                                        ToastUtils.showToast("登录成功");
                                        SharedPreferencesUtil.getInstance().putBoolean("login_state", true);
                                        dissLoading();
                                        MainActivity.start(LoginActivity.this);
                                        finish();
                                    }else {
                                        ToastUtils.showToast("登录失败:密码错误");
                                        dissLoading();
                                    }
                                }else{
                                    ToastUtils.showToast("登录失败：" + e.getMessage());
                                    dissLoading();
                                }
                            }

                        });
                    } else {
                        LogUtil.e("~~~~~~失败：" + e.getMessage() + "," + e.getErrorCode());
                        dissLoading();
                    }
                }else {
                    LogUtil.e("~~~~~~~~~失败：" + e.getMessage() + "," + e.getErrorCode());
                    dissLoading();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        bind.unbind();
        dissLoading();
        super.onDestroy();
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            if (requestCode == 200) {


            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            if (requestCode == 200) {

            }

        }
    };
}

