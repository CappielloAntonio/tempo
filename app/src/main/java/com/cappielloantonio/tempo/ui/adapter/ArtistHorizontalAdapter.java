package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.ItemHorizontalArtistBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.subsonic.models.ArtistID3;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ArtistHorizontalAdapter extends RecyclerView.Adapter<ArtistHorizontalAdapter.ViewHolder> implements Filterable {
    private final ClickCallback click;

    private List<ArtistID3> artistsFull;
    private List<ArtistID3> artists;
    private String currentFilter;

    private final Filter filtering = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ArtistID3> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(artistsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                currentFilter = filterPattern;

                for (ArtistID3 item : artistsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            artists = (List<ArtistID3>) results.values;
            notifyDataSetChanged();
        }
    };

    public ArtistHorizontalAdapter(ClickCallback click) {
        this.click = click;
        this.artists = Collections.emptyList();
        this.artistsFull = Collections.emptyList();
        this.currentFilter = "";
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

        holder.item.artistNameTextView.setText(artist.getName());

        if (artist.getAlbumCount() > 0) {
            holder.item.artistInfoTextView.setText("Album count: " + artist.getAlbumCount());
        } else {
            holder.item.artistInfoTextView.setVisibility(View.GONE);
        }

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), artist.getCoverArtId(), CustomGlideRequest.ResourceType.Artist)
                .build(true)
                .into(holder.item.artistCoverImageView);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setItems(List<ArtistID3> artists) {
        this.artistsFull = artists != null ? artists : Collections.emptyList();
        filtering.filter(currentFilter);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filtering;
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

            return true;
        }
    }

    public void sort(String order) {
        switch (order) {
            case Constants.ARTIST_ORDER_BY_NAME:
                artists.sort(Comparator.comparing(ArtistID3::getName));
                break;
            case Constants.ARTIST_ORDER_BY_MOST_RECENTLY_STARRED:
                artists.sort(Comparator.comparing(ArtistID3::getStarred, Comparator.nullsLast(Comparator.reverseOrder())));
                break;
            case Constants.ARTIST_ORDER_BY_LEAST_RECENTLY_STARRED:
                artists.sort(Comparator.comparing(ArtistID3::getStarred, Comparator.nullsLast(Comparator.naturalOrder())));

                break;
        }

        notifyDataSetChanged();
    }
}
