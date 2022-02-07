package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.session.MediaBrowser;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.util.MusicUtil;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

public class PlayerSongQueueAdapter extends RecyclerView.Adapter<PlayerSongQueueAdapter.ViewHolder> {
    private static final String TAG = "SongResultSearchAdapter";

    private final LayoutInflater mInflater;
    private final Context context;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;
    private List<Media> songs;

    public PlayerSongQueueAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.songs = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_player_queue_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Media song = songs.get(position);

        holder.songTitle.setText(MusicUtil.getReadableString(song.getTitle()));
        holder.songSubtitle.setText(context.getString(R.string.song_subtitle_formatter, MusicUtil.getReadableString(song.getArtistName()), MusicUtil.getReadableDurationString(song.getDuration(), false)));

        CustomGlideRequest.Builder
                .from(context, song.getPrimary(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.cover);

        MediaManager.getCurrentIndex(mediaBrowserListenableFuture, index -> {
            if (position < index) {
                holder.songTitle.setTextColor(context.getResources().getColor(R.color.songToPlayTextColor, null));
                holder.songSubtitle.setTextColor(context.getResources().getColor(R.color.songToPlayTextColor, null));
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public List<Media> getItems() {
        return this.songs;
    }

    public void setItems(List<Media> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public void setMediaBrowserListenableFuture(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        this.mediaBrowserListenableFuture = mediaBrowserListenableFuture;
    }

    public Media getItem(int id) {
        return songs.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView songTitle;
        TextView songSubtitle;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.queue_song_title_text_view);
            songSubtitle = itemView.findViewById(R.id.queue_song_subtitle_text_view);
            cover = itemView.findViewById(R.id.queue_song_cover_image_view);

            itemView.setOnClickListener(this);

            songTitle.setSelected(true);
            songSubtitle.setSelected(true);
        }

        @Override
        public void onClick(View view) {
            MediaManager.startQueue(mediaBrowserListenableFuture, context, songs, getBindingAdapterPosition());
        }
    }
}
