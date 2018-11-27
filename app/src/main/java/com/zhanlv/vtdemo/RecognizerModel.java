package com.zhanlv.vtdemo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import java.io.File;

/**
 * Created by jarly on 2018/11/27.
 */

public class RecognizerModel {

  public static final String TYPE_FILE = "file";
  public static final String TYPE_SPEECH = "speech";
  public static final String TAG = "Recognizer";
  private SpeechRecognizer mRecognizer;
  private Context mContext;
  private StringBuilder mSpeechResult;
  private int ret;
  private MainView mView;
  private String mType;

  public RecognizerModel(MainView view) {
    mView = view;
  }

  public void setupSpeechRecognizer(Context context) {
    mRecognizer = SpeechRecognizer.createRecognizer(context, mInitListener);
    mContext = context;
    mSpeechResult = new StringBuilder();
  }

  /***
   * 设置参数
   * @param vadEos 设置语音后端点
   * @param asrPtt 设置标点符号
   * @param asrPath 保存语音文件路径
   */
  public void setParam(String vadEos, String asrPtt, String asrPath) {
    if (mRecognizer == null) {
      throw new NullPointerException("mRecognizer can't be null");
    }
    mRecognizer.setParameter(SpeechConstant.PARAMS, null);
    // 设置引擎
    mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
    // 设置返回结果格式
    mRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");
    mRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
    // 设置语言区域
    mRecognizer.setParameter(SpeechConstant.ACCENT, "zh_cn");

    // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
    mRecognizer.setParameter(SpeechConstant.VAD_BOS, "4000");

    // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
    mRecognizer.setParameter(SpeechConstant.VAD_EOS, vadEos);

    // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
    mRecognizer.setParameter(SpeechConstant.ASR_PTT, asrPtt);

    mRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
    //是否保存识别语音文件
    mRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, asrPath);

    //设置音频资源,通过写音频流方式(-1),如果通过Android自带的录音机录制音频方式(注释掉这一行)

    if (TYPE_FILE.equals(mType)) {
      mRecognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
    }
  }

  //设置参数完成后,开始识别
  public void startRecognizer(File asrFile) {
    ret = mRecognizer.startListening(mRecognizerListener);

    if (ret != ErrorCode.SUCCESS) {
      mView.showTip("听写失败,错误码：" + ret);
      return;
    }

    if (asrFile == null) {
      return;
    }
    //assets目录下获取语音文件
    //final byte[] audioData = FileUtil.readAudioFile(mContext, "vttest.wav");
    //通过sdcard获取语音文件
    final byte[] audioData = FileUtil.readFile(asrFile);
    if (audioData != null) {
      mRecognizer.writeAudio(audioData, 0, audioData.length);
      mRecognizer.stopListening();
    } else {
      mRecognizer.cancel();
      Log.e(TAG, "read audiorecord file failed!");
    }
  }

  private InitListener mInitListener = code -> {
    if (code != ErrorCode.SUCCESS) {
      mView.showTip("初始化失败，错误码：" + code);
    }
  };

  private RecognizerListener mRecognizerListener = new RecognizerListener() {

    @Override public void onBeginOfSpeech() {
      mView.showTip("开始说话");
    }

    @Override public void onEndOfSpeech() {
      mView.showTip("结束说话");
    }

    @Override public void onResult(RecognizerResult results, boolean isLast) {

      final String result = JsonParser.parseIatResult(results.getResultString());
      mSpeechResult.append(result);
      mView.setRecognizerResultText(mSpeechResult.toString());
      if (isLast) {
        mSpeechResult.delete(0, mSpeechResult.length());
        mView.setEnable(true);
      }

    }

    @Override public void onError(SpeechError error) {
      mView.showTip(error.getPlainDescription(true));
    }

    @Override public void onVolumeChanged(int volume, byte[] data) {
      mView.showTip("当前正在说话，音量大小：" + volume);
    }

    @Override public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

    }
  };

  public void setType(String type) {
    mType = type;
  }
}
