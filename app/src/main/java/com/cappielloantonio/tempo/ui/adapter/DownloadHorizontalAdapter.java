package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.ItemHorizontalDownloadBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UnstableApi
public class DownloadHorizontalAdapter extends RecyclerView.Adapter<DownloadHorizontalAdapter.ViewHolder> {
    private final ClickCallback click;

    private String view;
    private String filterKey;
    private String filterValue;

    private List<Child> songs;
    private List<Child> grouped;

    public DownloadHorizontalAdapter(ClickCallback click) {
        this.click = click;
        this.view = Constants.DOWNLOAD_TYPE_TRACK;
        this.songs = Collections.emptyList();
        this.grouped = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHorizontalDownloadBinding view = ItemHorizontalDownloadBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (view) {
            case Constants.DOWNLOAD_TYPE_TRACK:
                initTrackLayout(holder, position);
                break;
            case Constants.DOWNLOAD_TYPE_ALBUM:
                initAlbumLayout(holder, position);
                break;
            case Constants.DOWNLOAD_TYPE_ARTIST:
                initArtistLayout(holder, position);
                break;
            case Constants.DOWNLOAD_TYPE_GENRE:
                initGenreLayout(holder, position);
                break;
            case Constants.DOWNLOAD_TYPE_YEAR:
                initYearLayout(holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return grouped.size();
    }

    public void setItems(String view, String filterKey, String filterValue, List<Child> songs) {
        this.view = filterValue != null ? view : filterKey;
        this.filterKey = filterKey;
        this.filterValue = filterValue;

        this.songs = filterSong(filterKey, filterValue, songs);
        this.grouped = groupSong(songs);

        notifyDataSetChanged();
    }

    public Child getItem(int id) {
        return grouped.get(id);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private List<Child> groupSong(List<Child> songs) {
        switch (view) {
            case Constants.DOWNLOAD_TYPE_TRACK:
                return filterSong(filterKey, filterValue, songs.stream().filter(song -> Objects.nonNull(song.getId())).filter(Util.distinctByKey(Child::getId)).collect(Collectors.toList()));
            case Constants.DOWNLOAD_TYPE_ALBUM:
                return filterSong(filterKey, filterValue, songs.stream().filter(song -> Objects.nonNull(song.getAlbumId())).filter(Util.distinctByKey(Child::getAlbumId)).collect(Collectors.toList()));
            case Constants.DOWNLOAD_TYPE_ARTIST:
                return filterSong(filterKey, filterValue, songs.stream().filter(song -> Objects.nonNull(song.getArtistId())).filter(Util.distinctByKey(Child::getArtistId)).collect(Collectors.toList()));
            case Constants.DOWNLOAD_TYPE_GENRE:
                return filterSong(filterKey, filterValue, songs.stream().filter(song -> Objects.nonNull(song.getGenre())).filter(Util.distinctByKey(Child::getGenre)).collect(Collectors.toList()));
            case Constants.DOWNLOAD_TYPE_YEAR:
                return filterSong(filterKey, filterValue, songs.stream().filter(song -> Objects.nonNull(song.getYear())).filter(Util.distinctByKey(Child::getYear)).collect(Collectors.toList()));
        }

        return Collections.emptyList();
    }

    private List<Child> filterSong(String filterKey, String filterValue, List<Child> songs) {
        if (filterValue != null) {
            switch (filterKey) {
                case Constants.DOWNLOAD_TYPE_TRACK:
                    return songs.stream().filter(child -> child.getId().equals(filterValue)).collect(Collectors.toList());
                case Constants.DOWNLOAD_TYPE_ALBUM:
                    return songs.stream().filter(child -> Objects.equals(child.getAlbumId(), filterValue)).collect(Collectors.toList());
                case Constants.DOWNLOAD_TYPE_GENRE:
                    return songs.stream().filter(child -> Objects.equals(child.getGenre(), filterValue)).collect(Collectors.toList());
                case Constants.DOWNLOAD_TYPE_YEAR:
                    return songs.stream().filter(child -> Objects.equals(child.getYear(), Integer.valueOf(filterValue))).collect(Collectors.toList());
                case Constants.DOWNLOAD_TYPE_ARTIST:
                    return songs.stream().filter(child -> Objects.equals(child.getArtistId(), filterValue)).collect(Collectors.toList());
            }
        }

        return songs;
    }

    private String countSong(String filterKey, String filterValue, List<Child> songs) {
        if (filterValue != null) {
            switch (filterKey) {
                case Constants.DOWNLOAD_TYPE_TRACK:
                    return String.valueOf(songs.stream().filter(child -> child.getId().equals(filterValue)).count());
                case Constants.DOWNLOAD_TYPE_ALBUM:
                    return String.valueOf(songs.stream().filter(child -> Objects.equals(child.getAlbumId(), filterValue)).count());
                case Constants.DOWNLOAD_TYPE_GENRE:
                    return String.valueOf(songs.stream().filter(child -> Objects.equals(child.getGenre(), filterValue)).count());
                case Constants.DOWNLOAD_TYPE_YEAR:
                    return String.valueOf(songs.stream().filter(child -> Objects.equals(child.getYear(), Integer.valueOf(filterValue))).count());
                case Constants.DOWNLOAD_TYPE_ARTIST:
                    return String.valueOf(songs.stream().filter(child -> Objects.equals(child.getArtistId(), filterValue)).count());
            }
        }

        return "0";
    }

    private void initTrackLayout(ViewHolder holder, int position) {
        Child song = grouped.get(position);

        holder.item.downloadedItemTitleTextView.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.item.downloadedItemSubtitleTextView.setText(holder.itemView.getContext().getString(R.string.song_subtitle_formatter, MusicUtil.getReadableString(song.getArtist()), MusicUtil.getReadableDurationString(song.getDuration(), false)));
        holder.item.downloadedItemPreTextView.setText(MusicUtil.getReadableString(song.getAlbum()));

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), song.getCoverArtId())
                .build()
                .into(holder.item.itemCoverImageView);

        holder.item.itemCoverImageView.setVisibility(View.VISIBLE);
        holder.item.downloadedItemMoreButton.setVisibility(View.VISIBLE);
        holder.item.divider.setVisibility(View.VISIBLE);

        if (position > 0 && grouped.get(position - 1) != null && !Objects.equals(grouped.get(position - 1).getAlbum(), grouped.get(position).getAlbum())) {
            holder.item.divider.setPadding(0, (int) holder.itemView.getContext().getResources().getDimension(R.dimen.downloaded_item_padding), 0, 0);
        } else {
            if (position > 0) holder.item.divider.setVisibility(View.GONE);
        }
    }

    private void initAlbumLayout(ViewHolder holder, int position) {
        Child song = grouped.get(position);

        holder.item.downloadedItemTitleTextView.setText(MusicUtil.getReadableString(song.getAlbum()));
        holder.item.downloadedItemSubtitleTextView.setText(holder.itemView.getContext().getString(R.string.download_item_single_subtitle_formatter, countSong(Constants.DOWNLOAD_TYPE_ALBUM, song.getAlbumId(), songs)));
        holder.item.downloadedItemPreTextView.setText(MusicUtil.getReadableString(song.getArtist()));

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), song.getCoverArtId())
                .build()
                .into(holder.item.itemCoverImageView);

