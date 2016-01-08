package com.zy.sualianwb;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.zy.sualianwb.util.BitmapUtil;
import com.zy.sualianwb.util.BlindLayout;
import com.zy.sualianwb.util.DensityUtil;
import com.zy.sualianwb.util.L;

/**
 * Created by zz on 15/12/23.
 */
public class BigImageView extends ImageView {
    String TAG = "BigImageView";
    private Bitmap bm;
    private int id = 0;
    private BlindLayout layout;
    private Status mCurrStatus = Status.LEFT;

    private enum Status {
        RIGHT, LEFT, CENTER
    }


    public BigImageView(Context context) {
        super(context);
    }

    public BigImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BigImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void postID(int id) {
        this.id = id;
        int tempId = id;
        int posX = DensityUtil.getCurrLie(tempId);
        int posY = DensityUtil.getCurrHang(tempId);
        //从0开始
        L.i(TAG, "行" + posY + "  列" + posX);
        int truePosHang = posY + 1;
        int truePosLie = posX + 1;
        L.i(TAG, "真实行" + truePosHang + "  真实列" + truePosLie);

        //判断列的位置
        //中间的位置 列
        int centerL = Constants.X_NUM / 2 + 1;
        if (truePosLie < centerL) {
            //在左边
            mCurrStatus = Status.LEFT;
        } else if (truePosLie > centerL) {
            //在右边
            mCurrStatus = Status.RIGHT;
        } else {
            //中间
            mCurrStatus = Status.CENTER;
        }
        Log.i(TAG, "currStuts:" + mCurrStatus);
        //直接旋转+90°
        rotateBitmap();
        //计算真实行列公式: (NUM-(H-1))*L+(L-1)*(H-1)
        int NUM = Constants.X_NUM;
        int H = truePosHang;
        int L = truePosLie;
        //真实位置POS,减1是为了下标一致
        int POS = (NUM - (H - 1)) * L + (L - 1) * (H - 1) - 1;
        if (POS < 0) {
            Log.w(TAG, "POS<0 POS=:" + POS);
            POS = 0;
        }
        Log.i(TAG, "调整后的位置POS:" + POS);
        //调整行数从下标为0开始
        int currH = DensityUtil.getCurrHang(POS);
        int currLie = DensityUtil.getCurrLie(POS);
        Log.i(TAG, "当前行:" + currH + " 当前列:" + currLie);

        cropBitmap(currLie, currH);
    }

    private void rotateBitmap() {
        bm = BitmapUtil.adjustPhotoRotation(bm, 90);
    }


    public int getId() {
        L.i(TAG, "id " + id);
        return id;
    }

    /**
     * @param id 下标从1开始
     * @param bm
     */
    public void setBitmap(int id, Bitmap bm) {
        L.i(TAG, "setBitmap  id=" + id + "  bm=" + bm);
        //防止下标越界
        id -= 1;
        if (id < 0) {
            id = 0;
        }

        this.bm = bm;
        postID(id);
    }

    private void cropBitmap(int posX, int posY) {

        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();

        int cropBmWidth = bmWidth / Constants.X_NUM;
        int cropBmHeight = bmHeight / Constants.Y_NUM;

        int x = posX * cropBmWidth;
        int y = posY * cropBmHeight;

        Bitmap cropBitmap = null;
        try {
            cropBitmap = Bitmap.createBitmap(bm, x, y, cropBmWidth, cropBmHeight);
        } catch (Exception e) {
            cropBitmap = Bitmap.createBitmap(bm, x, y - bmHeight, cropBmWidth, cropBmHeight);
        }

        Bitmap checkUpBitmap = checkDirect(cropBitmap);
//        Bitmap checkUpBitmap = checkDirect(bm);
        startBlindAnim(checkUpBitmap);


    }

    /**
     * 根据位置确定方向，
     *
     * @param cropBitmap
     * @return
     */
    private Bitmap checkDirect(Bitmap cropBitmap) {
        switch (mCurrStatus) {
            case LEFT:
                return BitmapUtil.adjustPhotoRotation(cropBitmap, 180);
            case RIGHT:
                break;
            case CENTER:
//                return BitmapUtil.adjustPhotoRotation(cropBitmap, 180);
                break;
        }


        return cropBitmap;
    }

    private void startBlindAnim(final Bitmap cropBitmap) {
//        layout.setBackgroundColor(Color.parseColor("#00000000"));
//        setImageBitmap(cropBitmap);
        Log.i(TAG, "开始播放遮住动画");
        layout.setOnAnimEnd(new BlindLayout.OnAnimEnd() {
            @Override
            public void onOpenAnimEnd() {

            }

            @Override
            public void onCloseAnimEnd() {
                setImageBitmap(cropBitmap);
                Log.i(TAG, "开始播放打开动画");
                layout.setClose(false);
            }
        });
        layout.setClose(true);

    }

    public void setLayout(BlindLayout layout) {
        this.layout = layout;
    }
}
