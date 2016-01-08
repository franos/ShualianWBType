package com.zy.sualianwb.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zy.sualianwb.R;
import com.zy.sualianwb.util.SystemDateTime;

import java.io.IOException;

public class TestDate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_date);

    }

    public void testDateclick(View view) {
        try {
            SystemDateTime.setDateTime(2005, 5, 5, 5, 5);
            Toast.makeText(TestDate.this, "修改成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(TestDate.this, "修改失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (InterruptedException e) {
            Toast.makeText(TestDate.this, "修改失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

}
