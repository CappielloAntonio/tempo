package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.ItemHorizontalAlbumBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlbumHorizontalAdapter extends RecyclerView.Adapter<AlbumHorizontalAdapter.ViewHolder> implements Filterable {
    private final ClickCallback click;
    private final boolean isOffline;

    private List<AlbumID3> albumsFull;
    private List<AlbumID3> albums;
    private String currentFilter;

    private final Filter filtering = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AlbumID3> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(albumsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                currentFilter = filterPattern;

                for (AlbumID3 item : albumsFull) {
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
            albums = (List<AlbumID3>) results.values;
            notifyDataSetChanged();
        }
    };

    public AlbumHorizontalAdapter(ClickCallback click, boolean isOffline) {
        this.click = click;
        this.isOffline = isOffline;
        this.albums = Collections.emptyList();
        this.albumsFull = Collections.emptyList();
        this.currentFilter = "";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHorizontalAlbumBinding view = ItemHorizontalAlbumBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlbumID3 album = albums.get(position);

        holder.item.albumTitleTextView.setText(album.getName());
        holder.item.albumArtistTextView.setText(album.getArtist());

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), album.getCoverArtId(), CustomGlideRequest.ResourceType.Album)
                .build()
                .into(holder.item.albumCoverImageView);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void setItems(List<AlbumID3> albums) {
        this.albumsFull = albums != null ? albums : Collections.emptyList();
        filtering.filter(currentFilter);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filtering;
    }

    public AlbumID3 getItem(int id) {
        return albums.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHorizontalAlbumBinding item;

        ViewHolder(ItemHorizontalAlbumBinding item) {
            super(item.getRoot());

            this.item = item;

            item.albumTitleTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());

            item.albumMoreButton.setOnClickListener(v -> onLongClick());
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

    public void sort(String order) {
        switch (order) {
            case Constants.ALBUM_ORDER_BY_NAME:
                albums.sort(Comparator.comparing(AlbumID3::getName));
                break;
            case Constants.ALBUM_ORDER_BY_MOST_RECENTLY_STARRED:
                albums.sort(Comparator.comparing(AlbumID3::getStarred, Comparator.nullsLast(Comparator.reverseOrder())));
                break;
            case Constants.ALBUM_ORDER_BY_LEAST_RECENTLY_STARRED:
                albums.sort(Comparator.comparing(AlbumID3::getStarred, Comparator.nullsLast(Comparator.naturalOrder())));

                break;
        }

        notifyDataSetChanged();
    }
}
