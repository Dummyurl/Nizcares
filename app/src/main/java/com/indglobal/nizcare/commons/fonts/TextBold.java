package com.indglobal.nizcare.commons.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by readyassist on 12/12/17.
 */

public class TextBold extends TextView {
    public TextBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf"));
    }
}