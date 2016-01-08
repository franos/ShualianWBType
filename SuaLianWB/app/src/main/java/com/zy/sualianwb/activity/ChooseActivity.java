package com.zy.sualianwb.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.Toast;

import com.zy.sualianwb.ChooseAdapter;
import com.zy.sualianwb.Constants;
import com.zy.sualianwb.R;
import com.zy.sualianwb.base.BaseActivity;
import com.zy.sualianwb.util.ShareUitl;

/**
 * Created by zz on 15/12/24.
 */
public class ChooseActivity extends BaseActivity {
    private RecyclerView recy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_choose);
        recy = (RecyclerView) findViewById(R.id.choose_recy);
        ChooseAdapter adapter = new ChooseAdapter(this);
        recy.setLayoutManager(new GridLayoutManager(this, Constants.Y_NUM, GridLayoutManager.VERTICAL, false));
        recy.setItemAnimator(new DefaultItemAnimator());
        recy.setAdapter(adapter);

        adapter.setOnItemClick(new ChooseAdapter.OnItemClick() {
            @Override
            public void onClick(int pos, String text) {
                Constants.currPos = Integer.parseInt(text);
                Toast.makeText(ChooseActivity.this, "您选择了"+ Constants.currPos, Toast.LENGTH_SHORT).show();
                save(Constants.currPos);
                finish();
            }
        });

    }
    private void save(int currpos) {
        ShareUitl.writeString(Constants.SHARE_PREF, "currpos", String.valueOf(currpos), this);
    }

}
