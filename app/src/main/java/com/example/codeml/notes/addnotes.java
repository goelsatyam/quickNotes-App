package com.example.codeml.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class addnotes extends AppCompatActivity {
    private static boolean a;
    private static int x;
    private static int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addnotes);
        Intent intent = getIntent();

        a = intent.getBooleanExtra("value", false);

        if(a == true){

            x = intent.getIntExtra("index", 0);
            SharedPreferences prefs = getSharedPreferences("NOTES", MODE_PRIVATE);
            String text = prefs.getString(String.valueOf(x), "");

            EditText editText = (EditText) findViewById(R.id.editText);
            editText.setText(text);
        }

        total = intent.getIntExtra("count", 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNotes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveNotes();

    }

    private void saveNotes(){

        EditText editText = (EditText) findViewById(R.id.editText);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        String text = editText.getText().toString();

        SharedPreferences prefs = getSharedPreferences(
                "NOTES", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();


        if(text.length() != 0) {
            if(a == false){
                total++;
                prefsEditor.putString(String.valueOf(total), text);
            }
            else{
                prefsEditor.putString(String.valueOf(x), text);
            }
        }
        prefsEditor.putInt("count",total);
        prefsEditor.apply();
    }

}
