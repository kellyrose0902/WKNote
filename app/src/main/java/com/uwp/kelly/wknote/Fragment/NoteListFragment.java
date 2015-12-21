package com.uwp.kelly.wknote.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.uwp.kelly.wknote.Adapter.RecyclerAdapter;
import com.uwp.kelly.wknote.CustomView.MultiDirectionalSlidingDrawer;
import com.uwp.kelly.wknote.Data.Note;
import com.uwp.kelly.wknote.R;
import com.uwp.kelly.wknote.Utils.MultiUtil;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A placeholder fragment containing a simple view.
 */
public class NoteListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;
    private Realm realm;
    private View rootView;
    private Button button;
    private ImageButton redSort;
    private ImageButton blueSort;
    private ImageButton yellowSort;
    private ImageButton greenSort;
    private ImageButton orangeSort;
    private ImageButton violetSort;
    private ImageButton allSort;
    private ItemTouchHelper itemTouchHelper;

    MultiDirectionalSlidingDrawer slider;

    public NoteListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_note_list, container, false);
        FragmentActivity context = getActivity();

        realm = Realm.getInstance(context);
        RealmResults results = realm.where(Note.class).findAll();
        results.sort("timestamp", Sort.DESCENDING);
        mAdapter = new RecyclerAdapter(context,realm,results);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycle_list);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        setItemTouchHelper();
        setUpSlider();

        mAdapter.setSlider(slider);
        button = (Button)rootView.findViewById(R.id.delete_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.deleteAllNotes();
            }
        });


        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        Log.d("RESOLUTION","w = "+dpWidth+", h = "+dpHeight);


        return rootView;

    }

    private void setUpSlider() {
        slider = (MultiDirectionalSlidingDrawer)rootView.findViewById(R.id.color_sort);

        redSort = (ImageButton)slider.findViewById(R.id.red_sort);
        redSort.setOnClickListener(this);

        blueSort = (ImageButton)slider.findViewById(R.id.blue_sort);
        blueSort.setOnClickListener(this);

        greenSort = (ImageButton)slider.findViewById(R.id.green_sort);
        greenSort.setOnClickListener(this);

        violetSort = (ImageButton)slider.findViewById(R.id.violet_sort);
        violetSort.setOnClickListener(this);

        yellowSort = (ImageButton)slider.findViewById(R.id.yellow_sort);
        yellowSort.setOnClickListener(this);

        orangeSort = (ImageButton)slider.findViewById(R.id.orange_sort);
        orangeSort.setOnClickListener(this);

        allSort = (ImageButton)slider.findViewById(R.id.sort_all);
        allSort.setOnClickListener(this);
    }

    public void refreshAdapter(){
        //RealmResults results = realm.where(Note.class).findAll();
        //results.sort("timestamp", Sort.DESCENDING);
        //mAdapter.setResults(results);
        mAdapter.notifyDataSetChanged();
    }

        public void addDummyNotes(){
            for (int i = 0; i< 10 ; i++){
                Note note = new Note();
                note.setTitle("Dummy note");
                note.setTimestamp(MultiUtil.getCurrentTimeStamp());
                note.setColor("blue");
                note.setNote("dummmy text");
                mAdapter.addNote(note);
            }
        }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.red_sort:
                mAdapter.sortNotes("red");
                break;
            case  R.id.blue_sort:
                mAdapter.sortNotes("blue");
                break;
            case R.id.orange_sort:
                mAdapter.sortNotes("orange");
                break;
            case R.id.violet_sort:
                mAdapter.sortNotes("violet");
                break;
            case R.id.yellow_sort:
                mAdapter.sortNotes("yellow");
                break;
            case R.id.green_sort:
                mAdapter.sortNotes("green");
                break;
            case R.id.sort_all:
                mAdapter.sortNotes(null);
                break;
        }
    }
    public void setItemTouchHelper(){
        int swipe = ItemTouchHelper.RIGHT;
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, swipe) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                if(slider!=null && slider.isOpened()){
                    slider.animateClose();
                }
                if (direction == ItemTouchHelper.RIGHT) {
                        int pos = viewHolder.getAdapterPosition();
                        RealmResults<Note>results = mAdapter.getRealmResult();
                        final Note note = results.get(pos);
                        final Note newNote = new Note();
                        newNote.setId(note.getId());
                        newNote.setTitle(note.getTitle());
                        newNote.setTimestamp(note.getTimestamp());
                        newNote.setNote(note.getNote());
                        newNote.setColor(note.getColor());
                        mAdapter.deleteNote(pos);

                        Snackbar sb = Snackbar.make(getView(), " Deleted", Snackbar.LENGTH_LONG);
                        sb.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                realm.beginTransaction();
                                realm.copyToRealm(newNote);
                                realm.commitTransaction();
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                        sb.show();
                    }
                }

        };
        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }
}
