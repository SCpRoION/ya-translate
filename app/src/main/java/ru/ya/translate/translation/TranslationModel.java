package ru.ya.translate.translation;

import android.content.ContentValues;

/**
 * Created by kamospertsyan on 18.04.17.
 */

public class TranslationModel {

    private int id;                     /** Идентификатор записи */
    private String textFrom;            /** Переводимый текст */
    private String translation;         /** Перевод текста */
    private String fromLanguageKey;     /** Ключ языка оригинала */
    private String toLanguageKey;       /** Ключ языка перевода */
    private boolean isFavorite;         /** Флаг наличия в избранном */

    public TranslationModel(String textFrom, String translation, String fromLanguageKey, String toLanguageKey) {
        this(-1, textFrom, translation, fromLanguageKey, toLanguageKey, false);
    }

    public TranslationModel(int id, String textFrom, String translation, String fromLanguageKey, String toLanguageKey, boolean isFavorite) {
        this.id = id;
        this.textFrom = textFrom;
        this.translation = translation;
        this.fromLanguageKey = fromLanguageKey;
        this.toLanguageKey = toLanguageKey;
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextFrom() {
        return textFrom;
    }

    public void setTextFrom(String textFrom) {
        this.textFrom = textFrom;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getFromLanguageKey() {
        return fromLanguageKey;
    }

    public void setFromLanguageKey(String fromLanguageKey) {
        this.fromLanguageKey = fromLanguageKey;
    }

    public String getToLanguageKey() {
        return toLanguageKey;
    }

    public void setToLanguageKey(String toLanguageKey) {
        this.toLanguageKey = toLanguageKey;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(Object other) {
        if (! (other instanceof TranslationModel)) {
            return false;
        }

        TranslationModel otherTM = (TranslationModel) other;
        return getTextFrom().equals(otherTM.textFrom) &&
                getFromLanguageKey().equals(otherTM.getFromLanguageKey()) &&
                getToLanguageKey().equals(otherTM.getToLanguageKey());
    }
}
