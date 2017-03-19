package ru.ya.translate.languagelist;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.Collection;
import java.util.List;

import ru.ya.translate.R;
import ru.ya.translate.translator.TranslatorActivity;

public class LanguageListActivity extends ListActivity implements LanguageListView {

    private boolean isLanguageFrom;             /** Флаг выбираемого языка (истина, если язык источника) */
    private LanguageListPresenter presenter;    /** Презентер */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLanguageFrom = getIntent().getExtras().getBoolean(TranslatorActivity.isLanguageFromBundleKey);

        // Создаём презентер
        presenter = new LanguageListPresenter(this);
        presenter.onCreate();
    }

    @Override
    public void showLanguageList(List<String> languages) {
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        getListView().setAdapter(listAdapter);
    }
}
