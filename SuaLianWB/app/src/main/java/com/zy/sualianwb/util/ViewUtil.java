package com.zy.sualianwb.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zz on 15/12/24.
 */
public class ViewUtil {

    public static Toast showSingleToast(Context context, String text) {
        if (overallToast == null) {
            overallToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            overallToast.setText(text);
            overallToast.show();
        } else {
            overallToast.cancel();
            overallToast = null;
            showSingleToast(context, text);
        }
        return overallToast;
    }

    public static Toast showSingleToastLong(Context context, String text) {
        if (overallToast == null) {
            overallToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            overallToast.setText(text);
            overallToast.show();
        } else {
            overallToast.cancel();
            overallToast = null;
            showSingleToast(context, text);
        }
        return overallToast;
    }

    private static Toast overallToast;// 全局的toast

}
