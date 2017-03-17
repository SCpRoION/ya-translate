package ru.ya.translate.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Kamo Spertsyan on 17.03.2017.
 */
public class OkHttpSender implements HttpSender {

    private static OkHttpClient client = new OkHttpClient();

    @Override
    public String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
        catch (IOException e) {
            Log.e("GET REQUEST EXCEPTION", e.getMessage());
        }

        return null;
    }

    @Override
    public String post(String url) {
        return post(url, "");
    }

    @Override
    public String post(String url, String body) {
        return post(url, body, "application/json", "utf-8");
    }

    @Override
    public String post(String url, String body, String contentType, String charset) {
        MediaType type = MediaType.parse(contentType + "; charset=" + charset);
        RequestBody requestBody = RequestBody.create(type, body);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
        catch (IOException e) {
            Log.e("GET REQUEST EXCEPTION", e.getMessage());
        }

        return null;
    }
}
