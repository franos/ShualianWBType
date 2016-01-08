package com.zy.sualianwb.util.tables;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zy.sualianwb.Constants;
import com.zy.sualianwb.R;

public class MatrixTableAdapter<T> extends BaseTableAdapter {

    private final static int WIDTH_DIP = 180;
    private final static int HEIGHT_DIP = 32;

    private final Context context;

    private T[][] table;

    private final int width;
    private final int height;

    public MatrixTableAdapter(Context context) {
        this(context, null);
    }

    public MatrixTableAdapter(Context context, T[][] table) {
        this.context = context;
        Resources r = context.getResources();

        width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DIP, r.getDisplayMetrics()));
        height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP, r.getDisplayMetrics()));

        setInformation(table);
    }

    public void setInformation(T[][] table) {
        this.table = table;
    }

    @Override
    public int getRowCount() {
        return table.length - 1;
    }

    @Override
    public int getColumnCount() {
        return table[0].length - 1;
    }

    @Override
    public View getView(final int row, final int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TextView(context);
            ((TextView) convertView).setGravity(Gravity.CENTER);
            convertView.setClickable(true);
        }
//		((TextView) convertView).setText(table[row + 1][column + 1].toString());
        final int currNum = ((row) * 17 + (column + 1));
        ((TextView) convertView).setText("" + (row + 1) + " 行 " + (column + 1) + "列 | 第 " + (currNum) + "个");

        if (row == -1 || column == -1) {
            ((TextView) convertView).setText(table[row + 1][column + 1].toString());
            ((TextView) convertView).setBackgroundColor(Color.parseColor("#006064"));
            ((TextView) convertView).setTextColor(Color.parseColor("#ffffff"));
            convertView.setClickable(false);
            convertView.setEnabled(false);
        } else {
            convertView.setClickable(true);
            convertView.setEnabled(true);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onTableClick!=null){
                        onTableClick.onClick(v,currNum,row+1,column+1);
                    }
                }
            });
            ((TextView) convertView).setBackgroundColor(context.getResources().getColor(R.color.text_color));
            ((TextView) convertView).setTextColor(Color.parseColor("#000000"));
            if (currNum == Constants.currPos) {
                ((TextView) convertView).setBackgroundColor(Color.parseColor("#006064"));
                ((TextView) convertView).setTextColor(Color.parseColor("#ffffff"));
            }
        }


        return convertView;
    }

    @Override
    public int getHeight(int row) {
        return height;
    }

    @Override
    public int getWidth(int column) {
        if (column == -1) {
            return width / 4;
        }
        return width;
    }

    @Override
    public int getItemViewType(int row, int column) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    OnTableClick onTableClick;

    public void setOnTableClick(OnTableClick onTableClick) {
        this.onTableClick = onTableClick;
    }

    public interface OnTableClick{
        void onClick(View currView,int currpos,int row,int column);
    }

}
