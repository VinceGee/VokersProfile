package com.empire.vince.vokers.vokersprofile.fab;

import android.content.Context;
import android.os.Build;

/**
 * Created by VinceGee on 09/07/2016.
 */
final class Util {
    private Util() {
    }

    static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
