package com.uwp.kelly.wknote.Adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwp.kelly.wknote.CustomView.MultiDirectionalSlidingDrawer;
import com.uwp.kelly.wknote.Data.Note;
import com.uwp.kelly.wknote.Fragment.ViewNoteFragment;
import com.uwp.kelly.wknote.R;
import com.uwp.kelly.wknote.Utils.MultiUtil;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Kelly on 12/19/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {

    public TextView mTextView;
    RealmResults<Note> results;
    FragmentActivity context;
    Realm realm;
    MultiDirectionalSlidingDrawer slider = null;


    public RecyclerAdapter(FragmentActivity context, Realm realm, RealmResults<Note> results){
        this.realm = realm;
        this.results = results;
        this.context = context;

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public CardView card;
        private FragmentActivity context;
        public ViewHolder(FragmentActivity context, View v) {
            super(v);
            this.context = context;
            card = (CardView)v.findViewById(R.id.item_card);
            mTextView = (TextView)v.findViewById(R.id.item_text);

        }


    }


    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(context,view);
        return viewHolder;
    }
    public RealmResults<Note> getRealmResult(){
        return results;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, final int position) {
        final Note note = results.get(position);
        holder.mTextView.setText(note.getTitle());
        holder.card.setCardBackgroundColor(MultiUtil.getColorValue(context, note.getColor()));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(slider!=null){
                    if(slider.isOpened()){
                        slider.animateClose();
                    }
                }
                FragmentManager fragmentManager = context.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ViewNoteFragment noteFragment = new ViewNoteFragment();
                noteFragment.setColor(note.getColor());
                noteFragment.setEditNote(note);
                noteFragment.setIsEdit(true);
                fragmentTransaction.add(android.R.id.content,noteFragment,"EDIT_FRAGMENT" );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(RealmResults results){
        this.results = results;
        notifyDataSetChanged();
    }

    public void deleteNote(int pos){
        realm.beginTransaction();
        results.remove(pos);
        realm.commitTransaction();
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, results.size());
    }


    public void addNote(Note note){
        int id;
        try {
            id = realm.where(Note.class).max("id").intValue()+1;
        }
        catch (NullPointerException e){
            id = 0;
        }

        note.setId(id);
        realm.beginTransaction();
        realm.copyToRealm(note);
        realm.commitTransaction();
        notifyDataSetChanged();
    }
    public void deleteAllNotes(){

        results = realm.where(Note.class).findAll();
        realm.beginTransaction();
        results.clear();
        realm.commitTransaction();
        notifyDataSetChanged();
    }

    public void sortNotes(String color){
        realm.beginTransaction();
        if(color!=null){
            results = realm.where(Note.class).equalTo("color",color).findAll();
        }
        else {
            results = realm.where(Note.class).findAll();
            results.sort("timestamp", Sort.DESCENDING);
        }
        realm.commitTransaction();
        notifyDataSetChanged();
    }

    public void setSlider(MultiDirectionalSlidingDrawer slider){
        this.slider = slider;
    }
}
