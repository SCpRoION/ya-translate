package ru.ya.translate.languagelist;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import ru.ya.translate.translator.TranslatorActivity;

public class LanguageListActivity extends ListActivity implements LanguageListView {

    private boolean isLanguageFrom;             /** Флаг выбираемого языка (истина, если язык источника) */
    private LanguageListBasePresenter presenter;    /** Презентер */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLanguageFrom = getIntent().getExtras().getBoolean(TranslatorActivity.isLanguageFromBundleKey);

        // Создаём презентер
        presenter = new LanguageListBasePresenter(this);
        presenter.onCreate();
    }

    @Override
    public void showLanguageList(List<String> languages) {
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        getListView().setAdapter(listAdapter);
    }

    @Override
    public void sendNewLanguageToTranslator(String newLanguageKey, String newLanguageTitle) {
        Intent intent = new Intent(LanguageListActivity.this, TranslatorActivity.class);
        intent.putExtra(TranslatorActivity.isLanguageFromBundleKey, isLanguageFrom);
        intent.putExtra(TranslatorActivity.newSelectedLanguageKeyBundleKey, newLanguageKey);
        intent.putExtra(TranslatorActivity.newSelectedLanguageTitleBundleKey, newLanguageTitle);
        startActivity(intent);
    }

    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id) {
        presenter.newLanguageSelected((String)listView.getItemAtPosition(position));
    }
}
