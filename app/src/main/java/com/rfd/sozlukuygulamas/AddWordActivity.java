package com.rfd.sozlukuygulamas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class AddWordActivity extends AppCompatActivity {
    private Toolbar toolbarAdd;
    private EditText editTextTurkish, editTextEnglish;
    private Button buttonAdd;
    private DatabaseCopyHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        toolbarAdd = findViewById(R.id.toolbarAdd);
        editTextEnglish = findViewById(R.id.editTextEnglish);
        editTextTurkish = findViewById(R.id.editTextTurkish);
        buttonAdd = findViewById(R.id.buttonAdd);

        dbh = new DatabaseCopyHelper(this);

        toolbarAdd.setTitle("Yeni Kelime Ekle");

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newWordEnglish = editTextEnglish.getText().toString().trim();
                String newWordTurkish = editTextTurkish.getText().toString().trim();

                if(TextUtils.isEmpty(newWordEnglish)){
                    Snackbar.make(view, "Kelime giriniz!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(newWordTurkish)){
                    Snackbar.make(view, "Kelime giriniz!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                new WordsDao().addWord(dbh, newWordEnglish, newWordTurkish);

                startActivity(new Intent(AddWordActivity.this, MainActivity.class));
            }
        });
    }
}