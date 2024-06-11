package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.ItemLibraryCatalogueAlbumBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlbumCatalogueAdapter extends RecyclerView.Adapter<AlbumCatalogueAdapter.ViewHolder> implements Filterable {
    private final ClickCallback click;
    private String currentFilter;
    private boolean showArtist;

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

    private List<AlbumID3> albums;
    private List<AlbumID3> albumsFull;

    public AlbumCatalogueAdapter(ClickCallback click, boolean showArtist) {
        this.click = click;
        this.albums = Collections.emptyList();
        this.albumsFull = Collections.emptyList();
        this.currentFilter = "";
        this.showArtist = showArtist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLibraryCatalogueAlbumBinding view = ItemLibraryCatalogueAlbumBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlbumID3 album = albums.get(position);

        holder.item.albumNameLabel.setText(album.getName());
        holder.item.artistNameLabel.setText(album.getArtist());
        holder.item.artistNameLabel.setVisibility(showArtist ? View.VISIBLE : View.GONE);

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), album.getCoverArtId(), CustomGlideRequest.ResourceType.Album)
                .build(true)
                .into(holder.item.albumCatalogueCoverImageView);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public AlbumID3 getItem(int position) {
        return albums.get(position);
    }

    public void setItems(List<AlbumID3> albums) {
        this.albumsFull = new ArrayList<>(albums);
        filtering.filter(currentFilter);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return filtering;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLibraryCatalogueAlbumBinding item;

        ViewHolder(ItemLibraryCatalogueAlbumBinding item) {
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

    public void sort(String order) {
        switch (order) {
            case Constants.ALBUM_ORDER_BY_NAME:
                albums.sort(Comparator.comparing(AlbumID3::getName));
                break;
            case Constants.ALBUM_ORDER_BY_ARTIST:
                albums.sort(Comparator.comparing(AlbumID3::getArtist));
                break;
            case Constants.ALBUM_ORDER_BY_YEAR:
                albums.sort(Comparator.comparing(AlbumID3::getYear));
                break;
            case Constants.ALBUM_ORDER_BY_RANDOM:
                Collections.shuffle(albums);
                break;
            case Constants.ALBUM_ORDER_BY_RECENTLY_ADDED:
                albums.sort(Comparator.comparing(AlbumID3::getCreated));
                Collections.reverse(albums);
                break;
        }

        notifyDataSetChanged();
    }
}