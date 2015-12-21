package com.uwp.kelly.wknote.CustomView;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Kelly on 12/20/2015.
 */
public class CustomMovementMethod extends ArrowKeyMovementMethod {

    // The context we pass to the method

    private static CustomMovementMethod linkMovementMethod  = new CustomMovementMethod();

    public static MovementMethod getInstance(){

        // Return this movement method
        return linkMovementMethod;
    }

    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event){
        // Get the event action
        int action = event.getAction();

        // If action has finished
        if(action == MotionEvent.ACTION_UP) {
            // Locate the area that was pressed
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            x += widget.getScrollX();
            y += widget.getScrollY();

            // Locate the URL text
            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
            Log.e("CUSTOMMOVE","offset = "+off);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                }
                else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                }
                // If we're here, something's wrong
                return true;
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }
}