package ru.ya.translate;

/**
 * Created by Kamo Spertsyan on 17.03.2017.
 */
public interface Presenter {

    /**
     * Методы жизненного цикла
     */
    abstract void onCreate();
    abstract void onPause();
    abstract void onResume();
    abstract void onDestroy();
}
