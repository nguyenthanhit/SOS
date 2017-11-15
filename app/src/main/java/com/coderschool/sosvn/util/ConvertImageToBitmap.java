package com.coderschool.sosvn.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

public class ConvertImageToBitmap extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;
    private Bitmap image;
    private Context mContext;

    public ConvertImageToBitmap(Context context, ImageView imageView) {
        this.mContext = context;
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            image = null;
        }
        return image;
    }

    @SuppressLint("NewApi")
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            result = BlurImage.blur(mContext, result);
            imageView.setImageBitmap(result);
        }
    }
}