package ru.ya.translate.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.ya.translate.translation.TranslationModel;
import ru.ya.translate.translation.TranslationsStorage;

/**
 * Created by kamospertsyan on 23.04.17.
 */
public class HistoryFragment extends BaseHistoryFragment {
    @Override
    protected List<TranslationModel> getContent() {
        return TranslationsStorage.getInstance().getTranslations();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        TranslationsStorage.getInstance().addOnTranslationAddedListener(translation -> adapter.addData(translation));
        TranslationsStorage.getInstance().addOnTranslationFavoriteSwitchedListener(translation -> adapter.notifyDataSetChanged());

        return view;
    }
}
