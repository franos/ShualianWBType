package com.zy.sualianwb.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.zy.sualianwb.BigImageView;
import com.zy.sualianwb.Constants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 自定义的有关位图的工具类
 *
 * @author zZhen
 */
public class BitmapUtil {
    public final static String PNG = "png";
    public final static String JPG = "jpg";
    private static String TAG = "BitmapUtil";
    private Context mContext;
    // private static BitmapUtils bitmapUtils;


    public BitmapUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 根据图片需要显示的宽和高对图片进行压缩
     *
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static Bitmap decodeSampledBitmapFromPath(String path, int width,
                                                     int height) {
        // 获得图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,
                width, height);

        // 使用获得到的InSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        Log.i(TAG, "decode file path=" + path);
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    public static Bitmap decodeSampledBtmapFromPath(String path, ImageSizeUtil.ImageSize size) {
        return decodeSampledBitmapFromPath(path, size.width, size.height);
    }

    /**
     * 高斯模糊
     *
     * @return
     */
    public static Bitmap blurBitmap(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    public static void saveBitmapFile(String path) throws IOException {
        Bitmap bm = BitmapFactory.decodeFile(path);
        saveBitmap(bm, new File(path));
    }

    /**
     * 将Base64格式的字符串转换成Bitmap
     *
     * @param result
     * @return
     */
    public static Bitmap getBitmapFromBase64String(String result) {
        Bitmap bitmap = null;
        byte[] bitmapArray;
        bitmapArray = Base64.decode(result, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        return bitmap;
    }


    public static void saveBitmap(Bitmap bm, File f) throws IOException {
        File file = new File(f.getPath());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.PNG, 80, out);
        out.flush();
        out.close();
    }

    public static void saveBitmap(Bitmap bm, Uri uri) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile().getPath() + "Justan/");
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.PNG, 80, out);
        out.flush();
        out.close();
    }


    /**
     * <br/>取得bitmap25^2范围内的颜色取反，并设置到textView上<br/>
     * <br/>如果出现任何错误，颜色将设置为alpha 255 red 100 green 100 blue 100(默认)<br/>
     *
     * @param bm
     * @param targetTextV
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setTextDefaultColor(Bitmap bm, TextView targetTextV) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            targetTextV.setTextColor(Color.argb(255, 100, 100, 100));
            return;
        }
        try {
            int scope = 10;
            int[] pixels = new int[bm.getWidth() * bm.getHeight()];
            bm.getPixels(pixels, 0, bm.getWidth(), bm.getWidth() / 2 - scope, bm.getHeight() / 2 - scope, bm.getWidth() / 2 + scope, bm.getHeight() / 2 + scope);
            int colorPixels = pixels[0];
            int argb = Color.alpha(colorPixels);
            int red = Color.red(colorPixels);
            int green = Color.green(colorPixels);
            int blue = Color.blue(colorPixels);
            targetTextV.setTextColor(Color.argb(argb, 255 - red, 255 - green, 255 - blue));
        } catch (Exception e) {
            e.printStackTrace();
            targetTextV.setTextColor(Color.argb(255, 100, 100, 100));
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable, int defWidth, int defHeight) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if (w <= 0 || h <= 0) {
            w = defWidth;
            h = defHeight;
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }


    public static void disPlayImageView(final ImageView imageview, Bitmap bm) {
        imageview.setImageBitmap(bm);
//        Animation alphaAnim = ViewUtil.getAlphaAnim();
//        imageview.startAnimation(alphaAnim);
    }

    /**
     * 图片缓存的核心对象
     */
    private LruCache<String, Bitmap> mLruCache = new LruCache<>(100);

    /**
     * 根据path在缓存中获取bitmap
     *
     * @param key
     * @return
     */
    private Bitmap getBitmapFromLruCache(String key) {
        return mLruCache.get(key);
    }

    private void refreashBitmap(final String path, final ImageView imageView,
                                Bitmap bm) {
        Message message = Message.obtain();
        ImgBeanHolder holder = new ImgBeanHolder();
        holder.bitmap = bm;
        holder.path = path;
        holder.imageView = imageView;
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    /**
     * UI线程中的Handler
     */
    private Handler mUIHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 获取得到图片，为imageview回调设置图片
            ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
            Bitmap bm = holder.bitmap;
            ImageView imageview = holder.imageView;
            String path = holder.path;
            // 将path与getTag存储路径进行比较
            if (imageview.getTag().toString().equals(path)) {
                if (null != bm) {
                    if (onImageBitmapGet != null) {
                        onImageBitmapGet.get(bm);
                    }
                } else {
                    if (onImageBitmapGet != null) {
                        L.e(TAG, "该url" + holder.path + "获取到的图片为空" + "");
//                        getBitmap(imageview, StorageUtil.randomDefUrl(mContext).getSrc());
                    }
                }
            }
        }
    };

    private class ImgBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    /**
     * 获得缓存图片的地址
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = StorageUtil.getCachePath(context);
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 利用签名辅助类，将字符串字节数组
     *
     * @param str
     * @return
     */
    public String md5(String str) {
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            digest = md.digest(str.getBytes());
            return bytes2hex02(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方式二
     *
     * @param bytes
     * @return
     */
    public String bytes2hex02(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1)// 每个字节8为，转为16进制标志，2个16进制位
            {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }
        return sb.toString();

    }

    private Bitmap loadImageFromLocal(final String path,
                                      final ImageView imageView) {
        Bitmap bm;
        // 加载图片
        // 图片的压缩
        // 1、获得图片需要显示的大小
        ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
        // 2、压缩图片
        bm = BitmapUtil.decodeSampledBitmapFromPath(path, imageSize.width,
                imageSize.height);
        return bm;
    }

    public synchronized void getBitmap(final ImageView imageView, final String path) {
        imageView.setTag(path);
        new Thread() {
            @Override
            public void run() {
                // 根据path在缓存中获取bitmap
                Bitmap bm = getBitmapFromLruCache(path);
                if (bm != null) {
                    refreashBitmap(path, imageView, bm);
                    return;
                }
                File file = getDiskCacheDir(imageView.getContext(), md5(path));
                Log.e(TAG, "start find image :" + file + " in disk cache .");
                if (file.exists())// 如果在缓存文件中发现
                {
                    Log.e(TAG, "find image :" + path + " in disk cache .");
                    bm = loadImageFromLocal(file.getAbsolutePath(), imageView);
                    Log.e(TAG, "find bm:" + bm + " in disk cache .");
                    if (null == bm) {
                        Log.w(TAG, "disk bm is null");
                        boolean downloadState = DownloadImgUtils.downloadImageByUrl(path, file, true, mContext);
                        if (downloadState)// 如果下载成功
                        {
                            Log.e(TAG, "download image :" + path + " to disk cache . path is " + file.getAbsolutePath());
                            bm = loadImageFromLocal(file.getAbsolutePath(), imageView);
                        } else {
                            Log.e(TAG, "download image " + path + " fail");
                        }
                    }
                } else {

                    boolean downloadState = DownloadImgUtils.downloadImageByUrl(path, file, true, mContext);
                    if (downloadState)// 如果下载成功
                    {
                        Log.e(TAG, "download image :" + path + " to disk cache . path is " + file.getAbsolutePath());
                        bm = loadImageFromLocal(file.getAbsolutePath(), imageView);
                    }
                }
                addBitmapToLruCache(path, bm);
                Log.i(TAG, "" + bm);
                refreashBitmap(path, imageView, bm);
            }
        }.start();
    }

    public Bitmap getTrueBitmapFromNet(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        InputStream is = null;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        is = conn.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;

    }

    private Bitmap cropBitmap(Bitmap bm, BigImageView imageView) {
        int tempId = imageView.getId() + 1;
        int posX = tempId % Constants.X_NUM;//得到第几列
        int posY = tempId / Constants.Y_NUM;//得到第几行
        Bitmap cropBitmap = Bitmap.createBitmap(bm, posX, posY, posX + imageView.getMeasuredWidth(), posY + imageView.getMeasuredHeight());
        return cropBitmap;
    }

    /**
     * 将图片加入LruCache
     *
     * @param path
     * @param bm
     */
    protected void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null) {
            if (bm != null)
                mLruCache.put(path, bm);
        }
    }

    public OnImageBitmapGet onImageBitmapGet;

    public void setOnImageBitmapGet(OnImageBitmapGet onImageBitmapGet) {
        this.onImageBitmapGet = onImageBitmapGet;
    }

    public interface OnImageBitmapGet {
        void get(Bitmap bm);
    }

    public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
            L.e(TAG, "OutOfMemoryError" + ex);
        }
        return null;
    }
//
//    public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
//
//        Matrix m = new Matrix();
//        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
//        float targetX, targetY;
//        if (orientationDegree == 90) {
//            targetX = bm.getHeight();
//            targetY = 0;
//        } else {
//            targetX = bm.getHeight();
//            targetY = bm.getWidth();
//        }
//
//        final float[] values = new float[9];
//        //将矩阵的内容拷贝到values中
//        m.getValues(values);
//
//        float x1 = values[Matrix.MTRANS_X];
//        float y1 = values[Matrix.MTRANS_Y];
//
//        m.postTranslate(targetX - x1, targetY - y1);
//
//        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
//        Paint paint = new Paint();
//        Canvas canvas = new Canvas(bm1);
//        canvas.drawBitmap(bm, m, paint);
//
//        return bm1;
//    }

}

