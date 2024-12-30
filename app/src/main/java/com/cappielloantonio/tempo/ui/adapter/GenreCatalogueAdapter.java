package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.ItemLibraryCatalogueGenreBinding;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.Genre;
import com.cappielloantonio.tempo.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GenreCatalogueAdapter extends RecyclerView.Adapter<GenreCatalogueAdapter.ViewHolder> implements Filterable {
    private final ClickCallback click;

    private final Filter filtering = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Genre> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(genresFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Genre item : genresFull) {
                    if (item.getGenre().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            genres.clear();
            if (results.count > 0) genres.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    private List<Genre> genres;
    private List<Genre> genresFull;

    public GenreCatalogueAdapter(ClickCallback click) {
        this.click = click;
        this.genres = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLibraryCatalogueGenreBinding view = ItemLibraryCatalogueGenreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Genre genre = genres.get(position);

        holder.item.genreLabel.setText(genre.getGenre());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public Genre getItem(int position) {
        return genres.get(position);
    }

    public void setItems(List<Genre> genres) {
        this.genres = genres;
        this.genresFull = new ArrayList<>(genres);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filtering;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLibraryCatalogueGenreBinding item;

        ViewHolder(ItemLibraryCatalogueGenreBinding item) {
            super(item.getRoot());

            this.item = item;

            itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.MEDIA_BY_GENRE, Constants.MEDIA_BY_GENRE);
                bundle.putParcelable(Constants.GENRE_OBJECT, genres.get(getBindingAdapterPosition()));

                click.onGenreClick(bundle);
            });
        }
    }

    public void sort(String order) {
        switch (order) {
            case Constants.GENRE_ORDER_BY_NAME:
                genres.sort(Comparator.comparing(Genre::getGenre));
                break;
            case Constants.GENRE_ORDER_BY_RANDOM:
                Collections.shuffle(genres);
                break;
        }

        notifyDataSetChanged();
    }
}
