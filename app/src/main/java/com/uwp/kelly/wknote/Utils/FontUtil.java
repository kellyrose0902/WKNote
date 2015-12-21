package com.uwp.kelly.wknote.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kelly on 12/16/2015.
 */
public class FontUtil {

    private static final Map<String,Typeface> FONTS = new HashMap<String,Typeface>();

    public static Typeface getTypeface(Context context, String typefaceName){
        Typeface typeface = FONTS.get(typefaceName);
        if(typeface == null){
            typeface = Typeface.createFromAsset(context.getAssets(),"fonts/"+typefaceName);
            FONTS.put(typefaceName,typeface);
        }
        return typeface;
    }

    public static void  setTypeFace(EditText v, String typefaceName){
        v.setTypeface(getTypeface(v.getContext(),typefaceName));
    }

    public static void  setTextTypeFace(TextView v, String typefaceName){
        v.setTypeface(getTypeface(v.getContext(),typefaceName));
    }

}
