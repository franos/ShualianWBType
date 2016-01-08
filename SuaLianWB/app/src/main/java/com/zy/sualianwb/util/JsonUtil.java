package com.zy.sualianwb.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zz on 15/12/24.
 */
public class JsonUtil {
    public <T> List<T> parseJsonArray(String string, Class<T> clazz) {
        if (string == null) {
            return null;
        }

        List<T> ts = null;
        try {
            ts = new ArrayList<T>();
            Gson gson = new Gson();
            JsonElement jEle = new JsonParser().parse(string);
            JsonArray jsonArray = jEle.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                T fromJson = gson.fromJson(jsonObject, clazz);
                ts.add(fromJson);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Log.e("JSON", "" + exception.getMessage());
            return null;
        }
        return ts;
    }

}
