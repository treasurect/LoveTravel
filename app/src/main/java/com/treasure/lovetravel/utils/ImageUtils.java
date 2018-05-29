package com.treasure.lovetravel.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 18410 on 2017/7/28.
 */

public class ImageUtils {
    //打开相机 7.0适配
    public static File takephoto(Activity activity, int requestcode){
        String state = Environment.getExternalStorageState();
        File imgFile= null;

        if(state.equals(Environment.MEDIA_MOUNTED)){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String format = dateFormat.format(new Date());
            imgFile = new File(activity.getExternalCacheDir().getAbsolutePath(),"CarRental"+format+".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                Uri url = FileProvider.getUriForFile(activity, "com.treasure.lovetravel",imgFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, url);
            }else {
                //生成文件
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(imgFile));
            }

            activity.startActivityForResult(intent, requestcode);
        }else{
            //ToastUtil.showShort(activity, "内存卡不存在");
        }
        return imgFile;
    }

//    //非圆角
//    public static void loadImage(Context context, RequestManager glide,String url, ImageView view){
//        if (view!=null&& !((Activity) context).isFinishing())
//        glide.load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
//    }
//
//
//
//    //加载默认圆角
//    public static void loadDefaultHead(Context context, ImageView view, int def) {
//        Glide.with(context).load(def).transform(new GlideUtils.GlideCircleTransform(context)).into(view);
//    }
//
//    /**
//     * 设置图片圆角
//     *
//     * @param context
//     * @param url
//     * @param view
//     * @param defaultRes
//     */
////    public static void setImageRoundSyc(Context context, String url, ImageView view, int defaultRes,int dp) {
////        Glide.with(context).load(url).error(defaultRes).transform(new GlideUtils.GlideRoundTransform(context, dp)).into(view);
////    }
//
//    public static void setImageRoundSyc(Context context,RequestManager glide, String url, ImageView view, int defaultRes,int dp) {
//        if (view!=null&& !((Activity) context).isFinishing())
//        glide.load(url).diskCacheStrategy(DiskCacheStrategy.ALL).error(defaultRes).transform(new GlideUtils.GlideRoundTransform(context, dp)).into(view);
//    }
////    //加载圆形图片
////    public static void loadCircleImg(Context context, String url, ImageView view, int defaultRes){
////        Glide.with(context).load(url).error(defaultRes).transform(new GlideUtils.GlideCircleTransform(context)).into(view);
////    }
//
//
//    //加载圆形图片
//    public static void loadCircleImg(Context context,RequestManager glide, String url, ImageView view, int defaultRes){
//        if (view!=null&& !((Activity) context).isFinishing()){
//            glide.load(url).diskCacheStrategy(DiskCacheStrategy.ALL).error(defaultRes).transform(new GlideUtils.GlideCircleTransform(context)).into(view);
//        }
//    }

    public static int[] getImageWidthHeight(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth,options.outHeight};
    }

    public static boolean isLongImg(File file, Bitmap bitmap) {
        //TODO file.length()的判断，需要根据OS的版本号做动态调整大小
        if (file == null || file.length() == 0) {
            return false;
        }
        if (bitmap.getHeight() > bitmap.getWidth() * 3 || file.length() >= 0.5 * 1024 * 1024) {
            return true;
        }
        return false;
    }


}
