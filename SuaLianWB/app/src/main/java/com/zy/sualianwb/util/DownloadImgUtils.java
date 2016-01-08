package com.zy.sualianwb.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.widget.ImageView;

import com.zy.sualianwb.Constants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * http://blog.csdn.net/lmj623565791/article/details/41874561
 *
 * @author zhy
 */
public class DownloadImgUtils {
    public static final int BUFFER_SIZE = 512;
    private static final String TAG = "DownLoadUtils";

    public static boolean downloadImageByUrl(String urlStr, File file, boolean isOverWrite, Context context) {
        Log.i(TAG, "download url" + urlStr);
        if (file.exists() && (!isOverWrite)) {
            Log.i(TAG, "文件已存在    跳过");
            return true;
        }
        ShareUitl.writeString(Constants.SHARE_PREF_LAST_FILE, "file_path", file.getAbsolutePath(), context);
        Log.i(TAG, "download url start");
        FileOutputStream fos = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            is = conn.getInputStream();
            fos = new FileOutputStream(file);
            byte[] buf = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            //有下载失败的文件，下载成功标志设置为false
            Constants.isAllDownloadOk = false;
            //删除下载失误的文件
            if (null != file && file.exists()) {
                L.e(TAG, "delete files");
                boolean delete = file.delete();
                L.e(TAG, "delete result " + delete);
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                is = null;
            } catch (IOException e) {
            }

            try {
                if (fos != null) {
                    fos.close();
                }
                fos = null;
            } catch (IOException e) {
            }
            if (conn != null) {
                conn.disconnect();
            }
            conn = null;

        }
        return false;

    }

    /**
     * 根据url下载图片在指定的文件
     *
     * @param urlStr
     * @param file
     * @return
     */
    public static boolean downloadImgByUrl(String urlStr, File file, Context context) {
        return downloadImageByUrl(urlStr, file, false, context);
    }

    /**
     * 根据url下载图片在指定的文件
     *
     * @param urlStr
     * @param imageview
     * @return
     */
    public static Bitmap downloadImgByUrl(String urlStr, ImageView imageview) {
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(conn.getInputStream());
            is.mark(is.available());

            Options opts = new Options();
            opts.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);

            //获取imageview想要显示的宽和高
            ImageSizeUtil.ImageSize imageViewSize = ImageSizeUtil.getImageViewSize(imageview);
            opts.inSampleSize = ImageSizeUtil.caculateInSampleSize(opts,
                    imageViewSize.width, imageViewSize.height);

            opts.inJustDecodeBounds = false;
            is.reset();
            bitmap = BitmapFactory.decodeStream(is, null, opts);

            conn.disconnect();
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }

            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }

        return null;

    }

}
