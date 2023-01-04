package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GenreCatalogueAdapter extends RecyclerView.Adapter<GenreCatalogueAdapter.ViewHolder> implements Filterable {
    private final Context context;
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
                    if (item.getName().toLowerCase().contains(filterPattern)) {
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
            genres.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    private List<Genre> genres;
    private List<Genre> genresFull;

    public GenreCatalogueAdapter(Context context, ClickCallback click) {
        this.context = context;
        this.click = click;
        this.genres = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_library_catalogue_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Genre genre = genres.get(position);

        holder.textGenre.setText(MusicUtil.getReadableString(genre.getName()));
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
        TextView textGenre;

        ViewHolder(View itemView) {
            super(itemView);

            textGenre = itemView.findViewById(R.id.genre_label);

            itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(Media.BY_GENRE, Media.BY_GENRE);
                bundle.putParcelable("genre_object", genres.get(getBindingAdapterPosition()));

                click.onGenreClick(bundle);
            });
        }
    }

    public void sort(String order) {
        switch (order) {
            case Genre.ORDER_BY_NAME:
                genres.sort(Comparator.comparing(Genre::getName));
                break;
            case Genre.ORDER_BY_RANDOM:
                Collections.shuffle(genres);
                break;
        }

        notifyDataSetChanged();
    }
}
