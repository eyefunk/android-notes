package com.example.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {
    private EditText noteText;
    private EditText titleText;
    private String temp;
    public static final String newNote = "NOTE SAVED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Multi Notes");
        actionBar.setDisplayHomeAsUpEnabled(false);
        titleText = findViewById(R.id.titleBody);
        noteText = findViewById(R.id.noteBody);
        temp = "";
        Intent fromMain = getIntent();
        if(fromMain.hasExtra("editNote")) {
            Note n = (Note) fromMain.getSerializableExtra("editNote");
            noteText.setText(n.getNote());
            titleText.setText(n.getTitle());

            temp = n.getNote();
        }
    }

    @Override
    public void onBackPressed() {
        if(titleText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Untitled activity was not saved.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }
        else if(temp.equals(noteText.getText().toString())) {
            setResult(RESULT_CANCELED);
            finish();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save Note");
            builder.setMessage("Would you like to save the note?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    createNote(findViewById(R.id.recycler));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save:
                createNote(findViewById(R.id.recycler));
                break;
            default: return
                    super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNote(View v) {
        Intent i = new Intent();
        if(titleText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Need title before saving.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
        }
        else {
            String title = titleText.getText().toString();
            String note = noteText.getText().toString();
            long date = System.currentTimeMillis();
            Note n = new Note(title, note, date);

            i.putExtra(newNote,n);
            setResult(RESULT_OK, i);
            finish();


        }
    }


}
