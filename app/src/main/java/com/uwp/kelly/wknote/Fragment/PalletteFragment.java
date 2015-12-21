package com.uwp.kelly.wknote.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.uwp.kelly.wknote.Activity.NoteList;
import com.uwp.kelly.wknote.CustomView.TextViewFont;
import com.uwp.kelly.wknote.R;

/**
 * Created by Kelly on 12/17/2015.
 */
public class PalletteFragment extends Fragment {

    public static String TAG = String.valueOf(PalletteFragment.class);
    ImageView palletteImage;
    Context context;
    TextViewFont textViewFont;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_note,container,false);

        textViewFont = (TextViewFont)rootView.findViewById(R.id.red_text);
        textViewFont.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(),"long click",Toast.LENGTH_LONG).show();
                return true;
            }
        });
        textViewFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "click", Toast.LENGTH_LONG).show();
            }
        });
        palletteImage = (ImageView)rootView.findViewById(R.id.pallette);


        palletteImage.setDrawingCacheEnabled(true);
        palletteImage.buildDrawingCache(true);
        context =getActivity();


                palletteImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                    Bitmap bmp = palletteImage.getDrawingCache();
                    if(bmp!=null){
                        decodeTouchedColor(v, event,bmp);
                    }



                return false;
            }
        });
        return rootView;
    }

    public void decodeTouchedColor(View v, MotionEvent event, Bitmap bmp){
        int x = Integer.valueOf((int) event.getX());
        int y = Integer.valueOf((int) event.getY());

        int touchedColor = bmp.getPixel(x, y);

        int r = Color.red(touchedColor);
        int g = Color.green(touchedColor);
        int b = Color.blue(touchedColor);

        //String hexColor = String.format("#%06X", (0xFFFFFF & touchedColor));
        //Toast.makeText(context,"r = "+r+", g = "+g+", b = "+b+".", Toast.LENGTH_SHORT).show();

        if(touchedColor == Color.WHITE){
            try {
                Intent intent = new Intent(context, NoteList.class);
                startActivity(intent);
            }
            catch (Exception e){

            }

        }
        else if (r > 220 && g < 100 && b >70){
            newNote("red");
            //Toast.makeText(context,"red", Toast.LENGTH_SHORT).show();

        }
        else if(r < 90 && r > 73 && b > 108 && b < 120  ){
            newNote("violet");
            //Toast.makeText(context,"violet", Toast.LENGTH_SHORT).show();

        }
        else if(r > 36 & r < 53 && g > 110 && g < 139){
            newNote("blue");
            //Toast.makeText(context,"blue", Toast.LENGTH_SHORT).show();
        }
        else if(r>253){
            newNote("yellow");
            //Toast.makeText(context,"yellow", Toast.LENGTH_SHORT).show();
        }
        else if(r>27 && r<63 && g > 169 && g < 190){
            newNote("green");
            //Toast.makeText(context,"green", Toast.LENGTH_SHORT).show();
        }
        else if(r>220 && r < 235){
            newNote("orange");
            //Toast.makeText(context,"orange", Toast.LENGTH_SHORT).show();
        }

    }
    public void newNote(String color){
        try{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ViewNoteFragment noteFragment = new ViewNoteFragment();
            noteFragment.setColor(color);
            noteFragment.setIsEdit(false);
            fragmentTransaction.add(android.R.id.content,noteFragment );
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        catch (NullPointerException e){
            Log.e(TAG, "cannot create new note fragment");
        }

    }


}
