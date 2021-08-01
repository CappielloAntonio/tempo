package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
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
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class SongHorizontalAdapter extends RecyclerView.Adapter<SongHorizontalAdapter.ViewHolder> {
    private static final String TAG = "SongHorizontalAdapter";

    private List<Song> songs;
    private LayoutInflater mInflater;
    private MainActivity mainActivity;
    private Context context;
    private FragmentManager fragmentManager;

    public SongHorizontalAdapter(MainActivity mainActivity, Context context, FragmentManager fragmentManager) {
        this.mainActivity = mainActivity;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.mInflater = LayoutInflater.from(context);
        this.songs = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_horizontal_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.songTitle.setText(MusicUtil.getReadableInfo(song.getTitle()));
        holder.songArtist.setText(MusicUtil.getReadableInfo(song.getArtistName()));
        holder.songDuration.setText(MusicUtil.getReadableDurationString(song.getDuration(), false));

        if (song.isOffline()) {
            holder.downloadIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.downloadIndicator.setVisibility(View.GONE);
        }

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

    public Song getItem(int id) {
        return songs.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView songTitle;
        TextView songArtist;
        TextView songDuration;
        View downloadIndicator;
        ImageView more;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.search_result_song_title_text_view);
            songArtist = itemView.findViewById(R.id.album_artist_text_view);
            songDuration = itemView.findViewById(R.id.search_result_song_duration_text_view);
            downloadIndicator = itemView.findViewById(R.id.search_result_dowanload_indicator_image_view);
            more = itemView.findViewById(R.id.search_result_song_more_button);
            cover = itemView.findViewById(R.id.song_cover_image_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            more.setOnClickListener(v -> {
                openMore(v);
            });

            songTitle.setSelected(true);
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
        public boolean onLongClick(View v) {
            openMore(v);
            return true;
        }

        private void openMore(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song_object", songs.get(getBindingAdapterPosition()));
            Navigation.findNavController(view).navigate(R.id.songBottomSheetDialog, bundle);
        }
    }
}
