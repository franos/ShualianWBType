package com.zy.sualianwb.module;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zz on 15/12/24.
 */
public class ImagesUrl implements Serializable {
    private long id;

    private List<String> url;

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ImagesUrl{" +
                "url=" + url +
                '}';
    }
}
