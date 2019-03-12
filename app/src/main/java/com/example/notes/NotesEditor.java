package com.example.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class NotesEditor extends AppCompatActivity {
    Intent intent;
    EditText titleEditText;
    EditText contentEditText;
    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);
        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//
        Intent i = getIntent();
        noteId = i.getIntExtra("noteId",-1);
        if(noteId!=-1) {
            titleEditText.setText(MainActivity.list.get(noteId));
            contentEditText.setText(MainActivity.contentList.get(noteId));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(noteId!=-1) {
            MainActivity.list.set(noteId, titleEditText.getText().toString());
            MainActivity.contentList.set(noteId, contentEditText.getText().toString());
        }
        else {
            MainActivity.list.add(titleEditText.getText().toString());
            MainActivity.contentList.add(contentEditText.getText().toString());
        }
        MainActivity.arrayAdapter.notifyDataSetChanged();
        finish();
        startActivity(intent);
    }
}
