package com.zy.sualianwb.test;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zy.sualianwb.Constants;
import com.zy.sualianwb.R;
import com.zy.sualianwb.util.ShareUitl;
import com.zy.sualianwb.util.tables.MatrixTableAdapter;
import com.zy.sualianwb.util.tables.TableFixHeaders;

public class TestTable extends AppCompatActivity {
    TableFixHeaders headers;
    static final String LIE = "列";
    static final String HANG = "行";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_table);
        headers = (TableFixHeaders) findViewById(R.id.m_table);
        headers.setClickable(true);
        headers.setEnabled(true);
        restorage();
        MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(this, new String[][]{
                {
                        "行/列",
                        "1" + LIE,
                        "2" + LIE,
                        "3" + LIE,
                        "4" + LIE,
                        "5" + LIE,
                        "6" + LIE,
                        "7" + LIE,
                        "8" + LIE,
                        "9" + LIE,
                        "10" + LIE,
                        "11" + LIE,
                        "12" + LIE,
                        "13" + LIE,
                        "14" + LIE,
                        "15" + LIE,
                        "16" + LIE,
                        "17" + LIE
                },
                {
                        "1" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",
                },
                {
                        "2" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",
                },
                {
                        "3" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",
                },
                {
                        "4" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",
                },
                {
                        "5" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",
                },
                {
                        "6" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",

                },
                {
                        "7" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",
                },
                {
                        "8" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",

                },

                {
                        "9" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",

                },
                {
                        "10" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",

                },
                {
                        "11" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",

                },
                {
                        "12" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",

                },
                {
                        "13" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",

                },
                {
                        "14" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",

                },
                {
                        "15" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",

                },
                {
                        "16" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",

                },
                {
                        "17" + HANG,
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13",
                        "14",
                        "15",
                        "16",
                        "17",

                },
        });
        matrixTableAdapter.setOnTableClick(new MatrixTableAdapter.OnTableClick() {

            @Override
            public void onClick(View currView, final int currpos, final int row, final int column) {
                Constants.currPos = currpos;
                save(currpos);
                Toast.makeText(TestTable.this, "您选择了 " + row + "行 " + column + "列" + " 第" + currpos + "个 ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        headers.setAdapter(matrixTableAdapter);
    }

    private void save(int currpos) {
        ShareUitl.writeString(Constants.SHARE_PREF, "currpos", String.valueOf(currpos), this);
    }

    private void restorage() {
        String currpos = ShareUitl.getString(Constants.SHARE_PREF, "currpos", this);
        if (currpos != null) {
            Constants.currPos = Integer.parseInt(currpos);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restorage();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        save(Constants.currPos);
    }
}
