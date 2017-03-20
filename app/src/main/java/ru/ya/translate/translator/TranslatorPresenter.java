package ru.ya.translate.translator;

import android.os.AsyncTask;
import android.util.Log;

import ru.ya.translate.Presenter;
import ru.ya.translate.R;
import ru.ya.translate.translator.api.TranslatorAPI;
import ru.ya.translate.translator.api.TranslatorAPIManager;

/**
 * Created by Kamo Spertsyan on 17.03.2017.
 */
public class TranslatorPresenter implements Presenter {

    private final TranslatorView view;          /** Представление переводчика */
    private String fromLanguageKey;             /** Исходный язык */
    private String toLanguageKey;               /** Язык перевода */

    public TranslatorPresenter(TranslatorView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        // Инициализировать API, если это ещё не было сделано
        if (!TranslatorAPIManager.getTranslationAPI().isInitialized()) {
            new AsyncTask<Void, Void, Void>() {

                private TranslatorAPI.APIException exception;      // Исключение, возникшее при инициализации API

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        TranslatorAPIManager.getTranslationAPI().initialize();
                    }
                    catch(TranslatorAPI.APIException e) {
                        exception = e;
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    if (exception != null) {
                        Log.e("API INIT", exception.getMessage());
                        view.showToast(R.string.api_initialization_error);
                    }
                }
            }.execute();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    /**
     * Изменен исходный язык
     * @param newLanguageKey ключ нового исходного языка
     */
    public void fromLanguageChangedTo(String newLanguageKey) {
        fromLanguageKey = newLanguageKey;
    }

    /**
     * Изменен язык перевода
     * @param newLanguageKey ключ нового языка перевода
     */
    public void toLanguageChangedTo(String newLanguageKey) {
        toLanguageKey = newLanguageKey;
    }

    /**
     * Языки оригинала и перевода поменяли местами
     */
    public void languagesSwapped() {
        String tmp = toLanguageKey;
        toLanguageKey = fromLanguageKey;
        fromLanguageKey = tmp;
    }

    /**
     * Текст поля ввода был изменен
     * @param newText новый текст поля ввода
     */
    public void inputTextChanged(String newText) {
        Log.d("TEXT CHANGED", newText);
    }
}
