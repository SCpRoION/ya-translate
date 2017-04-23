package ru.ya.translate.translation;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by kamospertsyan on 18.04.17.
 */
public class TranslationsStorage {

    private final TranslationDatabaseHelper dbHelper;       /** Адаптер базы данных */
    private ArrayList<TranslationModel> translations;       /** История переводов */

    private static TranslationsStorage instance;             /** Единственный экземпляр класса */

    public static TranslationsStorage getInstance() {
        return instance;
    }

    /**
     * Инициализация хранителя переводов
     * @param context
     */
    public static void init(Context context) {
        instance = new TranslationsStorage(context);
    }

    private TranslationsStorage(Context context) {
        dbHelper = new TranslationDatabaseHelper(context);
        translations = new ArrayList<>();
        loadAll();
    }

    /**
     * Запомнить новый перевод
     * @param translation запоминаемый перевод
     */
    public void add(TranslationModel translation) {
        if (translations.contains(translation)) {
            return;
        }

        translations.add(translation);
        dbHelper.insertTranslation(translation);
    }

    /**
     * Загрузить все записи
     */
    public void loadAll() {
        translations = new ArrayList<>();
        translations.addAll(dbHelper.getAllTranslations());
    }
}
