package com.keegan.experiment.utilities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.keegan.experiment.R;

/**
 * Created by keegan on 27/01/16.
 */
public class TextToSpanFormat {
    private static Context mContext;

    public static SpannableStringBuilder formatThis(Context mContext, String string, String type) {
        TextToSpanFormat.mContext = mContext;
        final SpannableStringBuilder sb = new SpannableStringBuilder(string);

        int titleColour = ContextCompat.getColor(mContext, R.color.white);
        int subTitleColour = ContextCompat.getColor(mContext, R.color.white);
        int textColour = ContextCompat.getColor(mContext, R.color.white);

        ForegroundColorSpan fcs;
        StyleSpan bss;

        switch (type) {
            case "Title":
                fcs = new ForegroundColorSpan(titleColour);
                bss = new StyleSpan(Typeface.BOLD);
                break;
            case "SubTitle":
                fcs = new ForegroundColorSpan(subTitleColour);
                bss = new StyleSpan(Typeface.NORMAL);
                break;
            case "Content":
                fcs = new ForegroundColorSpan(textColour);
                bss = new StyleSpan(Typeface.ITALIC);
                break;
            default: //text
                fcs = new ForegroundColorSpan(textColour);
                bss = new StyleSpan(Typeface.NORMAL);
                break;
        }

        sb.setSpan(fcs, 0, string.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, 0, string.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return sb;
    }

}
