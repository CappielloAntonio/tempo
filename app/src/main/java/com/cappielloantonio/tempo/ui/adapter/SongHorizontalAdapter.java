package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.ItemHorizontalTrackBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.cappielloantonio.tempo.util.MusicUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UnstableApi
public class SongHorizontalAdapter extends RecyclerView.Adapter<SongHorizontalAdapter.ViewHolder> {
    private final ClickCallback click;
    private final boolean isCoverVisible;
    private final boolean showAlbum;

    private List<Child> songs;

    public SongHorizontalAdapter(ClickCallback click, boolean isCoverVisible, boolean showAlbum) {
        this.click = click;
        this.isCoverVisible = isCoverVisible;
        this.songs = Collections.emptyList();
        this.showAlbum = showAlbum;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHorizontalTrackBinding view = ItemHorizontalTrackBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Child song = songs.get(position);

        holder.item.searchResultSongTitleTextView.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.item.searchResultSongSubtitleTextView.setText(holder.itemView.getContext().getString(R.string.song_subtitle_formatter, MusicUtil.getReadableString(this.showAlbum ? song.getAlbum() : song.getArtist()), MusicUtil.getReadableDurationString(song.getDuration() != null ? song.getDuration() : 0, false)));
        holder.item.trackNumberTextView.setText(MusicUtil.getReadableTrackNumber(holder.itemView.getContext(), song.getTrack()));

        if (DownloadUtil.getDownloadTracker(holder.itemView.getContext()).isDownloaded(song.getId())) {
            holder.item.searchResultDowanloadIndicatorImageView.setVisibility(View.VISIBLE);
        } else {
            holder.item.searchResultDowanloadIndicatorImageView.setVisibility(View.GONE);
        }

        if (isCoverVisible) CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), song.getCoverArtId())
                .build()
                .into(holder.item.songCoverImageView);

        if (isCoverVisible) holder.item.trackNumberTextView.setVisibility(View.INVISIBLE);

        if (!isCoverVisible) holder.item.songCoverImageView.setVisibility(View.INVISIBLE);

        if (!isCoverVisible && (position > 0 && songs.get(position - 1) != null && songs.get(position - 1).getDiscNumber() != null && songs.get(position).getDiscNumber() != null && songs.get(position - 1).getDiscNumber() < songs.get(position).getDiscNumber())) {
            holder.item.differentDiskDivider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setItems(List<Child> songs) {
        this.songs = songs != null ? songs : Collections.emptyList();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Child getItem(int id) {
        return songs.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHorizontalTrackBinding item;

        ViewHolder(ItemHorizontalTrackBinding item) {
            super(item.getRoot());

            this.item = item;

            item.searchResultSongTitleTextView.setSelected(true);
            item.searchResultSongSubtitleTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());

            item.searchResultSongMoreButton.setOnClickListener(v -> onLongClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.TRACKS_OBJECT, new ArrayList<>(MusicUtil.limitPlayableMedia(songs, getBindingAdapterPosition())));
            bundle.putInt(Constants.ITEM_POSITION, MusicUtil.getPlayableMediaPosition(songs, getBindingAdapterPosition()));

            click.onMediaClick(bundle);
        }

        private boolean onLongClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TRACK_OBJECT, songs.get(getBindingAdapterPosition()));

            click.onMediaLongClick(bundle);

            return true;
        }
    }
}
