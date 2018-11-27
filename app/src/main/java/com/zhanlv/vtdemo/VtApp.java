package com.zhanlv.vtdemo;

import android.app.Application;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by jarly on 2018/11/27.
 */

public class VtApp extends Application {
  @Override public void onCreate() {
    super.onCreate();
    //初始化sdk,"="后面替换自己在讯飞官网创建应用的AppId
    SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5bfce2e6");
  }
}
