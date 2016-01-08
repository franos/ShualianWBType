package com.zy.sualianwb.module;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by zz on 15/12/24.
 */
public class ShowTime implements Serializable{

    private long id;

    private Map<String, Date> showTime;

    public Map<String, Date> getShowTime() {
        return showTime;
    }

    public void setShowTime(Map<String, Date> showTime) {
        this.showTime = showTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ShowTime{" +
                "showTime=" + showTime +
                '}';
    }
}
