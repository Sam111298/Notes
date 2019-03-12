package com.example.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Gson gson;
    SharedPreferences sharedPreferences;
    static List<String> list = new ArrayList<>();
    static List<String> contentList = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    Intent intent;
    ListView listView;
    static boolean isFirstTime=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate() called");
        setContentView(R.layout.activity_main);
        intent = new Intent(this, NotesEditor.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        sharedPreferences = getSharedPreferences("com.example.notes", MODE_PRIVATE);
        listView = findViewById(R.id.listView);
        if(isFirstTime) {
            if (sharedPreferences.getString("NotesTitles", null) != null) {
                list = getList("NotesTitles");
                contentList = getList("ContentList");
            }
            isFirstTime=false;
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("noteId", position);
                finish();
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                contentList.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.addNote:
                intent.putExtra("noteId",-1);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sharedPreferences.edit().putString("NotesTitles",setList(list)).apply();
        sharedPreferences.edit().putString("ContentList",setList(contentList)).apply();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public <T> String setList(List<T> list){
        gson = new Gson();
        return gson.toJson(list) ;
    }

    public <T> ArrayList<T> getList(String key){
        List<T> arrayItems = new ArrayList<>();
        String serializedObject = sharedPreferences.getString(key, null);
        if (serializedObject != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<T>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return (ArrayList<T>) arrayItems;
    }
}
