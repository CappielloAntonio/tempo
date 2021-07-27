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
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter per i brani recenti in home
 */
public class RecentMusicAdapter extends RecyclerView.Adapter<RecentMusicAdapter.ViewHolder> {
    private static final String TAG = "RecentMusicAdapter";

    private List<Song> songs;
    private LayoutInflater mInflater;
    private MainActivity mainActivity;
    private Context context;
    private FragmentManager fragmentManager;

    public RecentMusicAdapter(MainActivity mainActivity, Context context, FragmentManager fragmentManager) {
        this.mainActivity = mainActivity;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.mInflater = LayoutInflater.from(context);
        this.songs = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.textTitle.setText(song.getTitle());
        holder.textAlbum.setText(song.getAlbumName());

        CustomGlideRequest.Builder
                .from(context, song.getPrimary(), song.getBlurHash(), CustomGlideRequest.SONG_PIC)
                .build()
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setItems(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textTitle;
        TextView textAlbum;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.title_track_label);
            textAlbum = itemView.findViewById(R.id.album_track_label);
            cover = itemView.findViewById(R.id.track_cover_image_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            QueueRepository queueRepository = new QueueRepository(App.getInstance());
            queueRepository.insertAllAndStartNew(songs);

            mainActivity.isBottomSheetInPeek(true);
            mainActivity.setBottomSheetMusicInfo(songs.get(getBindingAdapterPosition()));

            MusicPlayerRemote.openQueue(songs, getBindingAdapterPosition(), true);

        }

        @Override
        public boolean onLongClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song_object", songs.get(getBindingAdapterPosition()));
            Navigation.findNavController(view).navigate(R.id.songBottomSheetDialog, bundle);
            return true;
        }
    }
}
