package ru.ya.translate.translator.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import ru.ya.translate.http.HttpSenderManager;

/**
 * Created by Kamo Spertsyan on 17.03.2017.
 */
public class YandexTranslatorAPI extends TranslatorAPI {

    private String key = "trnsl.1.1.20170316T200319Z.b7de206044a19ca2.e8a2b023f20a23dc66001a85b6280739b93ac317";

    @Override
    public void initialize() throws APIException {
        String response = HttpSenderManager.getHttpSender().post(
            new String("https://translate.yandex.net/api/v1.5/tr.json/getLangs?ui=%1&key=%2")
                .replace("%1", interfaceLanguageKey)
                .replace("%2", key));
        try {
            JSONObject obj = new JSONObject(response);
            // Если словили ошибку - выбрасываем исключение
            if (obj.has("message")) {
                throw new APIException(obj.getString("message"));
            }
            // В противном случае парсим ответ
            else {
                JSONObject langs = obj.getJSONObject("langs");
                Iterator<String> keys = langs.keys();
                String curKey;
                while (keys.hasNext()) {
                    curKey = keys.next();
                    supportedLanguages.put(curKey, langs.getString(curKey));
                }
                initialized = true;
            }
        }
        catch (JSONException e) {
            Log.e("JSON PARSING", e.getMessage());
        }
    }

    @Override
    public String translate(String text, String from, String to) throws APIException {
        String translation = null;

        try {
            // Подготавливаем тело запроса
            String bodyStr = "text=" + text;

            // Запрашиваем ответ
            String response = HttpSenderManager.getHttpSender().post(
                    new String("https://translate.yandex.net/api/v1.5/tr.json/translate?lang=%1-%2&key=%3")
                            .replace("%1", from)
                            .replace("%2", to)
                            .replace("%3", key),
                    bodyStr,
                    "application/x-www-form-urlencoded",
                    "utf-8");
            JSONObject obj = new JSONObject(response);

            // Если словили ошибку - выбрасываем исключение
            if (obj.has("message")) {
                throw new APIException(obj.getString("message"));
            }
            // В противном случае парсим ответ
            else {
                JSONArray translationJson = obj.getJSONArray("text");
                translation = translationJson.getString(0);
            }
        }
        catch (JSONException e) {
            Log.e("JSON PARSING", e.getMessage());
        }

        return translation;
    }
}
