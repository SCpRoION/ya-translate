package ru.ya.translate;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import ru.ya.translate.history.*;
import ru.ya.translate.translation.TranslationModel;
import ru.ya.translate.translation.TranslationsStorage;
import ru.ya.translate.translator.TranslatorFragment;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;  /** Адаптер фрагментов на каждый таб */
    private ViewPager mViewPager;                        /** Контейнер фрагментов */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Инициализировать хранилище переводов
        TranslationsStorage.init(this);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter implements
            MainActivity.OnGoToTranslatorFragmentListener {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new TranslatorFragment();
                    break;
                case 1:
                    fragment = new HistoryFragment();
                    ((HistoryFragment) fragment).setOnGoToTranslationFragmentListener(this);
                    break;
                case 2:
                    fragment = new FavoritesFragment();
                    ((FavoritesFragment) fragment).setOnGoToTranslationFragmentListener(this);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.translator);
                case 1:
                    return getString(R.string.history);
                case 2:
                    return getString(R.string.favorites);
            }
            return null;
        }

        @Override
        public void goToTranslatorFragment(TranslationModel currentTranslation) {
            mViewPager.setCurrentItem(0);
            TranslatorFragment fr = (TranslatorFragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(0));
            fr.setState(currentTranslation);
        }
    }

    /**
     * Нажата кнопка из фрагмента переводчика
     * @param v нажатая кнопка
     */
    public void translatorFragmentViewClicked(View v) {
        TranslatorFragment fr = (TranslatorFragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(0));
        fr.clicked(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == TranslatorFragment.newLanguageCheckResult) {
            TranslatorFragment fr = (TranslatorFragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(0));
            fr.manageResultData(data);
        }
    }

    private String getFragmentTag(int fragmentPosition)
    {
        return "android:switcher:" + mViewPager.getId() + ":" + fragmentPosition;
    }

    /**
     * Реализуется фрагментами, обрабатывающими нажатия на объекты пользовательского интерфейса
     */
    public interface ContainsClickable {
        void clicked(View v);
    }

    public interface OnGoToTranslatorFragmentListener {
        void goToTranslatorFragment(TranslationModel currentTranslation);
    }
}
