package com.example.user.myyandextranslate;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.user.myyandextranslate.adapter.FragmentAdapter;
import com.example.user.myyandextranslate.fragments.FavoritesFragment;
import com.example.user.myyandextranslate.fragments.NavigationViewPager;
import com.example.user.myyandextranslate.fragments.SettingsFragment;
import com.example.user.myyandextranslate.fragments.TranslateFragment;
import com.example.user.myyandextranslate.netJson.HttpManager;
import com.example.user.myyandextranslate.netJson.Singleton;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();


        setContentView(R.layout.activity_main);

        NavigationViewPager viewPager = (NavigationViewPager) findViewById(R.id.viewpager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(new TranslateFragment());
        fragmentAdapter.addFragment(new FavoritesFragment());
        fragmentAdapter.addFragment(new SettingsFragment());
        viewPager.setAdapter(fragmentAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsMain);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_translate));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_favorites));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_settings));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        setColorIcon(tab, R.color.text);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        setColorIcon(tab, R.color.colorLightGray);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }

                });
    }


    private void init(){
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        HttpManager.init(this);
        Singleton.init(this);
        ButterKnife.setDebug(BuildConfig.DEBUG);

    }
    //смена цвета иконки не/активной страницы
    private void setColorIcon(TabLayout.Tab tab, int color) {
        tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), color), PorterDuff.Mode.SRC_IN);
    }
}
