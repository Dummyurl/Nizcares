package com.indglobal.nizcare.commons.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by readyassist on 2/5/18.
 */

public class CheckRegular extends AppCompatCheckBox {
    public CheckRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf"));
    }

}