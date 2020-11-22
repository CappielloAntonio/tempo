package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.util.Util;

import java.util.List;

/**
 * Adapter per i brani recenti in home
 */
public class RecentMusicAdapter extends RecyclerView.Adapter<RecentMusicAdapter.ViewHolder> {
    private static final String TAG = "RecentMusicAdapter";
    private List<Song> songs;
    private LayoutInflater mInflater;
    private Context context;

    public RecentMusicAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.songs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home_recent_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.textTitle.setText(song.getTitle());
        holder.textArtist.setText(song.getAlbumName());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textTitle;
        TextView textArtist;

        ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.title_track_label);
            textArtist = itemView.findViewById(R.id.artist_track_label);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            SongRepository songRepository = new SongRepository(App.getInstance());
            songRepository.update(songs.get(getAdapterPosition()));
        }
    }

    public void setItems(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }
}
