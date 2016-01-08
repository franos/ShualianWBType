package com.zy.sualianwb.module;

/**
 * Created by zz on 15/12/25.
 */
public class GetsImageUrls {


    /**
     * id : 1
     * src : 123
     */

    private String id;
    private String src;


    public void setId(String id) {
        this.id = id;
    }


	@Override
	public String toString() {
		return "GetsImageUrls [id=" + id + ", src=" + src + "]";
	}

    public String getId() {
        return id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
