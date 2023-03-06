package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class PlaylistDialogSongHorizontalAdapter extends RecyclerView.Adapter<PlaylistDialogSongHorizontalAdapter.ViewHolder> {
    private final Context context;

    private List<Child> songs;

    public PlaylistDialogSongHorizontalAdapter(Context context) {
        this.context = context;
        this.songs = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_playlist_dialog_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Child song = songs.get(position);

        holder.songTitle.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.songArtist.setText(MusicUtil.getReadableString(song.getArtist()));
        holder.songDuration.setText(MusicUtil.getReadableDurationString(song.getDuration(), false));

        CustomGlideRequest.Builder
                .from(context, song.getCoverArtId(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public List<Child> getItems() {
        return this.songs;
    }

    public void setItems(List<Child> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public Child getItem(int id) {
        return songs.get(id);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        TextView songArtist;
        TextView songDuration;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.playlist_dialog_song_title_text_view);
            songArtist = itemView.findViewById(R.id.playlist_dialog_album_artist_text_view);
            songDuration = itemView.findViewById(R.id.playlist_dialog_song_duration_text_view);
            cover = itemView.findViewById(R.id.playlist_dialog_song_cover_image_view);

            songTitle.setSelected(true);
        }
    }
}
