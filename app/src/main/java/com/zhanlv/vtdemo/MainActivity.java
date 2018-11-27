package com.zhanlv.vtdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainView {

  private TextView mResultTv;
  private RecognizerModel mRecognizerModel;
  private String mAsrPath;
  private View mRecognizerfileBtn;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    requestPermissions();
    mResultTv = findViewById(R.id.result_tv);
    mRecognizerfileBtn = findViewById(R.id.file_btn);
    mRecognizerfileBtn.setOnClickListener(this);
    findViewById(R.id.speech_btn).setOnClickListener(this);
    //语音文件路径
    mAsrPath = Environment.getExternalStorageDirectory() + "/vttest.wav";
    mRecognizerModel = new RecognizerModel(this);
    mRecognizerModel.setupSpeechRecognizer(this);
  }

  @Override public void onClick(View v) {

    final int id = v.getId();
    switch (id) {
      case R.id.file_btn:
        mRecognizerModel.setType(RecognizerModel.TYPE_FILE);
        mRecognizerfileBtn.setEnabled(false);
        mRecognizerModel.setParam("4000", "0", "");
        mRecognizerModel.startRecognizer(new File(mAsrPath));
        break;
      case R.id.speech_btn:
        mRecognizerModel.setType(RecognizerModel.TYPE_SPEECH);
        mRecognizerModel.setParam("3000", "1", "");
        mRecognizerModel.startRecognizer(null);
        break;
      default:
        break;
    }
  }

  private void requestPermissions() {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        int permission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this, new String[] {
              Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.LOCATION_HARDWARE,
              Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_SETTINGS,
              Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,
              Manifest.permission.READ_CONTACTS
          }, 0x0010);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override public void setRecognizerResultText(String resultText) {
    mResultTv.setText(resultText);
  }

  @Override public void showTip(String tip) {
    ToastUtil.showTip(this, tip);
  }

  @Override public void setEnable(boolean enable) {
    mRecognizerfileBtn.setEnabled(enable);
  }
}
