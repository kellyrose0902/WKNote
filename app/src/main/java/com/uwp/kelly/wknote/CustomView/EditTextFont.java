package com.uwp.kelly.wknote.CustomView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.uwp.kelly.wknote.R;
import com.uwp.kelly.wknote.Utils.FontUtil;

/**
 * Created by Kelly on 12/16/2015.
 */
public class EditTextFont extends EditText {
    public EditTextFont(Context context) {
        super(context);
        init(context,null,0);
    }

    public EditTextFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public EditTextFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditTextFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


    }




    public void init(Context context, AttributeSet attrs, int defStyleAttr){
        if(attrs!=null){
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextFont,defStyleAttr,0);
            final String typefaceName = a.getString(R.styleable.EditTextFont_typeface);
            a.recycle();
            if (typefaceName!=null){
                setCustomTypeface(typefaceName);
            }
        }

    }

    public void setCustomTypeface(String typeFace){
        FontUtil.setTypeFace(this,typeFace);
    }

}
