package com.zy.sualianwb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by zz on 15/12/24.
 */
public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.MVholder> {

    private Context context;
    private int[] lis;

    public ChooseAdapter(Context context) {
        this.context = context;
        lis = initList();
    }

    public int[] initList() {
        lis = new int[Constants.X_NUM * Constants.Y_NUM];
        int temp = 1;
        for (int i = 0; i < Constants.X_NUM * Constants.Y_NUM; i++) {
            lis[i] = temp++;
        }
        return lis;
    }

    @Override
    public MVholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.choose_item, null);
        MVholder mVholder = new MVholder(view);
        return mVholder;
    }

    @Override
    public void onBindViewHolder(MVholder holder, int position) {
        holder.btn.setText("" + lis[position]);
    }

    @Override
    public int getItemCount() {
        return lis.length;
    }

    class MVholder extends RecyclerView.ViewHolder {
        Button btn;

        public MVholder(View itemView) {
            super(itemView);
            btn = (Button) itemView.findViewById(R.id.item_choose_btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.onClick(getAdapterPosition(), btn.getText().toString());
                    }
                }
            });

        }
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onClick(int pos, String text);
    }
}
