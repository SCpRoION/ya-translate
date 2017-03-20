package ru.ya.translate.languagelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ya.translate.Presenter;
import ru.ya.translate.translator.TranslatorActivity;
import ru.ya.translate.translator.api.TranslatorAPIManager;

/**
 * Created by Kamo Spertsyan on 19.03.2017.
 */
public class LanguageListPresenter implements Presenter {

    private LanguageListView view;              /** Представление списка языков */

    public LanguageListPresenter(LanguageListView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        List<String> languages = new ArrayList<>(TranslatorAPIManager.getTranslationAPI().supportedLanguages());
        Collections.sort(languages);
        view.showLanguageList(languages);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    /**
     * Выбран новый язык из перечня
     * @param languageTitle новый выбранный язык
     */
    public void newLanguageSelected(String languageTitle) {
        String languageKey = TranslatorAPIManager.getTranslationAPI().getLanguageKey(languageTitle);
        view.sendNewLanguageToTranslator(languageKey, languageTitle);
    }
}
