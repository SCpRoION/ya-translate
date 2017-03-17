package ru.ya.translate.http;

/**
 * Created by Kamo Spertsyan on 17.03.2017.
 */

public interface HttpSender {

    /**
     * Get-запрос
     * @param url ссылка
     * @return ответ сервера
     */
    public String get(String url);

    /**
     * Post-запрос без тела
     * @param url ссылка
     * @return ответ сервера
     */
    public String post(String url);

    /**
     * Post-запрос
     * @param url ссылка
     * @param body тело запроса (тип - JSON, utf8)
     * @return ответ сервера
     */
    public String post(String url, String body);

    /**
     * Post-запрос
     * @param url ссылка
     * @param body тело запроса
     * @param contentType тип содержимого
     * @param charset кодировка
     * @return ответ сервера
     */
    public String post(String url, String body, String contentType, String charset);
}
