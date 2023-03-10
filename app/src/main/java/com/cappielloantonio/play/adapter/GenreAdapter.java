package com.cappielloantonio.play.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.databinding.ItemLibraryGenreBinding;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.subsonic.models.Genre;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<Genre> genres;

    public GenreAdapter(ClickCallback click) {
        this.click = click;
        this.genres = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLibraryGenreBinding view = ItemLibraryGenreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Genre genre = genres.get(position);

        holder.item.genreLabel.setText(MusicUtil.getReadableString(genre.getGenre()));
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
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLibraryGenreBinding item;

        ViewHolder(ItemLibraryGenreBinding item) {
            super(item.getRoot());

            this.item = item;

            itemView.setOnClickListener(v -> onClick());
        }

        private void onClick() {
            Bundle bundle = new Bundle();
            bundle.putString(Media.BY_GENRE, Media.BY_GENRE);
            bundle.putParcelable("genre_object", genres.get(getBindingAdapterPosition()));

            click.onGenreClick(bundle);
        }
    }
}
