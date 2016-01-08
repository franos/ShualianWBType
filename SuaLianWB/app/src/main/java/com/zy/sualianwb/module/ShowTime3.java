package com.zy.sualianwb.module;

import com.zy.sualianwb.Constants;
import com.zy.sualianwb.util.L;
import com.zy.sualianwb.util.PosUrlUtil;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by zz on 15/12/24.
 */
public class ShowTime3 {

    /**
     * src : xxx.jpg
     * showtime : Dec 24…..
     * 转换后的数据
     */
    private long id;
    private String src;
    private Date showtime;
    private long delayMillis = 0;
    private long timeStamp;
    private String type;

    public ShowTime3() {
    }


    public ShowTime3(ShowTime2 time2) {
        this.id = time2.getId();
        String uid = time2.getUid();
        src = Constants.IMAGE_URL_HOST + PosUrlUtil.urlWidthPos(uid) + ".jpg";
//        src = uid;
        this.timeStamp = time2.getTimeStamp();
        showtime = time2.getShowtime();
        this.type = Constants.TEMPLATE_URL_HOST + time2.getType() + ".jpg";
        if (showtime == null) {
            showtime = new Date(DateTime.now().getMillis() + Constants.DEFAULT_PIC_DELAY);
        }
    }

    //获取时间,每次调用都会将当前时间与计划时间相比较，求出相隔的值
    public long getDelayMillis() {
        DateTime dtCurr = new DateTime(System.currentTimeMillis());
        long currentTimeMillis = dtCurr.getMillis();
        DateTime dt = new DateTime(showtime);
        DateTime dateTime = null;
        long millis = 0;
        boolean shouldPlus = shouldPlus(currentTimeMillis, dt);
        if (shouldPlus) {
            dateTime = dt.plusHours(12);
            millis = dateTime.getMillis();
        } else {
            millis = dt.getMillis();
        }


        long cut = millis - currentTimeMillis;

        int currPos = Constants.currPos;//当前位置
        int currY = (currPos - 1) % Constants.Y_NUM;//当前列

        delayMillis = (cut) + Constants.PIC_DIZENG_DELAY_TIME * currY;

        if (cut < Constants.ALLOW_PLAY) {
            delayMillis = Constants.DELAY_TIME_OVERLOAD_FLAG;
        }

        if (delayMillis != Constants.DELAY_TIME_OVERLOAD_FLAG) {
            delayMillis = cut;
        }
        return delayMillis;
    }

    public Date targetDate() {
        DateTime dtCurr = new DateTime(System.currentTimeMillis());
        long currentTimeMillis = dtCurr.getMillis();
        DateTime dt = new DateTime(showtime);
        DateTime dateTime = null;
        long millis = 0;
        boolean shouldPlus = shouldPlus(currentTimeMillis, dt);
        if (shouldPlus) {
            dateTime = dt.plusHours(12);
        } else {
            dateTime = dt;
        }
        Date targetDate = dateTime.toDate();
        return targetDate;
    }

    public Timestamp targetStamp() {
        Timestamp targetTimeStamp = new Timestamp(timeStamp);
        return targetTimeStamp;
    }


    private boolean shouldPlus(long currentTimeMillis, DateTime dt) {
        DateTime dtCurr = new DateTime(currentTimeMillis);
        int targetHourOfDay = dt.getHourOfDay();
        L.e("ShowTime3", "targetHour" + targetHourOfDay);
        int hourOfDay = dtCurr.getHourOfDay();
        L.e("ShowTime3", "currHour" + hourOfDay);
        if (hourOfDay > 12) {
            L.e("ShowTime3", "现在是下午");
            //如果已经是下午了
            if (targetHourOfDay < 12) {
                L.e("ShowTime3", "小于12");
                //还小于12--就该加上去
                return true;
            }//否则不用加
            return false;
        }
        return false;
    }

    private boolean isOver(DateTime dt, long currMillis) {
        return dt.isBefore(currMillis);
    }

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public Date getShowtime() {
        return showtime;
    }

    public void setShowtime(Date showtime) {
        this.showtime = showtime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ShowTime3{" +
                "id=" + id +
                ", src='" + src + '\'' +
                ", showtime=" + showtime +
                ", delayMillis=" + delayMillis +
                ", timeStamp=" + timeStamp +
                ", type='" + type + '\'' +
                '}';
    }
}
