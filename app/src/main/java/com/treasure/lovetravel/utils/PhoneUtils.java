package com.treasure.lovetravel.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.util.UUID;

/**
 * Created by 18410 on 2017/8/3.
 */

public class PhoneUtils {
//    //获得唯一id
//    public static String getDeviceID(Activity activity){
//        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
//
//                Build.BOARD.length()%10 +
//                Build.BRAND.length()%10 +
//                Build.CPU_ABI.length()%10 +
//                Build.DEVICE.length()%10 +
//                Build.DISPLAY.length()%10 +
//                Build.HOST.length()%10 +
//                Build.ID.length()%10 +
//                Build.MANUFACTURER.length()%10 +
//                Build.MODEL.length()%10 +
//                Build.PRODUCT.length()%10 +
//                Build.TAGS.length()%10 +
//                Build.TYPE.length()%10 +
//                Build.USER.length()%10 ; //13 digits
//
//        TelephonyManager TelephonyMgr = (TelephonyManager)activity.getSystemService(activity.TELEPHONY_SERVICE);
//        String szImei = TelephonyMgr.getDeviceId();
//        return m_szDevIDShort+szImei;
//    }

    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
    //厂商+手机型号
    public static String getDevice(){
       return Build.MANUFACTURER+"  "+ Build.MODEL;
    }

    //版本号
    public static String getVersionCode(Context context){
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String getAndroidVersion(){
       return "android  "+ Build.VERSION.SDK_INT;
    }

    //获取mac地址
    public static String getWLANMACAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        return m_szWLANMAC;
    }

    public static String getNetWorkStatus(Context context) {
        String netWorkType = "未知";

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();

            if (type == ConnectivityManager.TYPE_WIFI) {
                netWorkType = "wifi";
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = "移动数据";
            }
        }

        return netWorkType;
    }
}
