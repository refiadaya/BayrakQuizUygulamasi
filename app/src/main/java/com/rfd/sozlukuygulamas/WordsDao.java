package com.rfd.sozlukuygulamas;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

//Database Access Object
public class WordsDao{

    public ArrayList<Words> returnAllWords(DatabaseCopyHelper dbh){
        ArrayList<Words> wordsArrayList = new ArrayList<>();

        SQLiteDatabase db = dbh.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT*FROM kelimeler", null);
        while (c.moveToNext()){
            @SuppressLint("Range") Words w = new Words(c.getInt(c.getColumnIndex("kelime_id")),
                    c.getString(c.getColumnIndex("ingilizce")),
                    c.getString(c.getColumnIndex("turkce")));

            wordsArrayList.add(w);
        }
        return wordsArrayList;
    }

    public ArrayList<Words> searchWord(DatabaseCopyHelper dbh, String searchWord){
        ArrayList<Words> wordsArrayList = new ArrayList<>();

        SQLiteDatabase db = dbh.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT*FROM kelimeler WHERE ingilizce like '%"+searchWord+"%'", null);
        while (c.moveToNext()){
            @SuppressLint("Range") Words w = new Words(c.getInt(c.getColumnIndex("kelime_id")),
                    c.getString(c.getColumnIndex("ingilizce")),
                    c.getString(c.getColumnIndex("turkce")));
            wordsArrayList.add(w);
        }
        return wordsArrayList;
    }

    public void addWord(DatabaseCopyHelper dbh, String english, String turkish) {
        SQLiteDatabase db = dbh.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ingilizce", english);
        values.put("turkce", turkish);

        db.insertOrThrow("kelimeler", null, values);
        db.close();
    }

    public void editWord(DatabaseCopyHelper dbh, int wordId, String english, String turkish) {
        SQLiteDatabase db = dbh.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ingilizce", english);
        values.put("turkce", turkish);
        db.update("kelimeler", values, "kelime_id=?", new String[]{String.valueOf(wordId)});
        db.close();
    }

    public void deleteWord(DatabaseCopyHelper dbh, int wordId) {
        SQLiteDatabase db = dbh.getWritableDatabase();

        db.delete("kelimeler","kelime_id=?", new String[]{String.valueOf(wordId)});
        db.close();
    }
}
