package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter per i brani recenti in home
 */
public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private static final String TAG = "RecentMusicAdapter";

    private final MainActivity mainActivity;
    private final Context context;
    private final LayoutInflater mInflater;

    private List<Song> songs;

    public TrackAdapter(MainActivity mainActivity, Context context) {
        this.mainActivity = mainActivity;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.songs = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.textTitle.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.textAlbum.setText(MusicUtil.getReadableString(song.getAlbumName()));

        CustomGlideRequest.Builder
                .from(context, song.getPrimary(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
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

            mainActivity.setBottomSheetInPeek(true);
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
