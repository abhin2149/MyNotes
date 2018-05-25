package com.example.abhinav.mynotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView notesList;
    static ArrayList<String> notes;
    static ArrayAdapter adapter;
    static SharedPreferences preferences;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent in=new Intent(getApplicationContext(),notesActivity.class);

        startActivity(in);


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater=this.getMenuInflater();
        inflater.inflate(R.menu.item_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesList=(ListView)findViewById(R.id.notesList);
        preferences=this.getSharedPreferences("com.example.abhinav.mynotes", Context.MODE_PRIVATE);

        notes=new ArrayList<String>();

        if(preferences.getAll().size()>0 && preferences.contains("notes")){


            try {
                notes=(ArrayList<String>)ObjectSerializer.deserialize(preferences.getString("notes",ObjectSerializer.serialize(new ArrayList<String>())));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        else {

            notes.add("Example note...");

        }

        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);

        notesList.setAdapter(adapter);


       notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent in=new Intent(getApplicationContext(),notesActivity.class);

               in.putExtra("index",i);

               Log.i("index",""+i);
               startActivity(in);
           }
       });

       notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

               final int index=i;


               new AlertDialog.Builder(MainActivity.this)
                       .setIcon(android.R.drawable.ic_dialog_alert)
                       .setTitle("Are you sure?")
                       .setMessage("Do you want to delete this note")
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {


                               notes.remove(index);

                               adapter.notifyDataSetChanged();

                               preferences.edit().clear();
                               try {

                                   preferences.edit().putString("notes",ObjectSerializer.serialize(notes)).apply();

                               } catch (IOException e) {
                                   e.printStackTrace();
                               }




                           }
                       })
                       .setNegativeButton("No",null)
                       .show();




               return true;
           }
       });


    }
}
