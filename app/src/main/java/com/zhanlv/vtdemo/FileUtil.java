package com.zhanlv.vtdemo;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {


  /**
   * 读取asset目录下音频文件。
   *
   * @return 二进制文件数据
   */
  public static byte[] readAudioFile(Context context, String filename) {
    try {
      InputStream ins = context.getAssets().open(filename);
      byte[] data = new byte[ins.available()];

      ins.read(data);
      ins.close();

      return data;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;
  }

  public static byte[] readFile(File file) {
    FileInputStream fis = null;
    byte[] bytes = null;
    try {
      fis = new FileInputStream(file);
      bytes = new byte[fis.available()];
      fis.read(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return bytes;
  }

}
