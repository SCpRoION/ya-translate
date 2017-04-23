package ru.ya.translate.translation;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by kamospertsyan on 18.04.17.
 */
public class TranslationsStorage {

    private final TranslationDatabaseHelper dbHelper;                                                   /** Адаптер базы данных */
    private ArrayList<TranslationModel> translations;                                                   /** История переводов */
    private ArrayList<OnTranslationAddedListener> onTranslationAddedListeners;                          /** Слушатели события добавления нового перевода */
    private ArrayList<OnTranslationFavoriteSwitchedListener> onTranslationFavoriteSwitchedListeners;    /** Слушатели события изменения флага избранного перевода */
    private ArrayList<OnTranslationRemovedListener> onTranslationRemovedListeners;                      /** Слушатели события удаления перевода */

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
        onTranslationAddedListeners = new ArrayList<>();
        onTranslationFavoriteSwitchedListeners = new ArrayList<>();
        onTranslationRemovedListeners = new ArrayList<>();
        loadAll();
    }

    /**
     * Добавить слушателя события добавления нового перевода
     * @param listener слушатель
     */
    public void addOnTranslationAddedListener(OnTranslationAddedListener listener) {
        onTranslationAddedListeners.add(listener);
    }

    /**
     * Сообщить о добавлении нового перевода
     * @param translation
     */
    private void fireTranslationAdded(TranslationModel translation) {
        for (OnTranslationAddedListener listener : onTranslationAddedListeners) {
            listener.translationAdded(translation);
        }
    }

    /**
     * Добавить слушателя события добавления нового перевода
     * @param listener слушатель
     */
    public void addOnTranslationFavoriteSwitchedListener(OnTranslationFavoriteSwitchedListener listener) {
        onTranslationFavoriteSwitchedListeners.add(listener);
    }

    /**
     * Сообщить о добавлении нового перевода
     * @param translation новый перевод
     */
    private void fireTranslationFavoriteSwitched(TranslationModel translation) {
        for (OnTranslationFavoriteSwitchedListener listener : onTranslationFavoriteSwitchedListeners) {
            listener.translationFavoriteSwitched(translation);
        }
    }

    /**
     * Добавить слушателя события удаления перевода
     * @param listener слушатель
     */
    public void addOnTranslationRemovedListener(OnTranslationRemovedListener listener) {
        onTranslationRemovedListeners.add(listener);
    }

    /**
     * Сообщить об удалении перевода
     * @param translation удаленный перевод
     */
    private void fireTranslationRemoved(TranslationModel translation) {
        for (OnTranslationRemovedListener listener : onTranslationRemovedListeners) {
            listener.translationRemoved(translation);
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
        translation.setFavorite(!translation.isFavorite());
        dbHelper.updateTranslation(translation);

        fireTranslationFavoriteSwitched(translation);
    }

    public void remove(TranslationModel translation) {
        translations.remove(translation);
        dbHelper.deleteTranslation(translation);

        fireTranslationRemoved(translation);
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
        return (ArrayList<TranslationModel>) translations.clone();
    }


    /**
     * Получить список текущих избранных переводов
     * @return список текущих избранных переводов
     */
    public ArrayList<TranslationModel> getFavoriteTranslations() {
        ArrayList<TranslationModel> res = new ArrayList<>();
        for (TranslationModel tm : translations) {
            if (tm.isFavorite()) {
                res.add(tm);
            }
        }
        return res;
    }

    public interface OnTranslationAddedListener {
        void translationAdded(TranslationModel translation);
    }

    public interface OnTranslationFavoriteSwitchedListener {
        void translationFavoriteSwitched(TranslationModel translation);
    }

    public interface OnTranslationRemovedListener {
        void translationRemoved(TranslationModel translation);
    }
}
