package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.model.Song;

import java.util.List;

public class DiscoverSongAdapter extends PagerAdapter {

    private List<Song> songs;
    private LayoutInflater layoutInflater;
    private Context context;

    public DiscoverSongAdapter(Context context, List<Song> models) {
        this.context = context;
        this.songs = models;
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
        View view = layoutInflater.inflate(R.layout.item_home_discover_song, container, false);

        TextView title;
        TextView desc;

        title = view.findViewById(R.id.title_discover_song_label);
        desc = view.findViewById(R.id.artist_discover_song_label);

        title.setText(songs.get(position).getTitle());
        desc.setText(songs.get(position).getAlbumName());

        view.setOnClickListener(v -> Toast.makeText(context, songs.get(position).getTitle(), Toast.LENGTH_SHORT).show());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}