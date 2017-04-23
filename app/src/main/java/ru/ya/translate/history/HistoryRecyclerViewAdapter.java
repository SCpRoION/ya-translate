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
    private OnFavoriteBtnClickedListener favoriteBtnClickedListener;
    private OnItemClickedListener itemClickedListener;

    public void setData(List<TranslationModel> translations) {
        this.translations = new ArrayList<>();
        this.translations.addAll(translations);

        notifyDataSetChanged();
    }

    public void addData(TranslationModel translation) {
        int i = 0;
        for (; i < translations.size(); ++i) {
            int trId = translations.get(i).getId();
            if (trId == translation.getId()) {
                return;
            }
            if (trId < translation.getId()) {
                break;
            }
        }
        translations.add(i, translation);

        notifyDataSetChanged();
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        itemClickedListener = listener;
    }

    public void setOnFavoriteBtnClickedListener(OnFavoriteBtnClickedListener listener) {
        favoriteBtnClickedListener = listener;
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

        holder.translationModel = item;
        holder.textFrom.setText(item.getTextFrom());
        holder.translation.setText(item.getTranslation());
        holder.languages.setText(item.getFromLanguageKey().toUpperCase() + '-' + item.getToLanguageKey().toUpperCase());
        holder.setFavoriteBtn.setVisibility(item.isFavorite() ? View.GONE : View.VISIBLE);
        holder.setNotFavoriteBtn.setVisibility(item.isFavorite() ? View.VISIBLE : View.GONE);

        if (itemClickedListener != null) {
            holder.rootContainer.setOnClickListener(v -> itemClickedListener.itemClicked(item));
        }
        if (favoriteBtnClickedListener != null) {
            holder.setOnFavoriteBtnClickedListener(favoriteBtnClickedListener);
        }
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout     rootContainer;
        TextView         textFrom;
        TextView         translation;
        ImageView        setFavoriteBtn;
        ImageView        setNotFavoriteBtn;
        TextView         languages;
        TranslationModel translationModel;

        public ViewHolder(View view) {
            super(view);

            rootContainer = (LinearLayout) view.findViewById(R.id.ll_root_container);
            textFrom = (TextView) view.findViewById(R.id.tv_text_from);
            translation = (TextView) view.findViewById(R.id.tv_translation);
            setFavoriteBtn = (ImageView) view.findViewById(R.id.btn_set_favorite);
            setNotFavoriteBtn = (ImageView) view.findViewById(R.id.btn_set_not_favorite);
            languages = (TextView) view.findViewById(R.id.tv_languages);
        }

        public void setOnFavoriteBtnClickedListener(OnFavoriteBtnClickedListener listener) {
            setFavoriteBtn.setOnClickListener(v -> {
                listener.favoriteBtnClicked(translationModel, true);
                setFavoriteBtn.setVisibility(View.GONE);
                setNotFavoriteBtn.setVisibility(View.VISIBLE);
            });
            setNotFavoriteBtn.setOnClickListener(v -> {
                listener.favoriteBtnClicked(translationModel, false);
                setFavoriteBtn.setVisibility(View.VISIBLE);
                setNotFavoriteBtn.setVisibility(View.GONE);
            });
        }
    }

    public interface OnFavoriteBtnClickedListener {
        /**
         * Нажата кнопка избранного
         * @param translation перевод, у которого она нажата
         * @param setFavorite флаг установки или удаления из избранного
         */
        void favoriteBtnClicked(TranslationModel translation, boolean setFavorite);
    }

    public interface OnItemClickedListener {
        /**
         * Выбран перевод из истории
         * @param translation выбранный перевод
         */
        void itemClicked(TranslationModel translation);
    }
}
