package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private final Context context;
    private ClickCallback click;

    private List<Genre> genres;

    public GenreAdapter(Context context, ClickCallback click) {
        this.context = context;
        this.click = click;
        this.genres = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_library_genre, parent, false);
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
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textGenre;

        ViewHolder(View itemView) {
            super(itemView);

            textGenre = itemView.findViewById(R.id.genre_label);

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
