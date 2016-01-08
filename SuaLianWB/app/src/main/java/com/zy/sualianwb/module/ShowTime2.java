package com.zy.sualianwb.module;

import java.util.Date;

/**
 * Created by zz on 15/12/24.
 * 发过来的数据
 */
public class ShowTime2 {
    /**
     * uid : xxx.jpg
     * showtime : Dec 24…..
     */
    private long id;
    private String uid;
    private Date showtime;
    private long timeStamp;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Date getShowtime() {
        return showtime;
    }

    public void setShowtime(Date showtime) {
        this.showtime = showtime;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
