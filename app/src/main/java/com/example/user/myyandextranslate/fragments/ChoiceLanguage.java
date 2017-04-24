package com.example.user.myyandextranslate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.myyandextranslate.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

;

public class ChoiceLanguage extends AppCompatActivity {

    @BindView(R.id.buttonClose) protected ImageButton ibClose;
    @BindView(R.id.languagesList) protected ListView lwLanguagesList;
    @BindView(R.id.textViewTitle) protected TextView tvTitle;

    private ArrayList<String> languages;
    private int direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_choice);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra("direction")) {
            direction = intent.getIntExtra("direction", 0);
            setTitleActivity(direction);
        }

        if(intent.hasExtra("languages")) {
            languages = intent.getStringArrayListExtra("languages");
            Collections.sort(languages);
        }

        lwLanguagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeActivity(languages.get(position));
            }
        });

        lwLanguagesList.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_language, languages));

    }

    @OnClick(R.id.buttonClose)
    public void onClickCloceActivity() {
        closeActivity("");
    }

    private void closeActivity(final String language) {
        Intent intent = new Intent();
        intent.putExtra("selectedLanguage", language);
        intent.putExtra("direction", direction);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setTitleActivity(final int direction) {
        if(direction == 1) {
            tvTitle.setText(R.string.inputLanguage);
        } else if(direction == 2) {
            tvTitle.setText(R.string.targetLanguage);
        }
    }

}
