package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UnstableApi
public class SongHorizontalAdapter extends RecyclerView.Adapter<SongHorizontalAdapter.ViewHolder> {
    private final Context context;
    private final ClickCallback click;
    private final boolean isCoverVisible;

    private List<Child> songs;

    public SongHorizontalAdapter(Context context, ClickCallback click, boolean isCoverVisible) {
        this.context = context;
        this.click = click;
        this.isCoverVisible = isCoverVisible;
        this.songs = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Child song = songs.get(position);

        holder.songTitle.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.songSubtitle.setText(context.getString(R.string.song_subtitle_formatter, MusicUtil.getReadableString(song.getArtist()), MusicUtil.getReadableDurationString(song.getDuration(), false)));
        holder.trackNumber.setText(String.valueOf(song.getTrack()));

        // TODO
        /* if (DownloadUtil.getDownloadTracker(context).isDownloaded(MappingUtil.mapMediaItem(context, song, false))) {
            holder.downloadIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.downloadIndicator.setVisibility(View.GONE);
        } */

        if (isCoverVisible) CustomGlideRequest.Builder
                .from(context, song.getCoverArtId(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.cover);

        if (isCoverVisible) holder.trackNumber.setVisibility(View.INVISIBLE);

        if (!isCoverVisible) holder.cover.setVisibility(View.INVISIBLE);

        if (!isCoverVisible && (position > 0 && songs.get(position - 1) != null && songs.get(position - 1).getDiscNumber() < songs.get(position).getDiscNumber())) {
            holder.differentDiscDivider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setItems(List<Child> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public Child getItem(int id) {
        return songs.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View differentDiscDivider;
        TextView songTitle;
        TextView songSubtitle;
        TextView trackNumber;
        View downloadIndicator;
        View coverSeparator;
        ImageView more;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            differentDiscDivider = itemView.findViewById(R.id.different_disk_divider);
            songTitle = itemView.findViewById(R.id.search_result_song_title_text_view);
            songSubtitle = itemView.findViewById(R.id.search_result_song_subtitle_text_view);
            trackNumber = itemView.findViewById(R.id.track_number_text_view);
            downloadIndicator = itemView.findViewById(R.id.search_result_dowanload_indicator_image_view);
            more = itemView.findViewById(R.id.search_result_song_more_button);
            cover = itemView.findViewById(R.id.song_cover_image_view);
            coverSeparator = itemView.findViewById(R.id.cover_image_separator);

            songTitle.setSelected(true);
            songSubtitle.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());

            more.setOnClickListener(v -> onLongClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("songs_object", new ArrayList<>(songs));
            bundle.putInt("position", getBindingAdapterPosition());

            click.onMediaClick(bundle);
        }

        private boolean onLongClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song_object", songs.get(getBindingAdapterPosition()));

            click.onMediaLongClick(bundle);

            return false;
        }
    }
}
