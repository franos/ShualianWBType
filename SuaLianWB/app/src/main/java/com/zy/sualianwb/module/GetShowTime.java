package com.zy.sualianwb.module;

public class GetShowTime {
    /**
     * uid : xxx.jpg
     * showtime : Dec 24…..
     */
    private long id;
    private String uid;
    private String showtime;
    private String type;
    private long showtimestamp;



    public long getShowtimestamp() {
        return showtimestamp;
    }

    public void setShowtimestamp(long showtimestamp) {
        this.showtimestamp = showtimestamp;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    @Override
    public String toString() {
        return "GetShowTime{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", showtime='" + showtime + '\'' +
                ", type='" + type + '\'' +
                ", showtimestamp=" + showtimestamp +
                '}';
    }
}
