package com.coderschool.sosvn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.object.Country;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryAdapter  extends RecyclerView.Adapter<CountryAdapter.MyHolder>{

    private Context mContext;
    private List<Country> mCountryList;
    private Listener mListener;

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public CountryAdapter(Context context) {
        mContext = context;
        mCountryList = new ArrayList<>();
    }

    public void setData(List<Country> list) {
        mCountryList.clear();
        mCountryList = list;
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_country, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final Country country = mCountryList.get(position);

        holder.flag.setImageResource(country.getIdFlag());
        holder.nameCountry.setText(country.getName() + " ("+country.getPhoneCode()+")");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(country);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCountryList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_flag)
        ImageView flag;

        @BindView(R.id.tv_name_country)
        TextView nameCountry;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface Listener {
        void onItemClick(Country country);
    }
}
