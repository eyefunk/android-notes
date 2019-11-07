package com.example.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivityTag";

    public static final int CODE_FOR_NEW_NOTE = 111;
    public static final int CODE_FOR_EDIT_NOTE = 222;



    //NOTES HELD HERE
    private ArrayList<Note> noteList = new ArrayList<>();

    private int pos;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Read the JSON file
        doRead();
        setCount();

        recyclerView = findViewById(R.id.recycler);
        noteAdapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //makeList();

        ActionBar actionBar = getSupportActionBar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add:
                Intent i = new Intent(this, EditActivity.class);
                startActivityForResult(i,CODE_FOR_NEW_NOTE);
                break;
            case R.id.help:
                Intent j = new Intent(this, AboutActivity.class);
                startActivity(j);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: TESTING SAVE INSTANCE STATE");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: TESTING RESTORE");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent incomingNote) {

        //Incoming note that is result of a new note created
        if (requestCode ==CODE_FOR_NEW_NOTE){
            if (resultCode == RESULT_OK){
                Note blankNote = (Note) incomingNote.getSerializableExtra(EditActivity.newNote);
                noteList.add(0, blankNote);
            }
        }
        //Incoming note that is result of an edited note
        if (requestCode == CODE_FOR_EDIT_NOTE){
            if(resultCode == RESULT_OK){
                Note editedNote = (Note) incomingNote.getSerializableExtra(EditActivity.newNote);
                noteList.remove(pos);
                noteList.add(0, editedNote);

            }
        }

        try {
            doWrite();
        } catch (JSONException e) {
            Toast.makeText(this, "Failed to save.", Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save.", Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        }
        doRead();
        setCount();
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        try {
            doWrite();
        } catch (JSONException e) {
            Toast.makeText(this, "Failed to save.", Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save.", Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        pos = recyclerView.getChildLayoutPosition(v);
        Note selection = noteList.get(pos);

        Intent i = new Intent(this, EditActivity.class);
        Note n = noteList.get(pos);
        i.putExtra("editNote", n);
        startActivityForResult(i,CODE_FOR_EDIT_NOTE);
        Toast.makeText(this, "Selected: " + selection.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onLongClick(View v) {
        pos = recyclerView.getChildLayoutPosition(v);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Would you like to delete the note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteList.remove(pos);
                try {
                    doWrite();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                noteAdapter.notifyDataSetChanged();
                setCount();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


        return true;
    }

    public void doWrite() throws JSONException, IOException {

        JSONArray jsonArray = new JSONArray();

        for (Note n : noteList) {
            try {
                JSONObject noteJSON = new JSONObject();
                noteJSON.put("title", n.getTitle());
                noteJSON.put("note", n.getNote());
                noteJSON.put("date", n.getDate());
                jsonArray.put(noteJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String jsonText = jsonArray.toString();

        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(
                            openFileOutput("mydata.txt", Context.MODE_PRIVATE)
                    );

            outputStreamWriter.write(jsonText);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Toast.makeText(this, "File write failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void doRead() {

        noteList.clear();
        try {
            InputStream inputStream = openFileInput("mydata.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();

                String jsonText = stringBuilder.toString();

                try {
                    JSONArray jsonArray = new JSONArray(jsonText);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");
                        String note = jsonObject.getString("note");
                        long date = jsonObject.getLong("date");
                        Note n = new Note(title, note, date);
                        noteList.add(n);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "doRead: File not found: \" + e.toString()");
        } catch (IOException e) {
            Log.d(TAG, "doRead: Can not read file: " + e.toString());
        }
    }

    private void setCount() {
        count = noteList.size();
        if(count == 0) {
            setTitle("Multi Notes");
        }
        else {
            setTitle("Multi Notes (" + count + ")");
        }
    }
}