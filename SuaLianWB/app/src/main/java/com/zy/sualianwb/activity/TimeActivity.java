package com.zy.sualianwb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zy.sualianwb.Constants;
import com.zy.sualianwb.R;
import com.zy.sualianwb.base.BaseActivity;
import com.zy.sualianwb.module.CrtTime;
import com.zy.sualianwb.util.HttpUtil;
import com.zy.sualianwb.util.TimeUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zz on 15/12/30.
 */
public class TimeActivity extends BaseActivity {
    TextView localdate_uncalibrate, beijin_time, net_delay, beijin_cut_local, calibrate_local_date, beijin_time_millis;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_layout);
        localdate_uncalibrate = (TextView) findViewById(R.id.localdate_uncalibrate);
        beijin_time = (TextView) findViewById(R.id.beijin_time);
        net_delay = (TextView) findViewById(R.id.net_delay);
        beijin_cut_local = (TextView) findViewById(R.id.beijin_cut_local);
        calibrate_local_date = (TextView) findViewById(R.id.calibrate_local_date);
        beijin_time_millis = (TextView) findViewById(R.id.beijin_time_millis);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String s = null;
                try {
                    TimeUtil.startRecode();
                    s = HttpUtil.get(Constants.CHECK_TIME);
                    CrtTime crtTime = new Gson().fromJson(s, CrtTime.class);
                    mHandler.obtainMessage(1, crtTime).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CrtTime time = (CrtTime) msg.obj;
            long stopDelay = TimeUtil.stop() / 2;
            Timestamp beiJinTimeStamp = new Timestamp(time.getBjtime());

            Timestamp localTimeStamp = new Timestamp(System.currentTimeMillis());

            //此时的北京时间
            long trueBeijinTime = beiJinTimeStamp.getTime() + stopDelay;
            //此时的本地时间
            long trueLocalTimeStamp = localTimeStamp.getTime() - stopDelay;
            //相隔时间
            long diff = trueBeijinTime - trueLocalTimeStamp;


//            localdate_uncalibrate.setText("本地时间-未校准 " + localTimeStamp.toString());
//            beijin_time.setText("北京时间 " + beiJinTimeStamp.toString());
//            net_delay.setText(new Timestamp(stopDelay).toString());
//            Timestamp cutStamp = new Timestamp(beiJinTimeStamp.getTime() - localTimeStamp.getTime());
//            beijin_cut_local.setText("北京时间-本地时间 " + cutStamp.toString());

            calibrate_local_date.setText("偏差时间" + diff);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp currTimeStamp = new Timestamp(currentTimeMillis);

            beijin_cut_local.setText("矫正后的时间 " + (currTimeStamp.getTime() + diff));
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String format1 = format.format(new Date((currTimeStamp.getTime() + diff)));
            localdate_uncalibrate.setText("矫正后的时间 " + format1);
            String format2 = format.format(trueBeijinTime);
            beijin_time.setText("获得的北京时间 " + format2);
            beijin_time_millis.setText("此时的北京时间millis" + trueBeijinTime);

            String format3 = format.format(currentTimeMillis);
            net_delay.setText("未校正的本地时间" + format3);


        }
    };


}
