package ru.ya.translate.history;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import ru.ya.translate.R;
import ru.ya.translate.translation.TranslationModel;
import ru.ya.translate.translation.TranslationsStorage;

public class HistoryFragment extends Fragment implements TranslationsStorage.OnTranslationAddedListener {

    private HistoryRecyclerViewAdapter adapter;

    public HistoryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new HistoryRecyclerViewAdapter();
        List<TranslationModel> translations = TranslationsStorage.getInstance().getTranslations();
        TranslationsStorage.getInstance().addOnTranslationAddedListener(this);
        Collections.reverse(translations);
        adapter.setData(translations);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void translationAdded(TranslationModel translation) {
        adapter.addData(translation);
    }
}
