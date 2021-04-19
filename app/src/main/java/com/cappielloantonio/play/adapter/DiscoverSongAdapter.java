package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.helper.MusicPlayerRemote;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.util.SyncUtil;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;

import java.util.ArrayList;
import java.util.List;

public class DiscoverSongAdapter extends RecyclerView.Adapter<DiscoverSongAdapter.ViewHolder> {
    private static final String TAG = "DiscoverSongAdapter";

    private List<Song> songs;
    private LayoutInflater inflater;
    private Context context;
    private MainActivity activity;

    public DiscoverSongAdapter(MainActivity activity, Context context, List<Song> songs) {
        this.activity = activity;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.songs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_home_discover_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.textTitle.setText(song.getTitle());
        holder.textAlbum.setText(song.getAlbumName());

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
        TextView textTitle;
        TextView textAlbum;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.title_discover_song_label);
            textAlbum = itemView.findViewById(R.id.album_discover_song_label);
            cover = itemView.findViewById(R.id.discover_song_cover_image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            SyncUtil.getInstantMix(context, new MediaCallback() {

                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "onError: " + exception.getMessage());
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    QueueRepository queueRepository = new QueueRepository(App.getInstance());
                    List<Song> mix = queueRepository.insertMix((ArrayList<Song>) media);

                    activity.isBottomSheetInPeek(true);
                    activity.setBottomSheetMusicInfo(mix.get(0));

                    PlayerBottomSheetViewModel playerBottomSheetViewModel = new ViewModelProvider(activity).get(PlayerBottomSheetViewModel.class);
                    playerBottomSheetViewModel.setNowPlayingSong(mix.get(0));

                    MusicPlayerRemote.openQueue(mix, 0, true);
                }
            }, SyncUtil.SONG, songs.get(getBindingAdapterPosition()).getId(), 50);
        }
    }

    public void setItems(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }
}