package com.uwp.kelly.wknote.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.uwp.kelly.wknote.Fragment.NoteListFragment;
import com.uwp.kelly.wknote.Fragment.ViewNoteFragment;

public class NoteList extends AppCompatActivity implements ViewNoteFragment.OnEditListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NoteListFragment noteListFragment = new NoteListFragment();
        fragmentTransaction.add(android.R.id.content, noteListFragment,"LIST_FRAGMENT");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }
    @Override
    public void onBackPressed(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        ViewNoteFragment viewNoteFragment = (ViewNoteFragment)fragmentManager.findFragmentByTag("EDIT_FRAGMENT");
        if(viewNoteFragment!=null && viewNoteFragment.isVisible()){
            super.onBackPressed();
        }
        else {
            Intent intent = new Intent(this,CreateNote.class);
            startActivity(intent);
        }

    }

    @Override
    public void notetifyEdition(boolean isEdit) {
        if(isEdit){
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            NoteListFragment listFragment = (NoteListFragment)fragmentManager.findFragmentByTag("LIST_FRAGMENT");
            if(listFragment!=null){
                listFragment.refreshAdapter();
            }
        }
    }
}
