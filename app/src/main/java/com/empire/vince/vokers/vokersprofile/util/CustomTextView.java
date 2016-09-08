package com.empire.vince.vokers.vokersprofile.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class CustomTextView extends TextView {

    FontSettings fontSettings;

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public CustomTextView(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context) {
        fontSettings = new FontSettings(context);
        this.setTypeface(fontSettings.getRobotoLightFont());
    }
}

