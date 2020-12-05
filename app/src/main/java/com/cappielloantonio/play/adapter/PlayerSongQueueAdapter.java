package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter per i brani ritrovati nella ricerca
 */
public class PlayerSongQueueAdapter extends RecyclerView.Adapter<PlayerSongQueueAdapter.ViewHolder> {
    private static final String TAG = "SongResultSearchAdapter";

    private List<Song> songs;
    private LayoutInflater mInflater;
    private MainActivity mainActivity;
    private Context context;
    private FragmentManager fragmentManager;

    public PlayerSongQueueAdapter(MainActivity mainActivity, Context context, FragmentManager fragmentManager) {
        this.mainActivity = mainActivity;
        this.context = context;
        this.fragmentManager = fragmentManager;
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

        holder.songTitle.setText(song.getTitle());
        holder.songArtist.setText(song.getArtistName());

        CustomGlideRequest.Builder
                .from(context, song.getPrimary(), song.getBlurHash(), CustomGlideRequest.PRIMARY, CustomGlideRequest.TOP_QUALITY)
                .build()
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return songs.size();
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
        }

        @Override
        public void onClick(View view) {
            SongRepository songRepository = new SongRepository(App.getInstance());
            QueueRepository queueRepository = new QueueRepository(App.getInstance());

            songRepository.increasePlayCount(songs.get(getAdapterPosition()));
        }
    }

    public void setItems(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public Song getItem(int id) {
        return songs.get(id);
    }
}
