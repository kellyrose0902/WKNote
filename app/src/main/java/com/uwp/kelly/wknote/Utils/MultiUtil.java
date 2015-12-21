package com.uwp.kelly.wknote.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;

import com.uwp.kelly.wknote.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kelly on 12/19/2015.
 */
public class MultiUtil {
    public static int getColorValue(Context context,String colorTheme){
        int color;
        if(colorTheme.compareTo("red")==0){
            color = ContextCompat.getColor(context, R.color.colorWKRed);
        }
        else if(colorTheme.compareTo("blue")==0){
            color = ContextCompat.getColor(context,R.color.colorWKBlue);
        }
        else if(colorTheme.compareTo("yellow")==0){
            color = ContextCompat.getColor(context,R.color.colorWKYellow);
        }
        else if(colorTheme.compareTo("orange")==0){
            color = ContextCompat.getColor(context,R.color.colorWKOrange);
        }
        else if(colorTheme.compareTo("green")==0){
            color = ContextCompat.getColor(context,R.color.colorWKGreen);
        }
        else{
            color = ContextCompat.getColor(context,R.color.colorWKViolet);
        }
        return color;
    }
    public static boolean hasValue(EditText editText){
        String value = editText.getText().toString();
        return (value.length()!=0);
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM - dd - yyyy hh:mm a");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
