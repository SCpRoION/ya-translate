package ru.ya.translate.translator;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import ru.ya.translate.BasePresenter;
import ru.ya.translate.R;
import ru.ya.translate.translation.TranslationModel;
import ru.ya.translate.translation.TranslationsStorage;
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

    private final TranslatorView view;                          /** Представление переводчика */
    private String fromLanguageKey;                             /** Исходный язык */
    private String toLanguageKey;                               /** Язык перевода */
    private TranslationModel lastTranslation;                   /** Информация о последнем переводе */
    private long lastTranslationTime;                           /** Время последнего перевода */
    private final long pauseBeforeSavingLastTranslation = 2500; /** Временная пауза перед сохранением последнего перевода при отсутствии изменений */
    private Subscription curSubscription;                       /** Текущий подписчик перевода */

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
                    } catch (TranslatorAPI.APIException e) {
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
     * Установить состояние фрагмента
     * @param translation сохраненный перевод
     */
    public void setState(TranslationModel translation) {
        fromLanguageKey = translation.getFromLanguageKey();
        toLanguageKey = translation.getToLanguageKey();

        view.setFromLanguage(TranslatorAPIManager.getTranslationAPI().getLanguageName(fromLanguageKey));
        view.setToLanguage(TranslatorAPIManager.getTranslationAPI().getLanguageName(toLanguageKey));
        view.setTextFrom(translation.getTextFrom());
        view.setTranslation(translation.getTranslation());
    }

    /**
     * Фокус поля ввода текста потерян
     */
    public void editTextFocusLost() {
        saveLastTranslation();
    }

    /**
     * Изменен исходный язык
     *
     * @param newLanguageKey ключ нового исходного языка
     */
    public void fromLanguageChangedTo(String newLanguageKey) {
        fromLanguageKey = newLanguageKey;
        translate(view.getTranslatingText());
    }

    /**
     * Изменен язык перевода
     *
     * @param newLanguageKey ключ нового языка перевода
     */
    public void toLanguageChangedTo(String newLanguageKey) {
        toLanguageKey = newLanguageKey;
        translate(view.getTranslatingText());
    }

    /**
     * Языки оригинала и перевода поменяли местами
     */
    public void languagesSwapped() {
        String tmp = toLanguageKey;
        toLanguageKey = fromLanguageKey;
        fromLanguageKey = tmp;
        translate(view.getTranslatingText());
    }

    /**
     * Текст поля ввода был изменен
     *
     * @param newText новый текст поля ввода
     */
    public void inputTextChanged(String newText) {
        translate(newText);
    }

    /**
     * Перевести строку
     *
     * @param s строка для перевода
     */
    private void translate(final String s) {
        // Удалим пустые пробелы в начале и в конце
        final String trimmedS = s.trim();

        // Проверить, нужно ли сохранить последний перевод в историю
        if (lastTranslation != null && System.currentTimeMillis() - lastTranslationTime >= pauseBeforeSavingLastTranslation) {
            saveLastTranslation();
        }

        // Отписать предыдущего подписчика, так как он ждет старый перевод
        if (curSubscription != null && !curSubscription.isUnsubscribed()) {
            curSubscription.unsubscribe();
        }

        // Если строка пустая, то переводить не надо
        if (TextUtils.isEmpty(trimmedS)) {
            view.setTranslation("");
            return;
        }

        // Подписаться на новый перевод
        curSubscription = translationObservable(trimmedS)
                .subscribe(
                        translation -> {
                            view.setTranslation(translation);
                            lastTranslation = new TranslationModel(trimmedS, translation, fromLanguageKey, toLanguageKey);
                            lastTranslationTime = System.currentTimeMillis();
                        },
                        exception -> {
                            if (exception != null && exception.getMessage() != null) {
                                Log.e("TRANSLATION", exception.getMessage());
                            }
                        },
                        () -> {
                        });
    }

    private Observable<String> translationObservable(String from) {
        return Observable.just(from)
                .observeOn(Schedulers.io())
                .map(str -> {
                    String res = null;
                    try {
                        res = TranslatorAPIManager.getTranslationAPI().translate(str, fromLanguageKey, toLanguageKey);
                    } catch (TranslatorAPI.APIException exception) {
                        Log.e("TRANSLATION", exception.getMessage());
                    }
                    return res;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Сохранить последний произведенный перевод
     */
    private void saveLastTranslation() {
        if (lastTranslation != null) {
            TranslationsStorage.getInstance().add(lastTranslation);
            lastTranslation = null;
        }
    }
}