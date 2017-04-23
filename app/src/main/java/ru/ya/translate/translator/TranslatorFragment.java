package ru.ya.translate.translator;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.ya.translate.MainActivity;
import ru.ya.translate.R;
import ru.ya.translate.languagelist.LanguageListActivity;
import ru.ya.translate.translation.TranslationModel;

public class TranslatorFragment extends Fragment implements TranslatorView, MainActivity.ContainsClickable {

    /** Ключи для используемых полей контейнеров */
    public static String isLanguageFromBundleKey =              "isLanguageFrom";
    public static String newSelectedLanguageKeyBundleKey =      "newSelectedLanguageKey";
    public static String newSelectedLanguageTitleBundleKey =    "newSelectedLanguageTitle";
    private final String fromLanguageBundleKey =                "fromLanguage";
    private final String toLanguageBundleKey =                  "toLanguage";
    private final String translatingStringBundleKey =           "translatingString";
    private final String translationStringBundleKey =           "translationString";

    public static int newLanguageCheckResult = 0x02;/** Требуемый код результата при выборе нового языка оригинала/перевода */

    private TranslatorPresenter presenter;          /** Презентер */
    private EditText translatingTextEdit;           /** Поле ввода текста перевода */
    private TextView translationTextView;           /** Поле вывода текста перевода */
    private Button fromLanguageButton;              /** Кнопка исходного языка */
    private Button toLanguageButton;                /** Кнопка языка перевода */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translator, container, false);
        translationTextView = (TextView) rootView.findViewById(R.id.translation);
        translatingTextEdit = (EditText) rootView.findViewById(R.id.text_to_translate);
        fromLanguageButton = (Button) rootView.findViewById(R.id.from_language);
        toLanguageButton = (Button) rootView.findViewById(R.id.to_language);

        // Создаём презентер
        presenter = new TranslatorPresenter(this);
        presenter.onCreate();

        // Восстанавливаем сохраненное состояние
        if (savedInstanceState != null) {
            translatingTextEdit.setText(savedInstanceState.getString(translatingStringBundleKey));
            translationTextView.setText(savedInstanceState.getString(translationStringBundleKey));
            setFromLanguage(savedInstanceState.getString(fromLanguageBundleKey));
            setToLanguage(savedInstanceState.getString(toLanguageBundleKey));
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

        translatingTextEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                presenter.editTextFocusLost();
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(translatingStringBundleKey, translatingTextEdit.getText().toString());
        bundle.putString(translationStringBundleKey, translationTextView.getText().toString());
        bundle.putString(fromLanguageBundleKey, fromLanguageButton.getText().toString());
        bundle.putString(toLanguageBundleKey, toLanguageButton.getText().toString());
    }

    @Override
    public void showToast(int messageId) {
        Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setTextFrom(String textFrom) {
        translatingTextEdit.setText(textFrom);
        translatingTextEdit.setSelection(textFrom.length());
    }

    @Override
    public void setTranslation(String translation) {
        translationTextView.setText(translation);
    }

    @Override
    public void setFromLanguage(String name) {
        fromLanguageButton.setText(name);
    }

    @Override
    public void setToLanguage(String name) {
        toLanguageButton.setText(name);
    }

    @Override
    public String getTranslatingText() {
        return translatingTextEdit.getText().toString();
    }

    /**
     * Обработать данные, переданные активностью, вызвавшей startActivityForResult
     * @param intent интент, содержащий данные
     */
    public void manageResultData(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey(isLanguageFromBundleKey)) {
            boolean isLanguageFromChanged = extras.getBoolean(isLanguageFromBundleKey);
            String newLanguageKey = extras.getString(newSelectedLanguageKeyBundleKey);
            String newLanguageTitle = extras.getString(newSelectedLanguageTitleBundleKey);
            if (isLanguageFromChanged) {
                if (toLanguageButton.getText().toString().equals(newLanguageTitle)) {
                    swapLanguages();
                } else {
                    presenter.fromLanguageChangedTo(newLanguageKey);
                    setFromLanguage(newLanguageTitle);
                }
            } else {
                if (fromLanguageButton.getText().toString().equals(newLanguageTitle)) {
                    swapLanguages();
                } else {
                    presenter.toLanguageChangedTo(newLanguageKey);
                    setToLanguage(newLanguageTitle);
                }
            }
        }
    }

    /**
     * Нажата кнопка исходного языка
     */
    public void onFromLanguageButtonClicked() {
        Intent intent = new Intent(getContext(), LanguageListActivity.class);
        intent.putExtra(isLanguageFromBundleKey, true);
        startActivityForResult(intent, newLanguageCheckResult);
    }

    /**
     * Нажата кнопка смены языков местами
     */
    public void onSwapLanguagesButtonClicked() {
        translatingTextEdit.setText(translationTextView.getText());
        swapLanguages();
    }

    /**
     * Поменять языки местами
     */
    private void swapLanguages() {
        presenter.languagesSwapped();
        String fromLanguageTitle = fromLanguageButton.getText().toString();
        setFromLanguage(toLanguageButton.getText().toString());
        setToLanguage(fromLanguageTitle);
    }

    /**
     * Нажата кнопка языка перевода
     */
    public void onToLanguageButtonClicked() {
        Intent intent = new Intent(getContext(), LanguageListActivity.class);
        intent.putExtra(isLanguageFromBundleKey, false);
        startActivityForResult(intent, newLanguageCheckResult);
    }

    /**
     * Установить состояние фрагмента
     * @param translation сохраненный перевод
     */
    public void setState(TranslationModel translation) {
        presenter.setState(translation);
    }

    @Override
    public void clicked(View v) {
        switch (v.getId()) {
            case R.id.from_language:
                onFromLanguageButtonClicked();
                break;
            case R.id.to_language:
                onToLanguageButtonClicked();
                break;
            case R.id.swap_languages:
                onSwapLanguagesButtonClicked();
                break;
        }
    }
}
