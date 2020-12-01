package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter per i brani ritrovati nella ricerca
 */
public class SongResultSearchAdapter extends RecyclerView.Adapter<SongResultSearchAdapter.ViewHolder> {
    private static final String TAG = "SongResultSearchAdapter";

    private List<Song> songs;
    private LayoutInflater mInflater;
    private Context context;
    private FragmentManager fragmentManager;

    public SongResultSearchAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.mInflater = LayoutInflater.from(context);
        this.songs = new ArrayList<>();
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

        CustomGlideRequest.Builder
                .from(context, song.getPrimary(), song.getBlurHash(), CustomGlideRequest.PRIMARY, CustomGlideRequest.TOP_QUALITY)
                .build()
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView songTitle;
        TextView songArtist;
        TextView songDuration;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.search_result_song_title_text_view);
            songArtist = itemView.findViewById(R.id.search_result_song_artist_text_view);
            songDuration = itemView.findViewById(R.id.search_result_song_duration_text_view);
            cover = itemView.findViewById(R.id.song_cover_image_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            SongRepository songRepository = new SongRepository(App.getInstance());
            songRepository.increasePlayCount(songs.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song_object", songs.get(getAdapterPosition()));
            Navigation.findNavController(v).navigate(R.id.songBottomSheetDialog, bundle);
            return true;
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
