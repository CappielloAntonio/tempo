package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.util.Util;

import java.util.List;

/**
 * Adapter per i brani ritrovati nella ricerca
 */
public class SongResultSearchAdapter extends RecyclerView.Adapter<SongResultSearchAdapter.ViewHolder> {
    private static final String TAG = "SongResultSearchAdapter";
    private List<Song> songs;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener itemClickListener;

    public SongResultSearchAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.songs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_search_result_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.songTitle.setText(song.getTitle());
        holder.songArtist.setText(song.getArtistName());
        holder.songDuration.setText(Util.getReadableDurationString(song.getDuration()));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView songTitle;
        TextView songArtist;
        TextView songDuration;

        ViewHolder(View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.search_result_song_title_text_view);
            songArtist = itemView.findViewById(R.id.search_result_song_artist_text_view);
            songDuration = itemView.findViewById(R.id.search_result_song_duration_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setItems(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public Song getItem(int id) {
        return songs.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
