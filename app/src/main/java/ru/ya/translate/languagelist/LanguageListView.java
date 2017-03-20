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

    /**
     * Отправить информацию о выбранном языке активности переводчика
     * @param newLanguageKey ключ нового выбранного языка
     * @param newLanguageTitle название нового выбранного языка
     */
    void sendNewLanguageToTranslator(String newLanguageKey, String newLanguageTitle);
}
