package com.treasure.lovetravel.helper;

import android.os.Environment;

/**
 * ============================================================
 * Copyright：treasure和他的朋友们有限公司版权所有 (c) 2018
 * Author：   treasure
 * time：2018/5/5
 * name:
 * ============================================================
 */

public class Contacts {
    //个人资料
    public static final String USER_ID= "user_id";
    public static final String USER_NAME= "user_name";
    public static final String USER_NICK= "user_nick";
    public static final String USER_PWD= "user_pwd";
    public static final String USER_IMAGE= "user_image";
    public static final String USER_SEX= "user_sex";
    public static final String USER_QQ= "user_qq";
    public static final String USER_MAIL= "user_mail";

    //训练计划
    public static final String PLAN_STEP_NUM = "plan_step_num";
    public static final String PLAN_REMIND_SWITCH = "plan_remind_switch";
    public static final String PLAN_REMIND_TIME = "plan_remind_time";
    //当天的信息 存储方式如下 ： date&num
    public static final String CUR_STEP_NUM = "cur_step_num";


    public static final String PHOTO_DIR = Environment.getExternalStorageDirectory() + "/CarRental";
    public static final String CACHE = "/CarRental";
}
