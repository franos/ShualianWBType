package com.zy.sualianwb.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.zy.sualianwb.R;
import com.zy.sualianwb.util.BlindImageView;

public class TestBlindActivity extends Activity {
    BlindImageView blindImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_blind);
        blindImageView = (BlindImageView) findViewById(R.id.test_blindimage);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.def2);
        blindImageView.post(new Runnable() {
            @Override
            public void run() {
                blindImageView.setBitmap(0, bitmap);
            }
        });

    }

    boolean isBlack = false;

    public void event(View view) {

        if (isBlack) {
            isBlack = false;
            blindImageView.setLayoutBlack(true);
        } else {
            isBlack = true;
            blindImageView.setLayoutBlack(false);
        }

    }
}
