package com.cappielloantonio.play.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.media3.session.MediaBrowser;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.ItemPlayerQueueSongBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.util.MusicUtil;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerSongQueueAdapter extends RecyclerView.Adapter<PlayerSongQueueAdapter.ViewHolder> {
    private final ClickCallback click;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;
    private List<Child> songs;

    public PlayerSongQueueAdapter(ClickCallback click) {
        this.click = click;
        this.songs = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlayerQueueSongBinding view = ItemPlayerQueueSongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Child song = songs.get(position);

        holder.item.queueSongTitleTextView.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.item.queueSongSubtitleTextView.setText(holder.itemView.getContext().getString(R.string.song_subtitle_formatter, MusicUtil.getReadableString(song.getArtist()), MusicUtil.getReadableDurationString(song.getDuration(), false)));

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), song.getCoverArtId(), CustomGlideRequest.SONG_PIC)
                .build()
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.item.queueSongCoverImageView);

        MediaManager.getCurrentIndex(mediaBrowserListenableFuture, index -> {
            holder.item.queueSongPlayImageView.setVisibility(position == index ? View.VISIBLE : View.INVISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public List<Child> getItems() {
        return this.songs;
    }

    public void setItems(List<Child> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public void setMediaBrowserListenableFuture(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        this.mediaBrowserListenableFuture = mediaBrowserListenableFuture;
    }

    public Child getItem(int id) {
        return songs.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPlayerQueueSongBinding item;

        ViewHolder(ItemPlayerQueueSongBinding item) {
            super(item.getRoot());

            this.item = item;

            item.queueSongTitleTextView.setSelected(true);
            item.queueSongSubtitleTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.TRACKS_OBJECT, new ArrayList<>(songs));
            bundle.putInt(Constants.ITEM_POSITION, getBindingAdapterPosition());

            click.onMediaClick(bundle);
        }
    }
}
