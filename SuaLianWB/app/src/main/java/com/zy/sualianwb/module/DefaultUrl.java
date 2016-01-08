package com.zy.sualianwb.module;

/**
 * Created by zz on 15/12/24.
 */
public class DefaultUrl {
    private long id;
    private String src;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DefaultUrl{" +
                "id=" + id +
                ", src='" + src + '\'' +
                '}';
    }
}
