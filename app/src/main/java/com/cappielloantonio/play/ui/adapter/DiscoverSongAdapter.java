package com.cappielloantonio.play.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.databinding.ItemHomeDiscoverSongBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class DiscoverSongAdapter extends RecyclerView.Adapter<DiscoverSongAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<Child> songs;

    public DiscoverSongAdapter(ClickCallback click) {
        this.click = click;
        this.songs = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeDiscoverSongBinding view = ItemHomeDiscoverSongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Child song = songs.get(position);

        holder.item.titleDiscoverSongLabel.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.item.albumDiscoverSongLabel.setText(MusicUtil.getReadableString(song.getAlbum()));

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), song.getCoverArtId(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .into(holder.item.discoverSongCoverImageView);
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
        ItemHomeDiscoverSongBinding item;

        ViewHolder(ItemHomeDiscoverSongBinding item) {
            super(item.getRoot());

            this.item = item;

            itemView.setOnClickListener(v -> onClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TRACK_OBJECT, songs.get(getBindingAdapterPosition()));
            bundle.putBoolean(Constants.MEDIA_MIX, true);

            click.onMediaClick(bundle);
        }
    }

    private void startAnimation(ViewHolder holder) {
        holder.item.discoverSongCoverImageView.animate()
                .setDuration(20000)
                .setStartDelay(10)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(1.4f)
                .scaleY(1.4f)
                .start();
    }
}