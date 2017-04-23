package ru.ya.translate.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.ya.translate.R;
import ru.ya.translate.translation.TranslationModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private List<TranslationModel> translations;

    public void setData(List<TranslationModel> translations) {
        this.translations = new ArrayList<>();
        this.translations.addAll(translations);

        notifyDataSetChanged();
    }

    public void addData(TranslationModel translation) {
        translations.add(0, translation);

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TranslationModel item = translations.get(position);

        holder.textFrom.setText(item.getTextFrom());
        holder.translation.setText(item.getTranslation());
        holder.languages.setText(item.getFromLanguageKey().toUpperCase() + '-' + item.getToLanguageKey().toUpperCase());
        holder.setFavoriteBtn.setVisibility(item.isFavorite() ? View.GONE : View.VISIBLE);
        holder.setNotFavoriteBtn.setVisibility(item.isFavorite() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout rootContainer;
        TextView     textFrom;
        TextView     translation;
        ImageView    setFavoriteBtn;
        ImageView    setNotFavoriteBtn;
        TextView     languages;

        public ViewHolder(View view) {
            super(view);

            rootContainer = (LinearLayout) view.findViewById(R.id.ll_root_container);
            textFrom = (TextView) view.findViewById(R.id.tv_text_from);
            translation = (TextView) view.findViewById(R.id.tv_translation);
            setFavoriteBtn = (ImageView) view.findViewById(R.id.btn_set_favorite);
            setNotFavoriteBtn = (ImageView) view.findViewById(R.id.btn_set_not_favorite);
            languages = (TextView) view.findViewById(R.id.tv_languages);
        }
    }
}
