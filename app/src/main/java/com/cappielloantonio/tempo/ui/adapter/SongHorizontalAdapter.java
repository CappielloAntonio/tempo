package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.ItemHorizontalTrackBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.subsonic.models.DiscTitle;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.util.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@UnstableApi
public class SongHorizontalAdapter extends RecyclerView.Adapter<SongHorizontalAdapter.ViewHolder> {
    private final ClickCallback click;
    private final boolean showCoverArt;
    private final boolean showAlbum;
    private final AlbumID3 album;

    private List<Child> songs;

    public SongHorizontalAdapter(ClickCallback click, boolean showCoverArt, boolean showAlbum, AlbumID3 album) {
        this.click = click;
        this.showCoverArt = showCoverArt;
        this.showAlbum = showAlbum;
        this.songs = Collections.emptyList();
        this.album = album;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHorizontalTrackBinding view = ItemHorizontalTrackBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Child song = songs.get(position);

        holder.item.searchResultSongTitleTextView.setText(song.getTitle());

        holder.item.searchResultSongSubtitleTextView.setText(
                holder.itemView.getContext().getString(
                        R.string.song_subtitle_formatter,
                        this.showAlbum ?
                                song.getAlbum() :
                                song.getArtist(),
                        MusicUtil.getReadableDurationString(song.getDuration(), false),
                        MusicUtil.getReadableAudioQualityString(song)
                )
        );

        holder.item.trackNumberTextView.setText(MusicUtil.getReadableTrackNumber(holder.itemView.getContext(), song.getTrack()));

        if (DownloadUtil.getDownloadTracker(holder.itemView.getContext()).isDownloaded(song.getId())) {
            holder.item.searchResultDownloadIndicatorImageView.setVisibility(View.VISIBLE);
        } else {
            holder.item.searchResultDownloadIndicatorImageView.setVisibility(View.GONE);
        }

        if (showCoverArt) CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), song.getCoverArtId(), CustomGlideRequest.ResourceType.Song)
                .build()
                .into(holder.item.songCoverImageView);

        holder.item.trackNumberTextView.setVisibility(showCoverArt ? View.INVISIBLE : View.VISIBLE);
        holder.item.songCoverImageView.setVisibility(showCoverArt ? View.VISIBLE : View.INVISIBLE);

        if (!showCoverArt &&
                ((position == 0 && songs.get(position).getDiscNumber() != null) ||
                        (position > 0 && songs.get(position - 1) != null &&
                                songs.get(position - 1).getDiscNumber() != null &&
                                songs.get(position).getDiscNumber() != null &&
                                songs.get(position - 1).getDiscNumber() < songs.get(position).getDiscNumber()
                        )
                )
        ) {
            holder.item.differentDiskDividerSector.setVisibility(View.VISIBLE);
            holder.item.discTitleTextView.setText(holder.itemView.getContext().getString(R.string.disc_titleless, songs.get(position).getDiscNumber().toString()));

            if (album.getDiscTitles() != null) {
                Optional<DiscTitle> discTitle = album.getDiscTitles().stream().filter(title -> Objects.equals(title.getDisc(), songs.get(position).getDiscNumber())).findFirst();

                if (discTitle.isPresent() && discTitle.get().getDisc() != null && discTitle.get().getTitle() != null && !discTitle.get().getTitle().isEmpty()) {
                    holder.item.discTitleTextView.setText(holder.itemView.getContext().getString(R.string.disc_titlefull, discTitle.get().getDisc().toString() , discTitle.get().getTitle()));
                }
            }
        }

        if (Preferences.showItemRating()) {
            if (song.getStarred() == null && song.getUserRating() == null) {
                holder.item.ratingIndicatorImageView.setVisibility(View.GONE);
            }

            holder.item.preferredIcon.setVisibility(song.getStarred() != null ? View.VISIBLE : View.GONE);
            holder.item.ratingBarLayout.setVisibility(song.getUserRating() != null ? View.VISIBLE : View.GONE);

            if (song.getUserRating() != null) {
                holder.item.oneStarIcon.setImageDrawable(AppCompatResources.getDrawable(holder.itemView.getContext(), song.getUserRating() >= 1 ? R.drawable.ic_star : R.drawable.ic_star_outlined));
                holder.item.twoStarIcon.setImageDrawable(AppCompatResources.getDrawable(holder.itemView.getContext(), song.getUserRating() >= 2 ? R.drawable.ic_star : R.drawable.ic_star_outlined));
                holder.item.threeStarIcon.setImageDrawable(AppCompatResources.getDrawable(holder.itemView.getContext(), song.getUserRating() >= 3 ? R.drawable.ic_star : R.drawable.ic_star_outlined));
                holder.item.fourStarIcon.setImageDrawable(AppCompatResources.getDrawable(holder.itemView.getContext(), song.getUserRating() >= 4 ? R.drawable.ic_star : R.drawable.ic_star_outlined));
                holder.item.fiveStarIcon.setImageDrawable(AppCompatResources.getDrawable(holder.itemView.getContext(), song.getUserRating() >= 5 ? R.drawable.ic_star : R.drawable.ic_star_outlined));
            }
        } else {
            holder.item.ratingIndicatorImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setItems(List<Child> songs) {
        this.songs = songs != null ? songs : Collections.emptyList();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Child getItem(int id) {
        return songs.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHorizontalTrackBinding item;

        ViewHolder(ItemHorizontalTrackBinding item) {
            super(item.getRoot());

            this.item = item;

            item.searchResultSongTitleTextView.setSelected(true);
            item.searchResultSongSubtitleTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());

            item.searchResultSongMoreButton.setOnClickListener(v -> onLongClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.TRACKS_OBJECT, new ArrayList<>(MusicUtil.limitPlayableMedia(songs, getBindingAdapterPosition())));
            bundle.putInt(Constants.ITEM_POSITION, MusicUtil.getPlayableMediaPosition(songs, getBindingAdapterPosition()));

            click.onMediaClick(bundle);
        }

        private boolean onLongClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TRACK_OBJECT, songs.get(getBindingAdapterPosition()));

            click.onMediaLongClick(bundle);

            return true;
        }
    }
}
