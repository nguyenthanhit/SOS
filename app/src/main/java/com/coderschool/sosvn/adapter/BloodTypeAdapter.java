package com.coderschool.sosvn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coderschool.sosvn.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 7/29/2017.
 */

public class BloodTypeAdapter extends RecyclerView.Adapter<BloodTypeAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> mBloodList;
    private Listener listener;

    public BloodTypeAdapter(Context context) {
        mContext = context;
        mBloodList = new ArrayList<>();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setData(List<String> list) {
        mBloodList.clear();
        mBloodList = list;

        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_blood, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String blood = mBloodList.get(position);
        holder.tvBloodType.setText(blood);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(blood);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBloodList.size();
    }

    public interface Listener {
        void onItemClick(String blood);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_blood)
        TextView tvBloodType;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

