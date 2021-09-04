package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
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

    private final LayoutInflater mInflater;
    private final PlayerBottomSheetFragment playerBottomSheetFragment;
    private final Context context;

    private List<Song> songs;

    public PlayerSongQueueAdapter(Context context, PlayerBottomSheetFragment playerBottomSheetFragment) {
        this.context = context;
        this.playerBottomSheetFragment = playerBottomSheetFragment;
        this.mInflater = LayoutInflater.from(context);
        this.songs = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_player_queue_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.songTitle.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.songArtist.setText(MusicUtil.getReadableString(song.getArtistName()));

        CustomGlideRequest.Builder
                .from(context, song.getPrimary(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.cover);

        if (position < MusicPlayerRemote.getPosition()) {
            holder.songTitle.setTextColor(context.getResources().getColor(R.color.songToPlayTextColor, null));
            holder.songArtist.setTextColor(context.getResources().getColor(R.color.songToPlayTextColor, null));
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
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.queue_song_title_text_view);
            songArtist = itemView.findViewById(R.id.queue_song_artist_text_view);
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
