package com.coderschool.sosvn.card;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coderschool.sosvn.R;
import com.coderschool.sosvn.util.DecodeBitmapTask;

public class SliderCard extends RecyclerView.ViewHolder {

    private static int viewWidth = 0;
    private static int viewHeight = 0;

    private final ImageView imageView;

    private DecodeBitmapTask task;

    public SliderCard(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    void clearContent() {
        if (task != null) {
            task.cancel(true);
        }
    }

    private void loadBitmap(final String url, final Context context) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_hospital)
                .into(imageView);
    }

}