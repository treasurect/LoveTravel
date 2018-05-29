package com.treasure.lovetravel.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.treasure.lovetravel.R;
import com.treasure.lovetravel.bean.UserInfo;
import com.treasure.lovetravel.utils.LogUtil;
import com.treasure.lovetravel.utils.StringUtils;
import com.treasure.lovetravel.utils.ToastUtils;
import com.treasure.lovetravel.utils.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by 18410 on 2017/7/31.
 */

public class ResetPassActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, ResetPassActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.phonenum)
    EditText phonenum;
    @BindView(R.id.passwd)
    EditText passwd;
    @BindView(R.id.passwd_re)
    EditText passwdRe;
    @BindView(R.id.vecode)
    EditText vecode;
    @BindView(R.id.getvecode)
    TextView getvecode;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.pass_layout)
    LinearLayout passLayout;

    MyCountDownTimer mc;
    private String str;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    startTimer();
                    passLayout.setVisibility(View.VISIBLE);
                    dissLoading();
                    break;
                case 201:
                    String phone = phonenum.getText().toString().trim();
                    String pwd = passwd.getText().toString().trim();
                    register(phone, pwd);
                    break;
                case 400:
                    ToastUtils.showToast("发送验证码失败");
                    dissLoading();
                    break;
                case 401:
                    ToastUtils.showToast("验证码输入错误");
                    dissLoading();
                    break;
            }
        }
    };

    @Override
    protected void loadContentLayout() {
        setContentView(R.layout.activity_reset_pass);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {
        title.setText(R.string.resetpsw);
        str = getResources().getString(R.string.timer);
        mc = new MyCountDownTimer(60 * 1000, 1000);
    }

    @OnClick({R.id.back, R.id.getvecode, R.id.next, R.id.root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.getvecode:
                String phoneNum = phonenum.getText().toString();
                if (StringUtils.isBlank(phoneNum)) {
                    ToastUtils.showSingleToast(getString(R.string.phonecantnull));
                    return;
                } else {
                    Pattern compile = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");
                    Matcher matcher = compile.matcher(phoneNum);
                    //正则匹配手机号  匹配后发送验证码
                    if (matcher.matches()) {
                        showLoading();
                        sendVecode(phoneNum);
                    } else {
                        ToastUtils.showSingleToast("请输入正确手机号");
                    }
                }
                break;
            case R.id.next:
                String phone = phonenum.getText().toString().trim();
                String psw = passwd.getText().toString().trim();
                String psw_re = passwdRe.getText().toString().trim();
                String code = vecode.getText().toString().trim();
                if (StringUtils.isBlank(psw)) {
                    ToastUtils.showSingleToast("密码不能为空");
                    return;
                } else if (StringUtils.isBlank(code)) {
                    ToastUtils.showSingleToast("请填写验证码");
                    return;
                } else if (psw.length() < 6) {
                    ToastUtils.showSingleToast("密码长度必须大于等于6");
                    return;
                } else if (!psw.equals(psw_re)) {
                    ToastUtils.showToast("输入的密码不一致");
                    return;
                } else {
                    //注册时先提交验证码  检验成功后进行注册
                    submitCode(phone, psw, code);
                    showLoading();
                }
                break;
            case R.id.root:
                Utils.hideSoftInput(this, root);
                break;
        }
    }

    //更新
    private void register(final String phone, final String pwd) {
        if (mc != null) {
            mc.onFinish();
            mc.cancel();
        }
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo("user_name", phone);
        query.setLimit(1);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> object, BmobException e) {
                if (e == null) {
                    if (object != null && object.get(0) != null) {
                        String objectId = object.get(0).getObjectId();
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUser_name(phone);
                        userInfo.setUser_nick(phone);
                        userInfo.setUser_pwd(pwd);
                        userInfo.update(objectId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtils.showToast("修改成功，请重新登录");
                                    dissLoading();
                                    LoginActivity.start(ResetPassActivity.this);
                                    finish();
                                } else {
                                    ToastUtils.showToast("修改失败：" + e.getMessage());
                                    dissLoading();
                                }
                            }
                        });
                    } else {
                        dissLoading();
                        LogUtil.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            }
        });
    }

    //提交验证码
    public void submitCode(final String phone, final String pwd, String code) {
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    handler.sendEmptyMessage(201);
                } else {
                    handler.sendEmptyMessage(401);
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode("86", phone, code);
    }

    //发送验证码
    public void sendVecode(String phone) {
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    handler.sendEmptyMessage(200);
                } else {
                    handler.sendEmptyMessage(400);
                }

            }
        });
        SMSSDK.getVerificationCode("86", phone);
    }

    //开启计时器
    private void startTimer() {
        mc.start();
    }


    @Override
    protected void onDestroy() {
        if (mc != null) {
            mc.onFinish();
            mc.cancel();
        }

        super.onDestroy();
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            getvecode.setTextColor(getResources().getColor(
                    R.color.blue_87b0ff));
            getvecode.setText(R.string.getcode);
            getvecode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (null != getvecode) {
                getvecode.setTextColor(getResources().getColor(R.color.gray_8c));
                getvecode.setText(String.format(str,
                        millisUntilFinished / 1000));
                getvecode.setClickable(false);
            }
        }
    }
}
