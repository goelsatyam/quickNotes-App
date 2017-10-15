package com.example.codeml.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class notes extends AppCompatActivity {
    private static int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        /* prefs will cotain all data*/
        SharedPreferences prefs = getSharedPreferences("NOTES", MODE_PRIVATE);
        /* count will keep total number of notes in app*/
        total = prefs.getInt("count", /* default */ -1);

    }


    public void addNote(View view) {
        /*Take to new activity for making new notes*/
        Intent intent = new Intent(this, addnotes.class);
        //vaule will tell if we need to create new note
        intent.putExtra("value",false);
        intent.putExtra("count", total);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences prefs = getSharedPreferences("NOTES", MODE_PRIVATE);
        total = prefs.getInt("count", /* default */ 0);
        showViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("NOTES", MODE_PRIVATE);
        total = prefs.getInt("count", /* default */ 0);
        showViews();
    }

    protected void showViews(){
        String[] list = new String[total+1];

        SharedPreferences prefs = getSharedPreferences("NOTES", MODE_PRIVATE);

        for(int i=0;i<=total;i++){
         String a = prefs.getString(String.valueOf(i),"");

            if(a.length() > 100){
                String x = a.substring(0,50);
                list[i] = x;
            }
            else{
                for(int i1=a.length();i1<=50;i1++){
                    a = a + "  ";
                }
                list[i] = a;
            }

        }

        final List<String> list1 = new ArrayList<String>(Arrays.asList(list));
        final GridView grid = (GridView) findViewById(R.id.gridView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, list1);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), addnotes.class);
                intent.putExtra("value",true);
                intent.putExtra("index", i);
                intent.putExtra("count", total);
                startActivity(intent);
            }
        });

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder box = new AlertDialog.Builder(notes.this);
                box.setTitle("Delete this");

                box.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // code to run when OK is pressed
                            SharedPreferences prefs = getSharedPreferences(
                                    "NOTES", MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = prefs.edit();

                            for(int j=i;j<total;j++){
                                prefsEditor.putString(String.valueOf(j), prefs.getString(String.valueOf(j+1),""));
                            }
                            total--;
                            prefsEditor.putInt("count", total);
                            prefsEditor.apply();

                            list1.remove(i);
                            adapter.notifyDataSetChanged();
                            grid.setAdapter(adapter);

                            Toast.makeText(notes.this, "Deleted",
                                    Toast.LENGTH_SHORT).show();

                    }
                });

                box.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            //Do nothing
                    }
                });

                AlertDialog dialog = box.create();
                dialog.show();
                return true;
            }
        });

    }
}
