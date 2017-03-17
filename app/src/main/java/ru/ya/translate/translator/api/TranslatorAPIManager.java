package ru.ya.translate.translator.api;

/**
 * Created by Kamo Spertsyan on 17.03.2017.
 */
public class TranslatorAPIManager {

    private static TranslatorAPI current = new YandexTranslatorAPI();

    public static TranslatorAPI getTranslationAPI() {
        return current;
    }
}
