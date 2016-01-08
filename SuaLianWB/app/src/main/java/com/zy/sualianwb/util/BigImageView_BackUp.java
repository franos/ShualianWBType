package com.zy.sualianwb.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zy.sualianwb.Constants;

/**
 * Created by zz on 15/12/23.
 */
public class BigImageView_BackUp extends ImageView {
    String TAG = "BigImageView";
    private Bitmap bm;
    private int id = 0;
    private BlindLayout layout;

    public BigImageView_BackUp(Context context) {
        super(context);
    }

    public BigImageView_BackUp(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BigImageView_BackUp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSrc(Bitmap bm) {
        this.bm = bm;
    }


    public void setID(int id) {
        this.id = id;
    }

    public void postID(int id) {
        this.id = id;
        int tempId = id;
        int posX = tempId % Constants.X_NUM;//得到第几列
        int posY = tempId / Constants.Y_NUM;//得到第几行
        //从0开始
        L.i(TAG, "行" + posY + "  列" + posX);
        int truePosHang = posY + 1;
        int truePosLie = posX + 1;
        L.i(TAG, "真实行" + truePosHang + "  真实列" + truePosLie);




        cropBitmap(posX, posY);
    }

    public int getId() {
        L.i(TAG, "id " + id);
        return id;
    }

    public void setBitmap(int id, Bitmap bm) {
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
        startBlindAnim(cropBitmap);


    }

    private void startBlindAnim(final Bitmap cropBitmap) {
//        layout.setBackgroundColor(Color.parseColor("#00000000"));
//        setImageBitmap(cropBitmap);
        layout.setOnAnimEnd(new BlindLayout.OnAnimEnd() {
            @Override
            public void onOpenAnimEnd() {

            }

            @Override
            public void onCloseAnimEnd() {
                setImageBitmap(cropBitmap);
                layout.setClose(false);
            }
        });
        layout.setClose(true);

    }

    public void setLayout(BlindLayout layout) {
        this.layout = layout;
    }
}
