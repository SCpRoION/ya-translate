package ru.ya.translate.translator;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.ya.translate.R;
import ru.ya.translate.languagelist.LanguageListActivity;

public class TranslatorFragment extends Fragment implements TranslatorView {

    /** Ключи для используемых полей контейнеров */
    public static String isLanguageFromBundleKey = "isLanguageFrom";
    public static String newSelectedLanguageKeyBundleKey = "newSelectedLanguageKey";
    public static String newSelectedLanguageTitleBundleKey = "newSelectedLanguageTitle";
    private final String translatingStringBundleKey = "translatingString";
    private final String translationStringBundleKey = "translationString";

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
        }

        // Обработать переданные аргументы
        Bundle extras = getArguments();
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

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(translatingStringBundleKey, translatingTextEdit.getText().toString());
        bundle.putString(translationStringBundleKey, translationTextView.getText().toString());
    }

    @Override
    public void showToast(int messageId) {
        Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_LONG).show();
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

    /**
     * Нажата кнопка исходного языка
     * @param view нажатая кнопка
     */
    public void onFromLanguageButtonClicked(View view) {
        Intent intent = new Intent(getContext(), LanguageListActivity.class);
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
        Intent intent = new Intent(getContext(), LanguageListActivity.class);
        intent.putExtra(isLanguageFromBundleKey, false);
        startActivity(intent);
    }
}
