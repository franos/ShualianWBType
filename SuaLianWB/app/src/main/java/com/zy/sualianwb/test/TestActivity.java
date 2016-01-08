package com.zy.sualianwb.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zy.sualianwb.Constants;
import com.zy.sualianwb.R;
import com.zy.sualianwb.util.BitmapUtil;
import com.zy.sualianwb.util.BlindImageView;
import com.zy.sualianwb.util.L;

public class TestActivity extends Activity {
    private static final String TAG = "TestActivity";
    BlindImageView img;
    BitmapUtil bitmapUtil = new BitmapUtil(this);
    int currIndex = 0;
    private boolean isBlack;
    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        img = (BlindImageView) findViewById(R.id.test_img);
        bm = BitmapFactory.decodeResource(getResources(), R.mipmap.pic2);
    }

    public void next(View view) {
        currIndex++;
        if (currIndex > (Constants.X_NUM * Constants.X_NUM)) {
            currIndex = 0;
            Log.w(TAG, "currIndex >phone num");
        }
        L.i(TAG, "currIndex=" + currIndex);
        img.setBitmap(currIndex, bm);
    }
}
