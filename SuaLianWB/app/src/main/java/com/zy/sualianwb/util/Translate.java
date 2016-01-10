package com.zy.sualianwb.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zy.sualianwb.Constants;
import com.zy.sualianwb.module.GETPic;
import com.zy.sualianwb.module.GetShowTime;
import com.zy.sualianwb.module.GetsImageUrls;
import com.zy.sualianwb.module.ImagesUrl;
import com.zy.sualianwb.module.ShowTime2;
import com.zy.sualianwb.module.ShowTime3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by zz on 15/12/24.
 */
public class Translate {

    private static final String TAG = "Translate";

    public static ImagesUrl translateUrl(String json) {
        try {
            List<GETPic> getPics = new JsonUtil().parseJsonArray(json, GETPic.class);
            List<GetsImageUrls> getsImageUrlses = getPics(getPics);
            List<String> getUrls = getImageUrls(getsImageUrlses);
            Log.i("test_JSON", "" + getUrls);
            ImagesUrl urls = new ImagesUrl();
            urls.setUrl(getUrls);
            return urls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    private static List<GetsImageUrls> getPics(List<GETPic> getPics) {
        List<GetsImageUrls> getsImageUrlses = new ArrayList<>(getPics.size());
        for (int i = 0; i < getPics.size(); i++) {
            GetsImageUrls urls = new GetsImageUrls();
            urls.setId(String.valueOf(new Random().nextInt(6000)));
            urls.setSrc(Constants.IMAGE_URL_HOST + PosUrlUtil.urlWidthPos(getPics.get(i).getOpenid()) + ".jpg");
            Log.i(TAG, "url  = " + urls.getSrc() + " ");
            getsImageUrlses.add(urls);
        }
        return getsImageUrlses;
    }

    private static List<String> getImageUrls(List<GetsImageUrls> getsImageUrlses) {
        List<String> str = new ArrayList<>();
        for (GetsImageUrls urls : getsImageUrlses) {
            str.add(urls.getSrc());
        }
        return str;
    }


    public static List<ShowTime3> translateShowTime3(String json) {
        List<GetShowTime> getShowTimes = new JsonUtil().parseJsonArray(json, GetShowTime.class);
        List<ShowTime3> showTime3s = getShowTime3(getShowTimes);
        Log.i(TAG, "translate showtime result " + showTime3s);
        return showTime3s;
    }

    private static List<ShowTime3> getShowTime3(List<GetShowTime> getShowTimes) {
        List<ShowTime3> showTime3s = new ArrayList<>(getShowTimes.size());
        for (GetShowTime getShowTime : getShowTimes) {
            ShowTime2 showTime2 = new ShowTime2();
            String showtime = getShowTime.getShowtime();
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FOMAT);
            try {
                Date parse = dateFormat.parse(showtime);
                showTime2.setShowtime(parse);
                showTime2.setTimeStamp(getShowTime.getShowtimestamp());
                String type = getShowTime.getType();
                if (null == type) {
                    Log.w(TAG, "type is null");
                }
                showTime2.setType(type);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            showTime2.setUid(getShowTime.getUid());
            showTime2.setId(getShowTime.getId());
            ShowTime3 showTime3 = new ShowTime3(showTime2);
            showTime3s.add(showTime3);
        }

        return showTime3s;
    }


    public static List<ShowTime3> parseJsonArray(String string, Class<ShowTime2> clazz) {
        if (string == null) {
            return null;
        }
        List<ShowTime3> ts = null;
        try {
            ts = new ArrayList<ShowTime3>();
            Gson gson = new Gson();
            JsonElement jEle = new JsonParser().parse(string);
            JsonArray jsonArray = jEle.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                ShowTime2 fromJson = gson.fromJson(jsonObject, ShowTime2.class);
                ShowTime3 showTime3 = new ShowTime3(fromJson);
                ts.add(showTime3);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Log.e("JSON", "" + exception.getMessage());
            return null;
        }
        return ts;
    }

    // TODO: 16/1/8 模板图片json转换
    public static ImagesUrl translateTemplateUrl(String json) {
        ImagesUrl url = new ImagesUrl();
        List<GetsImageUrls> getsImageUrlses = new JsonUtil().parseJsonArray(json, GetsImageUrls.class);
        for (int i = 0; i < getsImageUrlses.size(); i++) {
            GetsImageUrls urls = getsImageUrlses.get(i);
            String trueSrc = Constants.IMAGE_URL_HOST + urls.getSrc() + ".jpg";
            urls.setSrc(trueSrc);
        }
        List<String> urls = new ArrayList<>(getsImageUrlses.size());
        for (int i = 0; i < getsImageUrlses.size(); i++) {
            urls.add(getsImageUrlses.get(i).getSrc());
        }
        url.setUrl(urls);
        return url;
    }
}
