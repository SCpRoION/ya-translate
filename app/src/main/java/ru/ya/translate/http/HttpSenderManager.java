package ru.ya.translate.http;

/**
 * Created by Kamo Spertsyan on 17.03.2017.
 */
public class HttpSenderManager {

    private static HttpSender current = new OkHttpSender();

    public static HttpSender getHttpSender() {
        return current;
    }
}
