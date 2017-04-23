package ru.ya.translate.history;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import ru.ya.translate.MainActivity;
import ru.ya.translate.R;
import ru.ya.translate.translation.TranslationModel;
import ru.ya.translate.translation.TranslationsStorage;

public abstract class BaseHistoryFragment extends Fragment {

    protected HistoryRecyclerViewAdapter adapter;
    private MainActivity.OnGoToTranslatorFragmentListener goToTranslatorFragmentListener;

    public BaseHistoryFragment() {
        setUserVisibleHint(false);
    }

    /**
     * Установить слушателя запроса на переход к фрагменту переводчика
     * @param goToTranslatorFragmentListener слушатель
     */
    public void setOnGoToTranslationFragmentListener(MainActivity.OnGoToTranslatorFragmentListener goToTranslatorFragmentListener) {
        this.goToTranslatorFragmentListener = goToTranslatorFragmentListener;
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
        List<TranslationModel> translations = getContent();
        Collections.reverse(translations);
        adapter.setData(translations);
        adapter.setOnFavoriteBtnClickedListener(new HistoryRecyclerViewAdapter.OnFavoriteBtnClickedListener() {
            @Override
            public void favoriteBtnClicked(TranslationModel translation, boolean setFavorite) {
                TranslationsStorage.getInstance().switchFavorite(translation);
            }
        });
        adapter.setOnItemClickedListener(translation -> goToTranslatorFragmentListener.goToTranslatorFragment(translation));
        recyclerView.setAdapter(adapter);

        registerForContextMenu(recyclerView);
        TranslationsStorage.getInstance().addOnTranslationRemovedListener(translation -> adapter.removeData(translation));

        return view;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            TranslationModel translation = adapter.getCurrentTranslation();
            TranslationsStorage.getInstance().remove(translation);
        }
        return super.onContextItemSelected(item);
    }

    protected abstract List<TranslationModel> getContent();
}
