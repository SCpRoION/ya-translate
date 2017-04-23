package ru.ya.translate.translation;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by kamospertsyan on 18.04.17.
 */
public class TranslationsStorage {

    private final TranslationDatabaseHelper dbHelper;       /** Адаптер базы данных */
    private ArrayList<TranslationModel> translations;       /** История переводов */
    private ArrayList<OnTranslationAddedListener> listeners;/** Слушатели события добавления нового перевода */

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
        listeners = new ArrayList<>();
        loadAll();
    }

    /**
     * Добавить слушателя события добавления нового перевода
     * @param listener слушатель
     */
    public void addOnTranslationAddedListener(OnTranslationAddedListener listener) {
        listeners.add(listener);
    }

    /**
     * Сообщить о добавлении нового перевода
     * @param translation
     */
    private void fireTranslationAdded(TranslationModel translation) {
        for (OnTranslationAddedListener listener : listeners) {
            listener.translationAdded(translation);
        }
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
        fireTranslationAdded(translation);
    }

    /**
     * Добавить/удалить из избранного
     * @param translation перевод
     */
    public void switchFavorite(TranslationModel translation) {
        dbHelper.updateTranslation(translation);
    }

    /**
     * Загрузить все записи
     */
    public void loadAll() {
        translations = new ArrayList<>();
        translations.addAll(dbHelper.getAllTranslations());
    }

    /**
     * Получить список текущих переводов в истории
     * @return список текущих сохраненных переводов
     */
    public ArrayList<TranslationModel> getTranslations() {
        return translations;
    }

    public interface OnTranslationAddedListener {
        void translationAdded(TranslationModel translation);
    }
}
