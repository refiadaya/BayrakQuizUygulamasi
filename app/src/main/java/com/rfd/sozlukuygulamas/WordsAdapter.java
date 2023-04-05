package com.rfd.sozlukuygulamas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.CardDesignHolder> {
    private Context mContext;
    private List<Words> wordsList;
    private DatabaseCopyHelper dbh;

    public WordsAdapter(Context mContext, List<Words> wordsList, DatabaseCopyHelper dbh) {
        this.mContext = mContext;
        this.wordsList = wordsList;
        this.dbh = dbh;
    }

    @NonNull
    @Override
    public CardDesignHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design, parent, false);
        return new CardDesignHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardDesignHolder holder, int position) {

        final Words word = wordsList.get(position);

        holder.textViewEnglish.setText(word.getEnglish());
        holder.textViewTurkish.setText(word.getTurkish());

        holder.wordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("object", word);
                mContext.startActivity(intent);
            }
        });

        holder.imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.imageViewMore);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.actionDelete:
                                Snackbar snackbar = Snackbar.make(holder.imageViewMore, "Silinsin mi?", Snackbar.LENGTH_LONG).setAction("Evet", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new WordsDao().deleteWord(dbh, word.getWordId());
                                        wordsList = new WordsDao().returnAllWords(dbh);
                                        notifyDataSetChanged();
                                    }
                                });
                                snackbar.show();
                                return true;
                            case R.id.actionUpdate:
                                showAlert(view, word);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return wordsList.size();
    }

    //kart tasarımını modelleyeceğimiz sınıf
    public class CardDesignHolder extends RecyclerView.ViewHolder {
        private TextView textViewEnglish;
        private TextView textViewTurkish;
        private CardView wordCard;
        private ImageView imageViewMore;


        public CardDesignHolder(@NonNull View itemView) {
            super(itemView);
            textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            textViewTurkish = itemView.findViewById(R.id.textViewTurkish);
            wordCard = itemView.findViewById(R.id.wordCard);
            imageViewMore = itemView.findViewById(R.id.imageViewMore);
        }
    }

    public void showAlert(View v,Words word) {
        final View view = v;
        LayoutInflater layout = LayoutInflater.from(mContext);
        View design = layout.inflate(R.layout.alert_design, null);

        final EditText editTextEnglishAlert = design.findViewById(R.id.editTextEnglishAlert);
        final EditText editTextTurkishAlert = design.findViewById(R.id.editTextTurkishAlert);

        editTextEnglishAlert.setText(word.getEnglish());
        editTextTurkishAlert.setText(word.getTurkish());

        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
        ad.setTitle("Kelimeleri Güncelle");
        ad.setView(design);
        ad.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String englishName = editTextEnglishAlert.getText().toString().trim();
                String turkishName = editTextTurkishAlert.getText().toString().trim();

                new WordsDao().editWord(dbh,word.getWordId(), englishName, turkishName);
                wordsList = new WordsDao().returnAllWords(dbh);
                notifyDataSetChanged();

                Snackbar.make(view,"Kelime Güncellendi", Snackbar.LENGTH_SHORT).show();

            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        ad.create().show();
    }
}
