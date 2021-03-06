package com.can.mvp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by can on 2018/4/9.
 * 图片工具类
 */

public class BitmapUtils {

    public static String PATH = "LoveLy";

    //保存图片到系统相册
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        boolean isSuccess = false;
        if(bmp==null){
            ToastUtils.getInstance(context).showText("图片为空，无法保存");
            return isSuccess;
        }
        String storePath = "";
        // 首先保存图片
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            storePath  = Environment.getExternalStorageDirectory() + File.separator + PATH;
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
        }
        if(StringUtils.isEmpty(storePath)){
            ToastUtils.getInstance(context).showText("无法保存照片,请检查SD卡是否可用");
        }else {
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(storePath, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                //通过io流的方式来压缩保存图片
                isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
                //把文件插入到系统图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                if (isSuccess) {
                    ToastUtils.getInstance(context).showText("保存图片成功");
                } else {
                    ToastUtils.getInstance(context).showText("无法保存照片,请检查SD卡是否可用");
                }
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtils.getInstance(context).showText("保存失败");
            }
        }
        return isSuccess;
    }

}
