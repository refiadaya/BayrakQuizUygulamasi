package com.rfd.sozlukuygulamas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class DetailActivity extends AppCompatActivity {
    private TextView textViewEnglishDetail, textViewTurkishDetail;
    private Words word;
    private Toolbar toolbarDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbarDetail = findViewById(R.id.toolbarDetail);
        textViewEnglishDetail = findViewById(R.id.textViewEnglishDetail);
        textViewTurkishDetail = findViewById(R.id.textViewTurkishDetail);

        word = (Words) getIntent().getSerializableExtra("object");

        textViewTurkishDetail.setText(word.getTurkish());
        textViewEnglishDetail.setText(word.getEnglish());

        toolbarDetail.setTitle("Detay Ekranı");

    }
}