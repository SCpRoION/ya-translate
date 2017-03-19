package ru.ya.translate.translator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import ru.ya.translate.R;

public class TranslatorActivity extends AppCompatActivity implements TranslatorView {

    private TranslatorPresenter presenter;          /** Презентер */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);

        // Создаём презентер
        presenter = new TranslatorPresenter(this);
        presenter.onCreate();

        // Прослушиваем ввод текста
        EditText editText = (EditText) findViewById(R.id.text_to_translate);
        editText.addTextChangedListener(new TextWatcher() {
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
    public void showToast(int messageId) {
        Toast.makeText(this, getString(messageId), Toast.LENGTH_LONG).show();
    }
}
