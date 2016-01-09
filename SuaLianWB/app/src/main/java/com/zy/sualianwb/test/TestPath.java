package com.zy.sualianwb.test;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.zy.sualianwb.R;
import com.zy.sualianwb.base.BaseActivity;

import java.io.File;

/**
 * Created by apple on 16/1/9.
 */
public class TestPath extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_path);
    }

    public void testPath(View v) {
        String cachePath = Environment.getExternalStorageDirectory() + "/EasyShare/image";
        File file = new File(cachePath);
        String[] list = file.list();
        for (String name : list) {
            Log.i("file", "" + name);

        }
    }
}
