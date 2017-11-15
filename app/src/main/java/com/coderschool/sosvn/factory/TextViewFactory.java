package com.coderschool.sosvn.factory;

import android.content.Context;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class TextViewFactory implements  ViewSwitcher.ViewFactory {

    @StyleRes
    final int styleId;
    final boolean center;
    private Context mContext;

    public TextViewFactory(@StyleRes int styleId, boolean center, Context context) {
        this.styleId = styleId;
        this.center = center;
        mContext = context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View makeView() {
        final TextView textView = new TextView(mContext);

        if (center) {
            textView.setGravity(Gravity.CENTER);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            textView.setTextAppearance(mContext, styleId);
        } else {
            textView.setTextAppearance(styleId);
        }

        return textView;
    }

}