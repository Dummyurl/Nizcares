package com.indglobal.nizcare.commons.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by readyassist on 12/14/17.
 */

public class RadioRegular extends RadioButton {
    public RadioRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf"));
    }

}