        holder.item.itemCoverImageView.setVisibility(View.VISIBLE);
        holder.item.downloadedItemMoreButton.setVisibility(View.GONE);
        holder.item.divider.setVisibility(View.VISIBLE);

        if (position > 0 && grouped.get(position - 1) != null && !Objects.equals(grouped.get(position - 1).getArtist(), grouped.get(position).getArtist())) {
            holder.item.divider.setPadding(0, (int) holder.itemView.getContext().getResources().getDimension(R.dimen.downloaded_item_padding), 0, 0);
        } else {
            if (position > 0) holder.item.divider.setVisibility(View.GONE);
        }
    }

    private void initArtistLayout(ViewHolder holder, int position) {
        Child song = grouped.get(position);

        holder.item.downloadedItemTitleTextView.setText(MusicUtil.getReadableString(song.getArtist()));
        holder.item.downloadedItemSubtitleTextView.setText(holder.itemView.getContext().getString(R.string.download_item_single_subtitle_formatter, countSong(Constants.DOWNLOAD_TYPE_ARTIST, song.getArtistId(), songs)));

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), song.getCoverArtId())
                .build()
                .into(holder.item.itemCoverImageView);

        holder.item.itemCoverImageView.setVisibility(View.VISIBLE);
        holder.item.downloadedItemMoreButton.setVisibility(View.GONE);
        holder.item.divider.setVisibility(View.GONE);
    }

    private void initGenreLayout(ViewHolder holder, int position) {
        Child song = grouped.get(position);

        holder.item.downloadedItemTitleTextView.setText(MusicUtil.getReadableString(song.getGenre()));
        holder.item.downloadedItemSubtitleTextView.setText(holder.itemView.getContext().getString(R.string.download_item_single_subtitle_formatter, countSong(Constants.DOWNLOAD_TYPE_GENRE, song.getGenre(), songs)));

        holder.item.itemCoverImageView.setVisibility(View.GONE);
        holder.item.downloadedItemMoreButton.setVisibility(View.GONE);
        holder.item.divider.setVisibility(View.GONE);
    }

    private void initYearLayout(ViewHolder holder, int position) {
        Child song = grouped.get(position);

        holder.item.downloadedItemTitleTextView.setText(String.valueOf(song.getYear()));
        holder.item.downloadedItemSubtitleTextView.setText(holder.itemView.getContext().getString(R.string.download_item_single_subtitle_formatter, countSong(Constants.DOWNLOAD_TYPE_YEAR, song.getYear().toString(), songs)));

        holder.item.itemCoverImageView.setVisibility(View.GONE);
        holder.item.downloadedItemMoreButton.setVisibility(View.GONE);
        holder.item.divider.setVisibility(View.GONE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHorizontalDownloadBinding item;

        ViewHolder(ItemHorizontalDownloadBinding item) {
            super(item.getRoot());

            this.item = item;

            item.downloadedItemTitleTextView.setSelected(true);
            item.downloadedItemSubtitleTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());

            item.downloadedItemMoreButton.setOnClickListener(v -> onLongClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();

            switch (view) {
                case Constants.DOWNLOAD_TYPE_TRACK:
                    bundle.putParcelableArrayList(Constants.TRACKS_OBJECT, new ArrayList<>(songs));
                    bundle.putInt(Constants.ITEM_POSITION, getBindingAdapterPosition());
                    click.onMediaClick(bundle);
                    break;
                case Constants.DOWNLOAD_TYPE_ALBUM:
                    bundle.putString(Constants.DOWNLOAD_TYPE_ALBUM, grouped.get(getBindingAdapterPosition()).getAlbumId());
                    click.onAlbumClick(bundle);
                    break;
                case Constants.DOWNLOAD_TYPE_ARTIST:
                    bundle.putString(Constants.DOWNLOAD_TYPE_ARTIST, grouped.get(getBindingAdapterPosition()).getArtistId());
                    click.onArtistClick(bundle);
                    break;
                case Constants.DOWNLOAD_TYPE_GENRE:
                    bundle.putString(Constants.DOWNLOAD_TYPE_GENRE, grouped.get(getBindingAdapterPosition()).getGenre());
                    click.onGenreClick(bundle);
                    break;
                case Constants.DOWNLOAD_TYPE_YEAR:
                    bundle.putString(Constants.DOWNLOAD_TYPE_YEAR, grouped.get(getBindingAdapterPosition()).getYear().toString());
                    click.onYearClick(bundle);
                    break;
            }
        }

        private boolean onLongClick() {
            Bundle bundle = new Bundle();

            switch (view) {
                case Constants.DOWNLOAD_TYPE_TRACK:
                    bundle.putParcelable(Constants.TRACK_OBJECT, grouped.get(getBindingAdapterPosition()));
                    click.onMediaLongClick(bundle);
                    return true;
                case Constants.DOWNLOAD_TYPE_ALBUM:
                    bundle.putString(Constants.DOWNLOAD_TYPE_ALBUM, grouped.get(getBindingAdapterPosition()).getAlbumId());
                    click.onAlbumLongClick(bundle);
                    return true;
                case Constants.DOWNLOAD_TYPE_ARTIST:
                    bundle.putString(Constants.DOWNLOAD_TYPE_ARTIST, grouped.get(getBindingAdapterPosition()).getArtistId());
                    click.onArtistLongClick(bundle);
                    return true;
            }

            return false;
        }
    }
}
