package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class DiscoverSongAdapter extends RecyclerView.Adapter<DiscoverSongAdapter.ViewHolder> {
    private final Context context;
    private final ClickCallback click;

    private List<Child> songs;

    public DiscoverSongAdapter(Context context, ClickCallback click) {
        this.context = context;
        this.click = click;
        this.songs = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_discover_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Child song = songs.get(position);

        holder.textTitle.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.textAlbum.setText(MusicUtil.getReadableString(song.getAlbum()));

        CustomGlideRequest.Builder
                .from(context, song.getCoverArtId(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .into(holder.cover);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        startAnimation(holder);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setItems(List<Child> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textAlbum;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.title_discover_song_label);
            textAlbum = itemView.findViewById(R.id.album_discover_song_label);
            cover = itemView.findViewById(R.id.discover_song_cover_image_view);

            itemView.setOnClickListener(v -> onClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song_object", songs.get(getBindingAdapterPosition()));
            bundle.putBoolean("is_mix", true);

            click.onMediaClick(bundle);
        }
    }

    private void startAnimation(ViewHolder holder) {
        holder.cover.animate()
                .setDuration(20000)
                .setStartDelay(10)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(1.4f)
                .scaleY(1.4f)
                .start();
    }
}