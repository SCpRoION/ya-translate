package ru.ya.translate.translator;

import android.os.AsyncTask;
import android.util.Log;

import ru.ya.translate.BasePresenter;
import ru.ya.translate.R;
import ru.ya.translate.translator.api.TranslatorAPI;
import ru.ya.translate.translator.api.TranslatorAPIManager;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kamo Spertsyan on 17.03.2017.
 */
public class TranslatorPresenter implements BasePresenter {

    private final TranslatorView view;          /** Представление переводчика */
    private String fromLanguageKey;             /** Исходный язык */
    private String toLanguageKey;               /** Язык перевода */

    private Subscription curSubscription;       /** Текущий подписчик перевода */

    public TranslatorPresenter(TranslatorView view) {
        this.view = view;
        fromLanguageKey = "ru";
        toLanguageKey = "en";
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
                    } else {
                        view.setFromLanguage(TranslatorAPIManager.getTranslationAPI().getLanguageName(fromLanguageKey));
                        view.setToLanguage(TranslatorAPIManager.getTranslationAPI().getLanguageName(toLanguageKey));
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
        translate(newText);
    }

    /**
     * Перевести строку
     * @param s строка для перевода
     */
    private void translate(final String s) {
        // Отписать предыдущего подписчика, так как он ждет старый перевод
        if (curSubscription != null && !curSubscription.isUnsubscribed()) {
            curSubscription.unsubscribe();
        }

        // Если строка пустая, то переводить не надо
        if (s == null || s.isEmpty()) {
            view.setTranslation("");
            return;
        }

        // Подписаться на новый перевод
        curSubscription = translationObservable(s)
                .subscribe(
                        translation -> view.setTranslation(translation),
                        exception -> Log.d("TRANSLATION", exception.getMessage()),
                        () -> {});
    }

    private Observable<String> translationObservable(String from) {
        return Observable.just(from)
                .observeOn(Schedulers.newThread())
                .map(str -> {
                    String res = null;
                    try {
                        res = TranslatorAPIManager.getTranslationAPI().translate(str, fromLanguageKey, toLanguageKey);
                    } catch (TranslatorAPI.APIException exception) {
                        Log.e("TRANSLATION", exception.getMessage());
                    }
                    return res;
                })
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}
