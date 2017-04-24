package com.example.user.myyandextranslate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.user.myyandextranslate.R;
import com.example.user.myyandextranslate.model.Direction;
import com.example.user.myyandextranslate.model.Language;
import com.example.user.myyandextranslate.model.Translate;
import com.example.user.myyandextranslate.netJson.JsonYandex;
import com.example.user.myyandextranslate.netJson.LanguagesResponce;
import com.example.user.myyandextranslate.netJson.TranslateResponce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import icepick.Icepick;
import icepick.State;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class TranslateFragment extends Fragment {

    @State String languageFromState;
    @State String languageToState;
    @State String inputTextState;

    @BindView(R.id.textViewLanguageFrom) protected TextView tvLanguageFrom;
    @BindView(R.id.textViewLanguageTo) protected TextView tvLanguageTo;
    @BindView(R.id.buttonSwitchDirection) protected ImageButton ibSwitchDirection;
    @BindView(R.id.buttonClearText) protected ImageButton ibClearText;
    @BindView(R.id.textEditInputText) protected EditText etInputText;
    @BindView(R.id.textViewTranslatedText) protected TextView tvTranslatedText;
    @BindView(R.id.textViewDictionaryEntry) protected TextView tvDictionaryEntry;
    @BindView(R.id.toggleButtonIsFavorites) protected ToggleButton tbIsFavorites;
    @BindView(R.id.leftpanel) protected LinearLayout leftPanel;

    @BindString(R.string.defaultLanguageFrom) protected String defaultLanguageFrom;
    @BindString(R.string.defaultLanguageTo) protected String defaultLanguageTo;

    private Realm realm;
    private Unbinder unbinder;

    // карта языков
    private Map<String,String> map = new HashMap<>();

    // Список направлений переводов
    private List<String> mDirections = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_translate, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        realm = Realm.getDefaultInstance();

        if(languageFromState != null) {
            setLanguageFrom(languageFromState.trim().isEmpty()?defaultLanguageFrom:languageFromState);
        } else {
            setLanguageFrom(defaultLanguageFrom);
        }

        if(languageToState != null) {
            setLanguageTo(languageToState.trim().isEmpty()?defaultLanguageTo:languageToState);
        } else {
            setLanguageTo(defaultLanguageTo);
        }

        if(inputTextState != null) {
            setInputText(inputTextState.trim().isEmpty() ? "" : inputTextState);
        }

        loadLanguages();

        translate();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.hasExtra("direction")) {
            if(data.hasExtra("selectedLanguage")) {

                int direction = data.getIntExtra("direction", 0);
                String selectedLanguage = data.getStringExtra("selectedLanguage");

                if(map.containsValue(selectedLanguage)) {

                    switch (direction) {

                        case 1:
                            if(getLanguageTo().equals(selectedLanguage)) {
                                setLanguageTo(getLanguageFrom());
                            }
                            setLanguageFrom(selectedLanguage);
                            break;

                        case 2:
                            if(getLanguageFrom().equals(selectedLanguage)) {
                                setLanguageFrom(getLanguageTo());
                            }
                            setLanguageTo(selectedLanguage);
                            break;
                    }

                    translate();
                }
            }
        }
    }

    @OnTextChanged(R.id.textEditInputText)
    public void OnTextChangedInputText() {
        if(getInputText().trim().isEmpty()) {
            leftPanel.setVisibility(View.INVISIBLE);
            tbIsFavorites.setChecked(false);
        } else {
            leftPanel.setVisibility(View.VISIBLE);
        }
        translate();
    }

    @OnClick(R.id.textViewLanguageFrom)
    public void onClickLanguageFrom() {
        choiceLanguage(new ArrayList<>(map.values()), 1);
    }

    @OnClick(R.id.textViewLanguageTo)
    public void onClickLanguageTo() {
        choiceLanguage(new ArrayList<>(map.values()), 2);
    }

    @OnClick(R.id.buttonSwitchDirection)
    public void onClickSwitchDirection() {
        String temp = getLanguageFrom();
        setLanguageFrom(getLanguageTo());
        setLanguageTo(temp);
        translate();
    }

    @OnClick(R.id.buttonClearText)
    public void onClickClearText() {
        setInputText("");
    }

    @OnClick(R.id.toggleButtonIsFavorites)
    public void onClickIsFavorites() {
        if(!getInputText().trim().isEmpty()) {
            realm.executeTransactionAsync(new Realm.Transaction() {

                @Override
                public void execute(Realm bgRealm) {
                    Translate translate = new Translate();
                    translate.setText(getInputText().trim());
                    translate.setDirection(getDirection());
                    translate.setTranslatedText(tvTranslatedText.getText().toString().trim());
                    translate.setFavorites(tbIsFavorites.isChecked());
                    bgRealm.insertOrUpdate(translate);
                }
            });
        }
    }

    @OnFocusChange(R.id.textEditInputText)
    public void onFocusChangeInputText(View v, boolean hasFocus) {
        if(!getInputText().trim().isEmpty() && !hasFocus) {
            realm.executeTransactionAsync(new Realm.Transaction() {

                @Override
                public void execute(Realm bgRealm) {

                    Translate translate = new Translate();
                    translate.setText(getInputText().trim());
                    translate.setFavorites(false);
                    translate.setTranslatedText(tvTranslatedText.getText().toString().trim());
                    translate.setDirection(getDirection());

                    bgRealm.insertOrUpdate(translate);
                }

            });
        }
            }

    @OnClick({R.id.textViewTranslatedText, R.id.textViewDictionaryEntry})
    public void onClickTranslatedText(View v) {
        v.requestFocus();

    }

    //выбор языка
    private void choiceLanguage(final ArrayList<String> languages, final int direction) {
        Intent intent = new Intent(getContext(), ChoiceLanguage.class);
        intent.putStringArrayListExtra("languages", languages);
        intent.putExtra("direction", direction);
        startActivityForResult(intent, 0);
    }


    @Nullable
    private String getKeyToValue(final Map<String, String> map, final String value) {

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if(entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }

        return null;
    }

    //загрузка языков
    private void loadLanguages() {

        final RealmResults<Language> rlanguages = realm.where(Language.class).findAll();
        final RealmResults<Direction> rdirections = realm.where(Direction.class).findAll();


        if(rlanguages.size() == 0 || rdirections.size() == 0) {
            JsonYandex.getSupportLanguages().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<LanguagesResponce>() {
                        @Override
                        public void accept(@NonNull LanguagesResponce languagesResponce) throws Exception {
                            map = languagesResponce.getLangs();
                            mDirections = languagesResponce.getDirs();
                            saveLanguagesInRealm(languagesResponce);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            Toast.makeText(getContext(), R.string.errorTranslate, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            for(int i = 0; i < rlanguages.size(); i++) {
                map.put(rlanguages.get(i).getKey(), rlanguages.get(i).getValue());
            }
            for(int i = 0; i < rdirections.size(); i++) {
                mDirections.add(rdirections.get(i).getDirection());
            }
        }
    }

   //перевод
    private void translate() {

        final String text = getInputText().trim();
        final String direction = getDirection();

        if(!text.isEmpty() && map.size() > 0) {


            JsonYandex.getTranslate(direction, text)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<TranslateResponce>() {
                        @Override
                        public void accept(@NonNull TranslateResponce translateResponce) throws Exception {
                            if(translateResponce != null) {
                                if(translateResponce.getCode().equals(200)) {
                                    tvTranslatedText.setText(translateResponce.getText().iterator().next());
                                } else {
                                    tvTranslatedText.setText("");
                                }
                            } else {
                                tvTranslatedText.setText("");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            Toast.makeText(getContext(), R.string.errorTranslate, Toast.LENGTH_SHORT).show();
                        }
                    });



        } else {
            tvTranslatedText.setText("");
            tvDictionaryEntry.setText("");
        }
    }

    //Сохранение языков в базу
    private void saveLanguagesInRealm(final LanguagesResponce languagesResponce) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                RealmResults<Language> rLanguage = bgRealm.where(Language.class).findAll();
                rLanguage.deleteAllFromRealm();
                for (Map.Entry<String, String> entry : languagesResponce.getLangs().entrySet()) {
                    Language lang = bgRealm.createObject(Language.class, entry.getKey());
                    lang.setValue(entry.getValue());
                }

                RealmResults<Direction> rDirection = bgRealm.where(Direction.class).findAll();
                rDirection.deleteAllFromRealm();

            }
        });
    }

    //перевод с какого на какой
    private String getDirection() {
        final String langFrom = getKeyToValue(map, getLanguageFrom());
        final String langTo = getKeyToValue(map, getLanguageTo());
        if(langFrom != null && langTo != null) {
            return langFrom + "-" + langTo;
        }
        return "";
    }


    private String getLanguageFrom() {
        return tvLanguageFrom.getText().toString();
    }

    private void setLanguageFrom(final String selectedLanguage) {
        tvLanguageFrom.setText(selectedLanguage);
    }

    private String getLanguageTo() {
        return tvLanguageTo.getText().toString();
    }

    private void setLanguageTo(final String selectedLanguage) {
        tvLanguageTo.setText(selectedLanguage);
    }

    private String getInputText() {
        return etInputText.getText().toString();
    }

    private void setInputText(final String text) {
        etInputText.setText(text);
    }

}
