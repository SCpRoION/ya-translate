package ru.ya.translate.translation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by kamospertsyan on 18.04.17.
 */
public class TranslationDatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "translator";     /** Имя БД */
    private static final int DB_VERSION = 1;                /** Версия БД */

    private static final String TRANSLATION_TABLE_NAME = "TRANSLATION";
    private static final String TRANSLATION_TABLE_ID = "_id";
    private static final String TRANSLATION_TABLE_TEXT_FROM = "TEXT_FROM";
    private static final String TRANSLATION_TABLE_TRANSLATION = "TRANSLATION";
    private static final String TRANSLATION_TABLE_FROM_LANGUAGE = "FROM_LANGUAGE";
    private static final String TRANSLATION_TABLE_TO_LANGUAGE = "TO_LANGUAGE";
    private static final String TRANSLATION_TABLE_IS_FAVORITE = "IS_FAVORITE";

    public TranslationDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TRANSLATION_TABLE_NAME + " (" +
                TRANSLATION_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TRANSLATION_TABLE_TEXT_FROM + " TEXT," +
                TRANSLATION_TABLE_TRANSLATION + " TEXT," +
                TRANSLATION_TABLE_FROM_LANGUAGE + " TEXT," +
                TRANSLATION_TABLE_TO_LANGUAGE + " TEXT," +
                TRANSLATION_TABLE_IS_FAVORITE + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Сохранить перевод в БД
     * @param translation сохраняемый перевод
     */
    public void insertTranslation(TranslationModel translation) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSLATION_TABLE_TEXT_FROM, translation.getTextFrom());
        contentValues.put(TRANSLATION_TABLE_TRANSLATION, translation.getTranslation());
        contentValues.put(TRANSLATION_TABLE_FROM_LANGUAGE, translation.getFromLanguageKey());
        contentValues.put(TRANSLATION_TABLE_TO_LANGUAGE, translation.getToLanguageKey());
        contentValues.put(TRANSLATION_TABLE_IS_FAVORITE, translation.isFavorite() ? 1 : 0);

        int id = (int) getWritableDatabase().insert(TRANSLATION_TABLE_NAME, null, contentValues);
        translation.setId(id);
    }

    /**
     * Обновить перевод в БД
     * @param translation обновляемый перевод
     */
    public void updateTranslation(TranslationModel translation) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSLATION_TABLE_TEXT_FROM, translation.getTextFrom());
        contentValues.put(TRANSLATION_TABLE_TRANSLATION, translation.getTranslation());
        contentValues.put(TRANSLATION_TABLE_FROM_LANGUAGE, translation.getFromLanguageKey());
        contentValues.put(TRANSLATION_TABLE_TO_LANGUAGE, translation.getToLanguageKey());
        contentValues.put(TRANSLATION_TABLE_IS_FAVORITE, translation.isFavorite() ? 1 : 0);

        getWritableDatabase().update(TRANSLATION_TABLE_NAME,
                contentValues,
                TRANSLATION_TABLE_ID + " = ?",
                new String[] { Integer.toString(translation.getId()) });
    }

    /**
     * Удалить перевод из БД
     * @param translation удаляемый перевод
     */
    public void deleteTranslation(TranslationModel translation) {
        getWritableDatabase().delete(TRANSLATION_TABLE_NAME, TRANSLATION_TABLE_ID + " = ?", new String[] {Integer.toString(translation.getId())});
    }

    /**
     * Получить все переводы из БД
     * @return список всех переводов в БД
     */
    public ArrayList<TranslationModel> getAllTranslations() {
        ArrayList<TranslationModel> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TRANSLATION_TABLE_NAME,
                new String[] {
                        TRANSLATION_TABLE_ID,
                        TRANSLATION_TABLE_TEXT_FROM,
                        TRANSLATION_TABLE_TRANSLATION,
                        TRANSLATION_TABLE_FROM_LANGUAGE,
                        TRANSLATION_TABLE_TO_LANGUAGE,
                        TRANSLATION_TABLE_IS_FAVORITE
                }, null, null, null, null, null);

        while (cursor.moveToNext()) {
            res.add(translationFromCursor(cursor));
        }

        return res;
    }

    /**
     * Сформировать объект перевода из курсора
     * @param cursor курсор
     * @return объект перевода
     */
    private TranslationModel translationFromCursor(Cursor cursor) {
        int id =             cursor.getInt   (0);
        String textFrom =    cursor.getString(1);
        String translation = cursor.getString(2);
        String fromLang =    cursor.getString(3);
        String toLang =      cursor.getString(4);
        boolean isFavorite = cursor.getInt   (5) == 1;

        TranslationModel t  = new TranslationModel(id, textFrom, translation, fromLang, toLang, isFavorite);
        return t;
    }
}
