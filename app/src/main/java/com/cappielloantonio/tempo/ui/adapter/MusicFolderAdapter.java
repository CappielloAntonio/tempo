package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.ItemLibraryMusicFolderBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.MusicFolder;
import com.cappielloantonio.tempo.util.Constants;

import java.util.Collections;
import java.util.List;

@UnstableApi
public class MusicFolderAdapter extends RecyclerView.Adapter<MusicFolderAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<MusicFolder> musicFolders;

    public MusicFolderAdapter(ClickCallback click) {
        this.click = click;
        this.musicFolders = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLibraryMusicFolderBinding view = ItemLibraryMusicFolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MusicFolder musicFolder = musicFolders.get(position);

        holder.item.musicFolderTitleTextView.setText(musicFolder.getName());

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), musicFolder.getName(), CustomGlideRequest.ResourceType.Directory)
                .build()
                .into(holder.item.musicFolderCoverImageView);
    }

    @Override
    public int getItemCount() {
        return musicFolders.size();
    }

    public void setItems(List<MusicFolder> musicFolders) {
        this.musicFolders = musicFolders;
        notifyDataSetChanged();
    }

    public MusicFolder getItem(int position) {
        return musicFolders.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLibraryMusicFolderBinding item;

        ViewHolder(ItemLibraryMusicFolderBinding item) {
            super(item.getRoot());

            this.item = item;

            item.musicFolderTitleTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());

            item.musicFolderMoreButton.setOnClickListener(v -> onClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.MUSIC_FOLDER_OBJECT, musicFolders.get(getBindingAdapterPosition()));
            click.onMusicFolderClick(bundle);
        }
    }
}
