package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.model.Song;

import java.util.ArrayList;
import java.util.List;

public class PlayerNowPlayingSongAdapter extends RecyclerView.Adapter<PlayerNowPlayingSongAdapter.ViewHolder> {
    private static final String TAG = "DiscoverSongAdapter";

    private List<Song> songs;
    private LayoutInflater inflater;
    private Context context;

    public PlayerNowPlayingSongAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.songs = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_player_now_playing_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        CustomGlideRequest.Builder
                .from(context, song.getPrimary(), song.getPrimary(), CustomGlideRequest.PRIMARY, CustomGlideRequest.TOP_QUALITY, CustomGlideRequest.SONG_PIC)
                .build()
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.now_playing_song_cover_image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (MusicPlayerRemote.isPlaying()) {
                MusicPlayerRemote.pauseSong();
            } else {
                MusicPlayerRemote.resumePlaying();
            }
        }
    }

    public Song getItem(int position) {
        try {
            return songs.get(position);
        } catch ( IndexOutOfBoundsException e ) {
            return null;
        }
    }

    public void setItems(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }
}