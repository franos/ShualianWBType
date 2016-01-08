package com.zy.sualianwb.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zy.sualianwb.R;
import com.zy.sualianwb.module.ImagesUrl;
import com.zy.sualianwb.module.ShowTime3;
import com.zy.sualianwb.util.DownLoadUtil;
import com.zy.sualianwb.util.L;
import com.zy.sualianwb.util.Translate;

import java.util.List;

/**
 * Created by zz on 15/12/25.
 */
public class TestNetInterface extends AppCompatActivity {
    private DownLoadUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_interface);

        util = new DownLoadUtil();
        util.setOnDataGet(new DownLoadUtil.OnDataGet() {
            @Override
            public void onUrlsGet(String json) {

                ImagesUrl imagesUrl = Translate.translateUrl(json);
                L.i("testInterface", "url--json" + json);
                L.i("testInterface", "图片url转换结果:" + imagesUrl);
            }

            @Override
            public void onShowTimeGet(String json) {
                L.i("testInterface", "showTime--json" + json);
                List<ShowTime3> showTime3s = Translate.translateShowTime3(json);
                L.i("testInterface", "图片显示时间转换结果:" + showTime3s);
            }

            @Override
            public void onUrlsGetFail(String msg) {

            }

            @Override
            public void onShowTimeGetFail(String msg) {

            }
        });
    }

    public void testShowTime(View view) {

        util.getShowTime();


    }

    public void testUrl(View view) {

        util.getUrls();
    }

}
