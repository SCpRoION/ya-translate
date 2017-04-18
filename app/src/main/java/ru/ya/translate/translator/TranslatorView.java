package ru.ya.translate.translator;

/**
 * Created by Kamo Spertsyan on 17.03.2017.
 */

public interface TranslatorView {

    /**
     * Показать всплывающее сообщение
     * @param messageId идентификатор сообщения из строковых ресурсов
     */
    void showToast(int messageId);

    /**
     * Установить перевод
     * @param translation перевод текста
     */
    void setTranslation(String translation);

    /**
     * Установить название языка перевола
     * @param name название языка перевода
     */
    void setFromLanguage(String name);

    /**
     * Установить название языка источника
     * @param name название языка источника
     */
    void setToLanguage(String name);

    /**
     * Узнать переводимый текст
     * @return переводимый текст
     */
    String getTranslatingText();
}
