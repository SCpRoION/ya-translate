package ru.ya.translate.translator.api;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Kamo Spertsyan on 17.03.2017.
 */
public abstract class TranslatorAPI {

    protected Map<String, String> supportedLanguages; /** Поддерживаемые языки */
    protected boolean initialized = false;               /** Флаг инициализации */

    // В рамках тестового задания, я надеюсь, нет необходимости поддерживать мультиязычный интерфейс,
    // поэтому я ограничусь русским и помещу ключ сюда, хотя, конечно, ему место не совсем здесь.
    protected final String interfaceLanguageKey = "ru"; /** Язык интерфейса для названий языков */

    public TranslatorAPI() {
        supportedLanguages = new LinkedHashMap<>();
    }

    /**
     * Инициализация API
     * @throws APIException ошибка при иниициализации
     */
    public abstract void initialize() throws APIException;

    /**
     * Узнать, инициализировано ли API
     * @return признак пройденной инициализации
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Список поддерживаемых языков
     * @return список наименований поддерживаемых языков
     */
    public Collection<String> supportedLanguages() {
        return supportedLanguages.values();
    }

    /**
     * Исключения в работе API
     */
    public class APIException extends Exception {

        public APIException() {
            super();
        }

        public APIException(String message) {
            super(message);
        }
    }
}
