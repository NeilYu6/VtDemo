package com.zhanlv.vtdemo;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by jarly on 2018/9/25.
 */

public class ToastUtil {

  private static Toast mToast;

  public static void showTip(Context context,String s) {
    if (mToast == null) {
      mToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);

      mToast.setGravity(Gravity.CENTER, 0, 0);
    } else {
      mToast.setText(s);
    }
    mToast.show();
  }

}
