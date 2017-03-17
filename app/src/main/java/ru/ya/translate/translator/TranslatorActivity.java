package ru.ya.translate.translator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ru.ya.translate.R;

public class TranslatorActivity extends AppCompatActivity implements TranslatorView {

    private TranslatorPresenter presenter;          /** Презентер */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);

        presenter = new TranslatorPresenter(this);
        presenter.onCreate();
    }

    @Override
    public void showToast(int messageId) {
        Toast.makeText(this, getString(messageId), Toast.LENGTH_LONG).show();
    }
}
