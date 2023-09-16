package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.ItemLibraryMusicIndexBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.helper.recyclerview.FastScrollbar;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.Artist;
import com.cappielloantonio.tempo.util.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@UnstableApi
public class MusicIndexAdapter extends RecyclerView.Adapter<MusicIndexAdapter.ViewHolder> implements FastScrollbar.BubbleTextGetter {
    private final ClickCallback click;

    private List<Artist> artists;

    public MusicIndexAdapter(ClickCallback click) {
        this.click = click;
        this.artists = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLibraryMusicIndexBinding view = ItemLibraryMusicIndexBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artist artist = artists.get(position);

        holder.item.musicIndexTitleTextView.setText(artist.getName());

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), artist.getName(), CustomGlideRequest.ResourceType.Directory)
                .build()
                .into(holder.item.musicIndexCoverImageView);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setItems(List<Artist> artists) {
        this.artists = artists;
        notifyDataSetChanged();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return artists != null && !artists.isEmpty() ? Character.toString(Objects.requireNonNull(artists.get(pos).getName().toUpperCase()).charAt(0)) : null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLibraryMusicIndexBinding item;

        ViewHolder(ItemLibraryMusicIndexBinding item) {
            super(item.getRoot());

            this.item = item;

            item.musicIndexTitleTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            item.musicIndexMoreButton.setOnClickListener(v -> onClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.MUSIC_DIRECTORY_ID, artists.get(getBindingAdapterPosition()).getId());
            click.onMusicIndexClick(bundle);
        }
    }
}
