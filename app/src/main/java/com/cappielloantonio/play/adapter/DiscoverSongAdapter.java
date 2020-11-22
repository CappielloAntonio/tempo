package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class DiscoverSongAdapter extends PagerAdapter {
    private static final String TAG = "DiscoverSongAdapter";

    private List<Song> songs;
    private LayoutInflater layoutInflater;
    private Context context;
    private View view;

    public DiscoverSongAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;

        Log.d(TAG, "DiscoverSongAdapter: " + songs.toString());
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_home_discover_song, container, false);

        TextView title = view.findViewById(R.id.title_discover_song_label);
        TextView desc = view.findViewById(R.id.artist_discover_song_label);
        title.setText(songs.get(position).getTitle());
        desc.setText(songs.get(position).getAlbumName());

        view.setOnClickListener(v -> {
            SongRepository songRepository = new SongRepository(App.getInstance());
            songRepository.update(songs.get(position));
        });

        container.addView(view, 0);
        return view;
    }

    public void setItems(List<Song> songs) {
        Log.d(TAG, "setItems: CHANGING");

        this.songs = songs;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}