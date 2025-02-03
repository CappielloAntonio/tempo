package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.ItemLibraryAlbumBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<AlbumID3> albums;

    public AlbumAdapter(ClickCallback click) {
        this.click = click;
        this.albums = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLibraryAlbumBinding view = ItemLibraryAlbumBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlbumID3 album = albums.get(position);

        holder.item.albumNameLabel.setText(album.getName());
        holder.item.artistNameLabel.setText(album.getArtist());

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), album.getCoverArtId(), CustomGlideRequest.ResourceType.Album)
                .build(true)
                .into(holder.item.albumCoverImageView);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public AlbumID3 getItem(int position) {
        return albums.get(position);
    }

    public void setItems(List<AlbumID3> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLibraryAlbumBinding item;

        ViewHolder(ItemLibraryAlbumBinding item) {
            super(item.getRoot());

            this.item = item;

            item.albumNameLabel.setSelected(true);
            item.artistNameLabel.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());
        }

        private void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.ALBUM_OBJECT, albums.get(getBindingAdapterPosition()));

            click.onAlbumClick(bundle);
        }

        private boolean onLongClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.ALBUM_OBJECT, albums.get(getBindingAdapterPosition()));

            click.onAlbumLongClick(bundle);

            return true;
        }
    }
}
