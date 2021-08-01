package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.fragment.PlayerBottomSheetFragment;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter per i brani ritrovati nella ricerca
 */
public class PlayerSongQueueAdapter extends RecyclerView.Adapter<PlayerSongQueueAdapter.ViewHolder> {
    private static final String TAG = "SongResultSearchAdapter";

    private List<Song> songs;
    private LayoutInflater mInflater;
    private PlayerBottomSheetFragment playerBottomSheetFragment;
    private Context context;

    public PlayerSongQueueAdapter(Context context, PlayerBottomSheetFragment playerBottomSheetFragment) {
        this.context = context;
        this.playerBottomSheetFragment = playerBottomSheetFragment;
        this.mInflater = LayoutInflater.from(context);
        this.songs = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_player_queue_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.songTitle.setText(MusicUtil.getReadableInfo(song.getTitle()));
        holder.songArtist.setText(MusicUtil.getReadableInfo(song.getArtistName()));
        holder.songDuration.setText(MusicUtil.getReadableDurationString(song.getDuration(), false));

        CustomGlideRequest.Builder
                .from(context, song.getPrimary(), song.getBlurHash(), CustomGlideRequest.SONG_PIC)
                .build()
                .into(holder.cover);

        if (position < MusicPlayerRemote.getPosition()) {
            holder.songTitle.setTextColor(context.getResources().getColor(R.color.songToPlayTextColor, null));
            holder.songArtist.setTextColor(context.getResources().getColor(R.color.songToPlayTextColor, null));
            holder.songDuration.setTextColor(context.getResources().getColor(R.color.songToPlayTextColor, null));
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public List<Song> getItems() {
        return this.songs;
    }

    public void setItems(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public Song getItem(int id) {
        return songs.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView songTitle;
        TextView songArtist;
        TextView songDuration;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.queue_song_title_text_view);
            songArtist = itemView.findViewById(R.id.queue_song_artist_text_view);
            songDuration = itemView.findViewById(R.id.queue_song_duration_text_view);
            cover = itemView.findViewById(R.id.queue_song_cover_image_view);

            itemView.setOnClickListener(this);

            songTitle.setSelected(true);
        }

        @Override
        public void onClick(View view) {
            playerBottomSheetFragment.scrollPager(songs.get(getBindingAdapterPosition()), getBindingAdapterPosition(), false);
            MusicPlayerRemote.openQueue(songs, getBindingAdapterPosition(), true);
        }
    }
}
