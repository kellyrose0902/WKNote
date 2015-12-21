package com.uwp.kelly.wknote.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.uwp.kelly.wknote.Activity.NoteList;
import com.uwp.kelly.wknote.CustomView.CustomMovementMethod;
import com.uwp.kelly.wknote.CustomView.EditTextFont;
import com.uwp.kelly.wknote.CustomView.TextViewFont;
import com.uwp.kelly.wknote.Data.Note;
import com.uwp.kelly.wknote.R;
import com.uwp.kelly.wknote.Utils.MultiUtil;

import io.realm.Realm;

/**
 * Created by Kelly on 12/17/2015.
 */
public class ViewNoteFragment extends Fragment {

    String colorTheme;
    boolean isEdit;
    private ImageButton doneButton;
    EditTextFont noteText;
    EditTextFont noteTitle;
    Note editNote;
    OnEditListener mCallback;

    public interface OnEditListener{
        public void notetifyEdition(boolean isEdit);
    }


    public void setColor(String color){
        this.colorTheme = color;
    }

    public void setEditNote(Note note){
        this.editNote = note;
    }

    public void setIsEdit(boolean isEdit){
        this.isEdit = isEdit;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (OnEditListener)context;

        }catch (ClassCastException e){

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_note, container, false);

        initializeView(rootView);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });



        return rootView;
    }



    private void initializeView(View rootView) {
        if(colorTheme!=null){
            int color = MultiUtil.getColorValue(getActivity(), colorTheme);
            RelativeLayout backround = (RelativeLayout)rootView.findViewById(R.id.background);
            backround.setBackgroundColor(color);

            noteTitle = (EditTextFont)rootView.findViewById(R.id.note_title);
            noteTitle.setHintTextColor(color);
            noteTitle.setTextColor(color);

            noteText = (EditTextFont)rootView.findViewById(R.id.note_text);

            noteText.setLinksClickable(true);
            noteText.setLinkTextColor(MultiUtil.getColorValue(getActivity(), colorTheme));
            noteText.setMovementMethod(CustomMovementMethod.getInstance());
            noteText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                    return false;
                }
            });

            TextViewFont doneText = (TextViewFont)rootView.findViewById(R.id.done_text);
            doneText.setTextColor(color);

        }
        doneButton = (ImageButton)rootView.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishEditting();
            }
        });

        if(isEdit){
            noteTitle.setText(editNote.getTitle());
            noteText.setText(editNote.getNote());
        }
        Linkify.addLinks(noteText,Linkify.ALL);

        noteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Linkify.addLinks(s,Linkify.ALL);
            }
        });
    }

    public void onFinishEditting(){
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(noteText.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(noteTitle.getWindowToken(),0);
        if(isEdit){
            updateNote();
            mCallback.notetifyEdition(true);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();

        }
        else {

            addNewNote();
            Intent intent = new Intent(getActivity(), NoteList.class);
            startActivity(intent);
        }


    }

    public void addNewNote() {
        if (MultiUtil.hasValue(noteText)) {
            Realm realm = Realm.getInstance(getActivity());
            realm.beginTransaction();

            Note note = new Note();
            int id;
            try {
                id = realm.where(Note.class).max("id").intValue() + 1;
            } catch (NullPointerException e) {
                id = 0;
            }
            note.setId(id);

            note.setTitle(noteTitle.getText().toString());


            note.setNote(noteText.getText().toString());
            note.setColor(colorTheme);
            note.setTimestamp(MultiUtil.getCurrentTimeStamp());
            realm.copyToRealm(note);
            realm.commitTransaction();

        }
    }
    public void updateNote(){
        Realm realm = Realm.getInstance(getActivity());
        realm.beginTransaction();
        editNote.setTitle(noteTitle.getText().toString());
        editNote.setNote(noteText.getText().toString());
        editNote.setTimestamp(MultiUtil.getCurrentTimeStamp());
        realm.copyToRealmOrUpdate(editNote);
        realm.commitTransaction();
    }
}

