package com.example.user.myyandextranslate.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.user.myyandextranslate.R;
import com.example.user.myyandextranslate.adapter.DeleteAdapter;
import com.example.user.myyandextranslate.model.Translate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import icepick.Icepick;
import icepick.State;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class FavoritesFragment extends Fragment {

    @State String searchTextState;

    @BindView(R.id.tabsHistoryAndFavorites) protected TabLayout rememberwords;
    @BindView(R.id.editTextSearch) protected EditText etSearch;
    @BindView(R.id.buttonClearHistory) protected ImageButton ibClearHistory;
    @BindView(R.id.historyAndFavoritesList) protected RecyclerView rvHistoryAndFavoritesList;

    private Realm realm;
    private Unbinder unbinder;
    private DeleteAdapter adapter;
    private RealmResults<Translate> results;
    private TabLayout.Tab history;
    private TabLayout.Tab favorite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        searchTextState = getSearchText();
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();

        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if(searchTextState != null) {
            etSearch.setText(!searchTextState.isEmpty() ? searchTextState : "");
        }

        history = rememberwords.newTab();
        favorite = rememberwords.newTab();
        rememberwords.addTab(history.setText(R.string.history));
        rememberwords.addTab(favorite.setText(R.string.favorites));

        rememberwords.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                etSearch.setHint(tab.equals(history)?R.string.searchInHistory:R.string.searchInFavorites);
                updateListTranslatedWords();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateListTranslatedWords();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        results = realm.where(Translate.class).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<Translate>>() {

            @Override
            public void onChange(RealmResults<Translate> element) {
                adapter.notifyDataSetChanged();
            }
        });

        rvHistoryAndFavoritesList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DeleteAdapter(getContext(), realm, results);
        rvHistoryAndFavoritesList.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        unbinder.unbind();
    }

    @OnClick(R.id.buttonClearHistory)
    public void onClickClearHistory() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        dialog.setTitle(R.string.history);
        dialog.setMessage(R.string.clearHistory);
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realm.executeTransactionAsync(new Realm.Transaction() {

                    @Override
                    public void execute(Realm bgRealm) {
                        bgRealm.where(Translate.class).findAll().deleteAllFromRealm();
                    }
                });
            }
        });
        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private void updateListTranslatedWords() {
        final String searchText = getSearchText();
        if(!searchText.trim().isEmpty()) {
            if(favorite.isSelected()) {
                results = realm.where(Translate.class)
                        .equalTo("isFavorites", true)
                        .contains("text", getSearchText())
                        .or()
                        .contains("translatedText", getSearchText()).findAll();
            } else {
                results = realm.where(Translate.class)
                        .contains("text", getSearchText())
                        .or()
                        .contains("translatedText", getSearchText()).findAll();
            }
        } else {
            if(favorite.isSelected()) {
                results = realm.where(Translate.class).equalTo("isFavorites", true).findAll();
            } else {
                results = realm.where(Translate.class).findAll();
            }
        }
        adapter.setResults(results);
    }

    private String getSearchText() {
        return etSearch.getText().toString();
    }
}
