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

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@UnstableApi
public class DownloadHorizontalAdapter extends RecyclerView.Adapter<DownloadHorizontalAdapter.ViewHolder> {
    private final Context context;
    private final ClickCallback click;

    private List<Child> songs;

    public DownloadHorizontalAdapter(Context context, ClickCallback click) {
        this.context = context;
        this.click = click;
        this.songs = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_download, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Child song = songs.get(position);

        holder.songTitle.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.songArtist.setText(context.getString(R.string.song_subtitle_formatter, MusicUtil.getReadableString(song.getArtist()), MusicUtil.getReadableDurationString(song.getDuration(), false)));
        holder.songAlbum.setText(MusicUtil.getReadableString(song.getAlbum()));

        if (position > 0 && songs.get(position - 1) != null && !Objects.equals(songs.get(position - 1).getAlbum(), songs.get(position).getAlbum())) {
            holder.divider.setPadding(0, 12, 0, 0);
        } else {
            if (position > 0) holder.divider.setVisibility(View.GONE);
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
        View divider;
        TextView songTitle;
        TextView songArtist;
        TextView songAlbum;
        ImageView more;

        ViewHolder(View itemView) {
            super(itemView);

            divider = itemView.findViewById(R.id.divider);
            songTitle = itemView.findViewById(R.id.downloaded_song_title_text_view);
            songArtist = itemView.findViewById(R.id.downloaded_song_artist_text_view);
            songAlbum = itemView.findViewById(R.id.downloaded_song_album_text_view);
            more = itemView.findViewById(R.id.downloaded_song_more_button);

            songTitle.setSelected(true);
            songArtist.setSelected(true);

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
