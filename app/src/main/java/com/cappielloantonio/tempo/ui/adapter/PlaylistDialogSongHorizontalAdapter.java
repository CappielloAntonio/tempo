package com.cappielloantonio.tempo.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.ItemHorizontalPlaylistDialogTrackBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class PlaylistDialogSongHorizontalAdapter extends RecyclerView.Adapter<PlaylistDialogSongHorizontalAdapter.ViewHolder> {
    private List<Child> songs;

    public PlaylistDialogSongHorizontalAdapter() {
        this.songs = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHorizontalPlaylistDialogTrackBinding view = ItemHorizontalPlaylistDialogTrackBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Child song = songs.get(position);

        holder.item.playlistDialogSongTitleTextView.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.item.playlistDialogAlbumArtistTextView.setText(MusicUtil.getReadableString(song.getArtist()));
        holder.item.playlistDialogSongDurationTextView.setText(MusicUtil.getReadableDurationString(song.getDuration(), false));

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), song.getCoverArtId(), CustomGlideRequest.ResourceType.Song)
                .build()
                .into(holder.item.playlistDialogSongCoverImageView);
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

    public Child getItem(int id) {
        return songs.get(id);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemHorizontalPlaylistDialogTrackBinding item;

        ViewHolder(ItemHorizontalPlaylistDialogTrackBinding item) {
            super(item.getRoot());

            this.item = item;

            item.playlistDialogSongTitleTextView.setSelected(true);
        }
    }
}
