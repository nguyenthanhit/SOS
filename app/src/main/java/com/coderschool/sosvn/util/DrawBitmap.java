package com.coderschool.sosvn.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.coderschool.sosvn.R;

public class DrawBitmap extends FrameLayout {
    private static Context context;
    private Bitmap mutableBitmap;
    private Paint paint;
    private BitmapFactory.Options myOptions;
    private static DrawBitmap ourInstance;

    private DrawBitmap(Context context) {
        super(context);
        this.context = context;
        initDrawBitmap();
    }

    public static DrawBitmap getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DrawBitmap(context);
        }
        return ourInstance;
    }

    private void initDrawBitmap() {
        myOptions = new BitmapFactory.Options();
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.RGB_565;// important

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.human_body_man_filled, myOptions);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    public void drawCircleAt(float posX, float posY) {
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(posX, posY, 50, paint);
    }

    public Bitmap takeScreenShot(LinearLayout v) {
        v.setDrawingCacheEnabled(true);
        v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false); // clear drawing cache

        return b;
    }

    public void resetBitmap(ImageView imageView) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.human_body_man_filled, myOptions);
        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        imageView.setImageBitmap(bitmap);
    }

    public Bitmap getMutableBitmap() {
        return mutableBitmap;
    }
}
