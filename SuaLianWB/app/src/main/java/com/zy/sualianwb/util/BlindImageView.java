package com.zy.sualianwb.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zy.sualianwb.BigImageView;

/**
 * Created by zz on 15/12/25.
 */
public class BlindImageView extends FrameLayout {

    private static final String TAG = "BlindImageView";
    BigImageView imageView;
    BlindLayout layout;

    public BlindImageView(Context context) {
        super(context);
        init();
    }

    public BlindImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlindImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        post(new Runnable() {
            @Override
            public void run() {
                imageView = (BigImageView) getChildAt(0);
                layout = (BlindLayout) getChildAt(1);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayout(layout);
            }
        });

    }

    public BlindLayout getLayout() {
        return layout;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setLayoutBlack(final boolean isBlack) {
        post(new Runnable() {
            @Override
            public void run() {
                layout.setClose(isBlack);
            }
        });

    }

    public void setBitmap(final int id, final Bitmap bm) {
        Log.i(TAG, "setBitmap  pos=" + id + "");
        imageView.post(new Runnable() {
            @Override
            public void run() {
                imageView.setBitmap(id, bm);

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
