package com.example.assignment2;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder {

    TextView titleText;
    TextView dateText;
    TextView noteText;

    MyHolder(View view) {
        super(view);

        titleText = view.findViewById(R.id.titleBody);
        dateText = view.findViewById(R.id.dateText);
        noteText = view.findViewById(R.id.noteText);
    }
}
