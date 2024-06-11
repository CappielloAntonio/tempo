package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.ItemLibraryMusicDirectoryBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UnstableApi
public class MusicDirectoryAdapter extends RecyclerView.Adapter<MusicDirectoryAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<Child> children;

    public MusicDirectoryAdapter(ClickCallback click) {
        this.click = click;
        this.children = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLibraryMusicDirectoryBinding view = ItemLibraryMusicDirectoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Child child = children.get(position);

        holder.item.musicDirectoryTitleTextView.setText(child.getTitle());

        CustomGlideRequest.ResourceType type = child.isDir()
                ? CustomGlideRequest.ResourceType.Directory
                : CustomGlideRequest.ResourceType.Song;

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), child.getCoverArtId(), type)
                .build(true)
                .into(holder.item.musicDirectoryCoverImageView);

        holder.item.musicDirectoryMoreButton.setVisibility(child.isDir() ? View.VISIBLE : View.INVISIBLE);
        holder.item.musicDirectoryPlayButton.setVisibility(child.isDir() ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public void setItems(List<Child> children) {
        this.children = children != null ? children : Collections.emptyList();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLibraryMusicDirectoryBinding item;

        ViewHolder(ItemLibraryMusicDirectoryBinding item) {
            super(item.getRoot());

            this.item = item;

            item.musicDirectoryTitleTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());

            item.musicDirectoryMoreButton.setOnClickListener(v -> onClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();

            if (children.get(getBindingAdapterPosition()).isDir()) {
                bundle.putString(Constants.MUSIC_DIRECTORY_ID, children.get(getBindingAdapterPosition()).getId());
                click.onMusicDirectoryClick(bundle);
            } else {
                bundle.putParcelableArrayList(Constants.TRACKS_OBJECT, new ArrayList<>(children));
                bundle.putInt(Constants.ITEM_POSITION, getBindingAdapterPosition());
                click.onMediaClick(bundle);
            }
        }

        private boolean onLongClick() {
            if (!children.get(getBindingAdapterPosition()).isDir()) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.TRACK_OBJECT, children.get(getBindingAdapterPosition()));

                click.onMediaLongClick(bundle);

                return true;
            } else {
                return false;
            }
        }
    }
}
