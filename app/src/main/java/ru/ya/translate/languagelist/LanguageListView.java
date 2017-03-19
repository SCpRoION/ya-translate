package ru.ya.translate.languagelist;

import java.util.List;

/**
 * Created by Kamo Spertsyan on 19.03.2017.
 */
public interface LanguageListView {

    /**
     * Показать список языков
     * @param languages список языков
     */
    void showLanguageList(List<String> languages);
}
