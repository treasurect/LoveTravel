package com.treasure.lovetravel.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.treasure.lovetravel.R;
import com.treasure.lovetravel.bean.UserInfo;
import com.treasure.lovetravel.helper.Contacts;
import com.treasure.lovetravel.listener.GenderChangeListener;
import com.treasure.lovetravel.listener.PhotoListener;
import com.treasure.lovetravel.ui.views.EditorDialog;
import com.treasure.lovetravel.ui.views.GenderChangeDialog;
import com.treasure.lovetravel.ui.views.PhotoDialog;
import com.treasure.lovetravel.utils.FileUtils;
import com.treasure.lovetravel.utils.GlideUtils;
import com.treasure.lovetravel.utils.ImageUtils;
import com.treasure.lovetravel.utils.LogUtil;
import com.treasure.lovetravel.utils.SharedPreferencesUtil;
import com.treasure.lovetravel.utils.StringUtils;
import com.treasure.lovetravel.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 18410 on 2017/8/24.
 */

public class PersonalInfoActivity extends BaseActivity implements PhotoListener, GenderChangeListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, PersonalInfoActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.name)
    TextView nameed;
    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.qq)
    TextView qqView;
    @BindView(R.id.email)
    TextView emailView;

    private final File PHOTO_DIR = new File(Contacts.PHOTO_DIR);
    private File mCacheFile;//裁剪路径
    private String key;
    private int changesex = -1;
    private GenderChangeDialog dialog;
    private EditorDialog editorDialog;
    private String nick;
    private String avatar;
    private String qq;
    private String email;
    private Intent intent;
    private PhotoDialog photoDialog;
    private File cameraFile;//相机路径
    private int state = 0;
    private String objectId;

    @Override
    protected void loadContentLayout() {
        setContentView(R.layout.activity_personal_info);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onResume() {
        objectId = SharedPreferencesUtil.getInstance().getString(Contacts.USER_ID);
        nick = SharedPreferencesUtil.getInstance().getString(Contacts.USER_NICK);
        avatar = SharedPreferencesUtil.getInstance().getString(Contacts.USER_IMAGE);
        qq = SharedPreferencesUtil.getInstance().getString(Contacts.USER_QQ);
        email = SharedPreferencesUtil.getInstance().getString(Contacts.USER_MAIL);


        qqView.setText(qq);
        emailView.setText(email);

        int gender = Integer.parseInt(SharedPreferencesUtil.getInstance().getString(Contacts.USER_SEX));
        changeSex(gender);
        nameed.setText(nick);
        GlideUtils.loadRoundImg(this, avatar, head, R.mipmap.ic_travel, 3);
        super.onResume();

    }

    @OnClick({R.id.back, R.id.headlayout, R.id.sex_layout,
            R.id.namelayout, R.id.qqlayout, R.id.emaillayout})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.headlayout:
                if (photoDialog == null) {
                    photoDialog = new PhotoDialog(this);
                    photoDialog.setLisener(this);
                }
                photoDialog.show();

                break;
            case R.id.sex_layout:
                if (dialog == null) {
                    dialog = new GenderChangeDialog(this);
                    dialog.setListener(this);

                }
                dialog.show();
                break;
            case R.id.namelayout:
                if (editorDialog == null) {
                    editorDialog = new EditorDialog(this);
                    editorDialog.setListener(this);

                }
                editorDialog.show();
                if (nameed != null) {
                    editorDialog.setEditorText(nameed.getText().toString().trim());
                }
                state = 0;
                break;
            case R.id.qqlayout:
                if (editorDialog == null) {
                    editorDialog = new EditorDialog(this);
                    editorDialog.setListener(this);

                }
                editorDialog.show();
                if (qqView != null) {
                    editorDialog.setEditorText(qqView.getText().toString().trim());
                }
                state = 1;
                break;
            case R.id.emaillayout:
                if (editorDialog == null) {
                    editorDialog = new EditorDialog(this);
                    editorDialog.setListener(this);

                }
                editorDialog.show();
                if (emailView != null) {
                    editorDialog.setEditorText(emailView.getText().toString().trim());
                }
                state = 2;
                break;

        }
    }

    private void changeSex(int changesex) {
        if (changesex == 1) {
            sex.setTextColor(getResources().getColor(R.color.black_2d));
            sex.setText("男");
        } else if (changesex == 2) {
            sex.setTextColor(getResources().getColor(R.color.black_2d));
            sex.setText("女");
        } else if (changesex == 3) {
            sex.setTextColor(getResources().getColor(R.color.black_2d));
            sex.setText("保密");
        } else {
            sex.setTextColor(getResources().getColor(R.color.black_2d));
            sex.setText("未设置");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void gerderClick(int id) {
        UserInfo userInfo;
        switch (id) {
            case R.id.man:
                changesex = 1;
                SharedPreferencesUtil.getInstance().putString(Contacts.USER_SEX, "1");
                changeSex(changesex);
                if (dialog != null) {
                    dialog.dismiss();
                }
                userInfo = new UserInfo();
                userInfo.setUser_sex("1");
                userInfo.update(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ToastUtils.showToast(getString(R.string.revise_success));
                            dissLoading();
                        } else {
                            ToastUtils.showToast(getString(R.string.revise_failed) + e.getMessage());
                            dissLoading();
                        }
                    }
                });
                break;
            case R.id.women:
                changesex = 2;
                SharedPreferencesUtil.getInstance().putString(Contacts.USER_SEX, "2");
                changeSex(changesex);
                if (dialog != null) {
                    dialog.dismiss();
                }
                userInfo = new UserInfo();
                userInfo.setUser_sex("2");
                userInfo.update(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ToastUtils.showToast(getString(R.string.revise_success));
                            dissLoading();
                        } else {
                            ToastUtils.showToast(getString(R.string.revise_failed) + "：" + e.getMessage());
                            dissLoading();
                        }
                    }
                });
                break;
            case R.id.unkonow:
                changesex = 3;
                SharedPreferencesUtil.getInstance().putString(Contacts.USER_SEX, "3");
                changeSex(changesex);
                if (dialog != null) {
                    dialog.dismiss();
                }
                userInfo = new UserInfo();
                userInfo.setUser_sex("3");
                userInfo.update(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ToastUtils.showToast(getString(R.string.revise_success));
                            dissLoading();
                        } else {
                            ToastUtils.showToast(getString(R.string.revise_failed) + "：" + e.getMessage());
                            dissLoading();
                        }
                    }
                });
                break;
            case R.id.cancle:
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            case R.id.cancel:
                if (editorDialog != null) {
                    editorDialog.dismiss();
                }
                break;
            case R.id.done:
                if (editorDialog != null) {
                    String editorText = editorDialog.getEditorText();
                    if (StringUtils.isEmpty(editorText)) {
                        return;
                    }
                    if (state == 0) {
                        nameed.setText(editorText);
                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_NICK, editorText);
                        userInfo = new UserInfo();
                        userInfo.setUser_nick(editorText);
                        userInfo.update(objectId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtils.showToast(getString(R.string.revise_success));
                                    dissLoading();
                                } else {
                                    ToastUtils.showToast(getString(R.string.revise_failed) + "：" + e.getMessage());
                                    dissLoading();
                                }
                            }
                        });
                    } else if (state == 1) {
                        qqView.setText(editorText);
                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_QQ, editorText);
                        userInfo = new UserInfo();
                        userInfo.setUser_qq(editorText);
                        userInfo.update(objectId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtils.showToast(getString(R.string.revise_success));
                                    dissLoading();
                                } else {
                                    ToastUtils.showToast(getString(R.string.revise_failed) + "：" + e.getMessage());
                                    dissLoading();
                                }
                            }
                        });
                    } else if (state == 2) {
                        emailView.setText(editorText);
                        SharedPreferencesUtil.getInstance().putString(Contacts.USER_MAIL, editorText);
                        userInfo = new UserInfo();
                        userInfo.setUser_mail(editorText);
                        userInfo.update(objectId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtils.showToast(getString(R.string.revise_success));
                                    dissLoading();
                                } else {
                                    ToastUtils.showToast(getString(R.string.revise_failed) + "：" + e.getMessage());
                                    dissLoading();
                                }
                            }
                        });
                    }
                    editorDialog.dismiss();
                }
                break;
        }
    }

    @Override
    public void camera() {
        AndPermission.with(this)
                .requestCode(300)
                .permission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(listener)
                .start();

    }

    @Override
    public void album() {
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(listener)
                .start();
    }

    /**
     * 开始拍照
     */
    private void startcamera() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            if (!PHOTO_DIR.exists()) {
                PHOTO_DIR.mkdirs();// 创建照片的存储目录
            }
            //魅族note2  data返回为null
            cameraFile = ImageUtils.takephoto(PersonalInfoActivity.this, 2003);
        } else {
            Toast.makeText(this, "没有SD卡", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        String fileName = System.currentTimeMillis() + ".jpg";
        if (PHOTO_DIR.exists() && PHOTO_DIR.isFile()) {
            PHOTO_DIR.delete();
        }
        if (!PHOTO_DIR.exists()) {
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
        }
        mCacheFile = new File(PHOTO_DIR, fileName);
        try {
            mCacheFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri outputUri = Uri.fromFile(new File(mCacheFile.getPath()));
        String url = FileUtils.getPath(this, uri);

        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri imageUri = FileProvider.getUriForFile(this, "com.treasure.lovetravel", new File(url));//通过FileProvider创建一个content类型的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra("noFaceDetection", true);//去除默认的人脸识别，否则和剪裁匡重叠
            intent.setDataAndType(imageUri, "image/*");
            //19=<sdk<24
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            //sdk<19
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 20);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 20);// x:y=1:2
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("output", outputUri);
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        startActivityForResult(intent, 20002);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            if (requestCode == 300) {
                if (AndPermission.hasPermission(PersonalInfoActivity.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    startcamera();
                } else {
                    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                    ToastUtils.showToast("权限被拒绝，请开通后使用");
                    // 建议：自定义这个Dialog，提示具体需要开启什么权限，自定义Dialog具体实现上面有示例代码。
                }

            } else if (requestCode == 200) {
                if (AndPermission.hasPermission(PersonalInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //startcamera();
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 20001);
                } else {
                    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                    ToastUtils.showToast("权限被拒绝，请开通后使用");
                    // 建议：自定义这个Dialog，提示具体需要开启什么权限，自定义Dialog具体实现上面有示例代码。
                }
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            if (requestCode == 300) {
                if (AndPermission.hasPermission(PersonalInfoActivity.this, Manifest.permission.CAMERA)) {
                    startcamera();
                } else {
                    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                    ToastUtils.showToast("权限被拒绝，请开通后使用");
                    // 建议：自定义这个Dialog，提示具体需要开启什么权限，自定义Dialog具体实现上面有示例代码。
                }
            } else if (requestCode == 200) {
                if (AndPermission.hasPermission(PersonalInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //startcamera();
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 20001);
                } else {
                    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                    ToastUtils.showToast("权限被拒绝，请开通后使用");
                    // 建议：自定义这个Dialog，提示具体需要开启什么权限，自定义Dialog具体实现上面有示例代码。
                }
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 20001:
                    Uri uri1 = data.getData();
                    String path = FileUtils.getPath(this, uri1);
                    File file = new File(path);
                    startPhotoZoom(Uri.fromFile(file));
                    break;
                case 20002:
                    if (mCacheFile.exists()) {
                        showLoading();
                        final String absolutePath = mCacheFile.getAbsolutePath();
                        //文件上传
                        final BmobFile bmobFile = new BmobFile(new File(absolutePath));
                        bmobFile.uploadblock(new UploadFileListener() {

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //源文件删除
                                    BmobFile file = new BmobFile();
                                    file.setUrl(avatar);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
                                    file.delete(new UpdateListener() {

                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                LogUtil.e("~~~~~~~~~~~~~~源文件删除成功");
                                            }
                                        }
                                    });
                                    //更新  本地、接口
                                    SharedPreferencesUtil.getInstance().putString(Contacts.USER_IMAGE, bmobFile.getFileUrl());
                                    GlideUtils.loadRoundImg(PersonalInfoActivity.this, absolutePath, head, R.mipmap.ic_travel, 3);
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.setUser_image(bmobFile.getFileUrl());
                                    userInfo.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                ToastUtils.showToast(getString(R.string.revise_success));
                                                dissLoading();
                                            } else {
                                                ToastUtils.showToast(getString(R.string.revise_failed) + "：" + e.getMessage());
                                                dissLoading();
                                            }
                                        }
                                    });
                                } else {
                                    ToastUtils.showToast("上传文件失败：" + e.getMessage());
                                }

                            }

                            @Override
                            public void onProgress(Integer value) {
                                // 返回的上传进度（百分比）
                            }
                        });
                    } else {
                        LogUtil.e("裁剪返回图片不存在");
                    }
                    break;
                case 2003:
                    if (cameraFile != null && cameraFile.exists()) {
                        startPhotoZoom(Uri.fromFile(cameraFile));
                    }
                    break;
            }
        }
    }
}
