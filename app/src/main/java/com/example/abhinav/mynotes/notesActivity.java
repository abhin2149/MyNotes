package com.example.abhinav.mynotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.io.IOException;

public class notesActivity extends AppCompatActivity {

    EditText note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        note=(EditText)findViewById(R.id.editText);

        if(getIntent().hasExtra("index")){

            note.setText(MainActivity.notes.get(getIntent().getIntExtra("index",0)));
        }


        else{


            note.setText("");

        }




    }



    @Override
    public void onBackPressed() {

        if(getIntent().hasExtra("index")){
            int index=getIntent().getIntExtra("index",0);

            MainActivity.notes.remove(index);

            if((note.getTextSize()>0)){



                MainActivity.notes.add(index,note.getText().toString());




                try {

                    MainActivity.preferences.edit().putString("notes",ObjectSerializer.serialize(MainActivity.notes)).apply();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.adapter.notifyDataSetChanged();
            }






        }
        else {

            if ((note.getTextSize() > 0)) {


                MainActivity.notes.add(note.getText().toString());


                try {

                    MainActivity.preferences.edit().putString("notes",ObjectSerializer.serialize(MainActivity.notes)).apply();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                MainActivity.adapter.notifyDataSetChanged();
            }

        }

        super.onBackPressed();
    }
}
