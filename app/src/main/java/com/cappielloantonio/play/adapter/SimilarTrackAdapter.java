package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class SimilarTrackAdapter extends RecyclerView.Adapter<SimilarTrackAdapter.ViewHolder> {
    private static final String TAG = "SimilarTrackAdapter";

    private final MainActivity mainActivity;
    private final Context context;
    private final LayoutInflater mInflater;

    private List<Song> songs;

    public SimilarTrackAdapter(MainActivity mainActivity, Context context) {
        this.mainActivity = mainActivity;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.songs = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home_similar_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.textTitle.setText(MusicUtil.getReadableString(song.getTitle()));

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
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.title_track_label);
            cover = itemView.findViewById(R.id.track_cover_image_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            List<Song> opener = new ArrayList<>();
            opener.add(songs.get(getBindingAdapterPosition()));
            MusicPlayerRemote.openQueue(opener, 0, true);

            QueueRepository queueRepository = new QueueRepository(App.getInstance());
            queueRepository.insertAllAndStartNew(opener);

            mainActivity.isBottomSheetInPeek(true);
            mainActivity.setBottomSheetMusicInfo(songs.get(getBindingAdapterPosition()));

            SongRepository songRepository = new SongRepository(App.getInstance());
            songRepository.getInstantMix(songs.get(getBindingAdapterPosition()), 20, new MediaCallback() {
                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "onError: " + exception.getMessage());
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    MusicPlayerRemote.enqueue((List<Song>) media);
                }
            });
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
