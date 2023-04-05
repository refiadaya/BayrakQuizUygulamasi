package com.rfd.sozlukuygulamas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private RecyclerView rv;
    private ArrayList<Words> wordsArrayList;
    private WordsAdapter adapter;
    private DatabaseCopyHelper dbh;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        copy();

        toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        toolbar.setTitle("Sözlük Uygulaması");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        dbh = new DatabaseCopyHelper(this);

        wordsArrayList = new WordsDao().returnAllWords(dbh);

        adapter = new WordsAdapter(this, wordsArrayList, dbh);

        rv.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddWordActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem item = menu.findItem(R.id.actionSearch);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return false;
    }


    public void copy() {
        DatabaseCopyHelper helper = new DatabaseCopyHelper(this);
        try {
            helper.createDataBase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        helper.openDataBase();
    }

    public void search(String searchWord) {
        wordsArrayList = new WordsDao().searchWord(dbh, searchWord);

        adapter = new WordsAdapter(this, wordsArrayList, dbh);
        rv.setAdapter(adapter);

    }

    public void showAlert() {
        LayoutInflater layout = LayoutInflater.from(this);
        View design = layout.inflate(R.layout.alert_design, null);

        EditText editTextEnglishAlert = design.findViewById(R.id.editTextEnglishAlert);
        EditText editTextTurkishAlert = design.findViewById(R.id.editTextTurkishAlert);

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Kelimeleri Güncelle");
        ad.setView(design);
        ad.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String englishName = editTextEnglishAlert.getText().toString().trim();
                String turkishName = editTextTurkishAlert.getText().toString().trim();

                Snackbar.make((View) dialogInterface, "Kelime Güncellendi", Snackbar.LENGTH_SHORT).show();

            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        ad.create().show();




    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }
}