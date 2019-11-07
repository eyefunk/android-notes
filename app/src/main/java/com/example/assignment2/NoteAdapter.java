package com.example.assignment2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class NoteAdapter extends RecyclerView.Adapter<MyHolder> {

    private static final String TAG = "NoteAdapter";
    private MainActivity mainActivity;
    private ArrayList<Note> noteList;
    private String truncatedNote;
    private String truncatedTitle;

    NoteAdapter(ArrayList<Note> list, MainActivity mainActivity) {
        noteList = list;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: creating new item");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item, parent, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: setting data");
        Note selection = noteList.get(position);
        Date date = new Date(selection.getDate());

        if(selection.getNote().length() > 80)   truncatedNote = selection.getNote().substring(0,80) + "...";
        else    truncatedNote = selection.getNote();

        if(selection.getTitle().length() > 35)  truncatedTitle = selection.getTitle().substring(0,35) + "...";
        else    truncatedTitle = selection.getTitle();

        holder.titleText.setText(truncatedTitle);
        holder.noteText.setText(truncatedNote);
        holder.dateText.setText(date.toString());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}