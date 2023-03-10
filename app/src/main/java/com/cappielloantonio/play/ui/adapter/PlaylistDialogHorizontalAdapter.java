package com.cappielloantonio.play.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.ItemHorizontalPlaylistDialogBinding;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.subsonic.models.Playlist;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class PlaylistDialogHorizontalAdapter extends RecyclerView.Adapter<PlaylistDialogHorizontalAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<Playlist> playlists;

    public PlaylistDialogHorizontalAdapter(ClickCallback click) {
        this.click = click;
        this.playlists = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHorizontalPlaylistDialogBinding view = ItemHorizontalPlaylistDialogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);

        holder.item.playlistDialogTitleTextView.setText(MusicUtil.getReadableString(playlist.getName()));
        holder.item.playlistDialogCountTextView.setText(holder.itemView.getContext().getString(R.string.playlist_counted_tracks, playlist.getSongCount()));
        holder.item.playlistDialogDurationTextView.setText(MusicUtil.getReadableDurationString(playlist.getDuration(), false));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public void setItems(List<Playlist> playlists) {
        this.playlists = playlists;
        notifyDataSetChanged();
    }

    public Playlist getItem(int id) {
        return playlists.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHorizontalPlaylistDialogBinding item;

        ViewHolder(ItemHorizontalPlaylistDialogBinding item) {
            super(item.getRoot());

            this.item = item;

            item.playlistDialogTitleTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("playlist_object", playlists.get(getBindingAdapterPosition()));

            click.onPlaylistClick(bundle);
        }
    }
}
