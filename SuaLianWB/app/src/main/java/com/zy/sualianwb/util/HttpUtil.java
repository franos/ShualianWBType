package com.zy.sualianwb.util;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by zz on 15/12/24.
 */
public class HttpUtil {
    private static final String RESP_FAIL = "-1";
    private static OkHttpClient client;

    static {
        if (client == null) {
            synchronized (HttpUtil.class) {
                if (client == null) {
                    client = new OkHttpClient();
                    client.setConnectTimeout(5000, TimeUnit.SECONDS);
                    client.setReadTimeout(5000, TimeUnit.SECONDS);
                    client.setWriteTimeout(5000, TimeUnit.SECONDS);
                }
            }
        }
    }
    /**
     * @return
     */
    public synchronized static OkHttpClient getClient() {
        return client;
    }



    public static String get(String url) throws IOException {
        {
            OkHttpClient client = getClient();
            Request request = new Request.Builder().url(url).build();
            Response resp = client.newCall(request).execute();
            if (resp == null) {
                return RESP_FAIL;
            }
            if (resp.isSuccessful()) {
                return resp.body().string();
            } else {
                StringBuffer buffer = new StringBuffer();
                buffer.append(RESP_FAIL).append("返回码").append(resp.code()).append("信息").append(resp.message());
                return buffer.toString();
            }

        }

    }
}
