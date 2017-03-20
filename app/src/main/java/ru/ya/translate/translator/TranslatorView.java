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
}
