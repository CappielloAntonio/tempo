package com.cappielloantonio.play.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cappielloantonio.play.databinding.ItemHorizontalArtistBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class ArtistHorizontalAdapter extends RecyclerView.Adapter<ArtistHorizontalAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<ArtistID3> artists;

    public ArtistHorizontalAdapter(ClickCallback click) {
        this.click = click;
        this.artists = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHorizontalArtistBinding view = ItemHorizontalArtistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArtistID3 artist = artists.get(position);

        holder.item.artistNameTextView.setText(MusicUtil.getReadableString(artist.getName()));

        if (artist.getAlbumCount() > 0) {
            holder.item.artistInfoTextView.setText("Album count: " + artist.getAlbumCount());
        } else {
            holder.item.artistInfoTextView.setVisibility(View.GONE);
        }

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), artist.getCoverArtId(), CustomGlideRequest.ARTIST_PIC)
                .build()
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.item.artistCoverImageView);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setItems(List<ArtistID3> artists) {
        this.artists = artists;
        notifyDataSetChanged();
    }

    public ArtistID3 getItem(int id) {
        return artists.get(id);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHorizontalArtistBinding item;

        ViewHolder(ItemHorizontalArtistBinding item) {
            super(item.getRoot());

            this.item = item;

            item.artistNameTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());

            item.artistMoreButton.setOnClickListener(v -> onLongClick());
        }

        private void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.ARTIST_OBJECT, artists.get(getBindingAdapterPosition()));

            click.onArtistClick(bundle);
        }

        public boolean onLongClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.ARTIST_OBJECT, artists.get(getBindingAdapterPosition()));

            click.onArtistLongClick(bundle);

            return false;
        }
    }
}
