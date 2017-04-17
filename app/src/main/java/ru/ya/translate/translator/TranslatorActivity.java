package ru.ya.translate.translator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.ya.translate.R;
import ru.ya.translate.languagelist.LanguageListActivity;

public class TranslatorActivity extends AppCompatActivity implements TranslatorView {

    /** Ключи для используемых полей контейнеров */
    public static String isLanguageFromBundleKey = "isLanguageFrom";
    public static String newSelectedLanguageKeyBundleKey = "newSelectedLanguageKey";
    public static String newSelectedLanguageTitleBundleKey = "newSelectedLanguageTitle";
    private final String translatingStringBundleKey = "translatingString";
    private final String translationStringBundleKey = "translationString";

    private TranslatorBasePresenter presenter;          /** Презентер */
    private EditText translatingTextEdit;           /** Поле ввода текста перевода */
    private TextView translationTextView;           /** Поле вывода текста перевода */
    private Button fromLanguageButton;              /** Кнопка исходного языка */
    private Button toLanguageButton;                /** Кнопка языка перевода */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        translationTextView = (TextView) findViewById(R.id.translation);
        translatingTextEdit = (EditText) findViewById(R.id.text_to_translate);
        fromLanguageButton = (Button) findViewById(R.id.from_language);
        toLanguageButton = (Button) findViewById(R.id.to_language);

        // Создаём презентер
        presenter = new TranslatorBasePresenter(this);
        presenter.onCreate();

        // Восстанавливаем сохраненное состояние
        if (savedInstanceState != null) {
            translatingTextEdit.setText(savedInstanceState.getString(translatingStringBundleKey));
            translationTextView.setText(savedInstanceState.getString(translationStringBundleKey));
        }

        // Обработать данные, переданные вместе с интентом
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(isLanguageFromBundleKey)) {
            boolean isLanguageFromChanged = extras.getBoolean(isLanguageFromBundleKey);
            String newLanguageKey = extras.getString(newSelectedLanguageKeyBundleKey);
            String newLanguageTitle = extras.getString(newSelectedLanguageTitleBundleKey);
            if (isLanguageFromChanged) {
                presenter.fromLanguageChangedTo(newLanguageKey);
                setFromLanguage(newLanguageTitle);
            }
            else {
                presenter.toLanguageChangedTo(newLanguageKey);
                setToLanguage(newLanguageTitle);
            }
        }

        // Прослушиваем ввод текста
        translatingTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.inputTextChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString(translatingStringBundleKey, translatingTextEdit.getText().toString());
        bundle.putString(translationStringBundleKey, translationTextView.getText().toString());
    }

    @Override
    public void showToast(int messageId) {
        Toast.makeText(this, getString(messageId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setTranslation(String translation) {
        translationTextView.setText(translation);
    }

    private void setFromLanguage(String name) {
        fromLanguageButton.setText(name);
    }

    private void setToLanguage(String name) {
        toLanguageButton.setText(name);
    }

    /**
     * Нажата кнопка исходного языка
     * @param view нажатая кнопка
     */
    public void onFromLanguageButtonClicked(View view) {
        Intent intent = new Intent(TranslatorActivity.this, LanguageListActivity.class);
        intent.putExtra(isLanguageFromBundleKey, true);
        startActivity(intent);
    }

    /**
     * Нажата кнопка смены языков местами
     * @param view нажатая кнопка
     */
    public void onSwapLanguagesButtonClicked(View view) {
        presenter.languagesSwapped();
        String fromLanguageTitle = fromLanguageButton.getText().toString();
        setFromLanguage(toLanguageButton.getText().toString());
        setToLanguage(fromLanguageTitle);
    }

    /**
     * Нажата кнопка языка перевода
     * @param view нажатая кнопка
     */
    public void onToLanguageButtonClicked(View view) {
        Intent intent = new Intent(TranslatorActivity.this, LanguageListActivity.class);
        intent.putExtra(isLanguageFromBundleKey, false);
        startActivity(intent);
    }
}
