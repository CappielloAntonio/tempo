package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.session.MediaBrowser;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.PodcastEpisode;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PodcastEpisodeAdapter extends RecyclerView.Adapter<PodcastEpisodeAdapter.ViewHolder> {
    private static final String TAG = "DiscoverSongAdapter";

    private final LayoutInflater inflater;
    private final Context context;
    private final MainActivity activity;
    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    private List<PodcastEpisode> podcastEpisodes;

    public PodcastEpisodeAdapter(MainActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.podcastEpisodes = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_home_podcast_episode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PodcastEpisode podcastEpisode = podcastEpisodes.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d");

        holder.textTitle.setText(MusicUtil.getReadableString(podcastEpisode.getTitle()));
        holder.textSubtitle.setText(MusicUtil.getReadableString(podcastEpisode.getArtist()));
        holder.textReleaseAndDuration.setText(context.getString(R.string.podcast_release_date_duration_formatter, simpleDateFormat.format(podcastEpisode.getPublishDate()), MusicUtil.getReadablePodcastDurationString(podcastEpisode.getDuration())));
        holder.textDescription.setText(MusicUtil.getReadableString(podcastEpisode.getDescription()));

        CustomGlideRequest.Builder
                .from(context, podcastEpisode.getCoverArtId(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return podcastEpisodes.size();
    }

    public void setItems(List<PodcastEpisode> podcastEpisodes) {
        this.podcastEpisodes = podcastEpisodes;
        notifyDataSetChanged();
    }

    public void setMediaBrowserListenableFuture(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        this.mediaBrowserListenableFuture = mediaBrowserListenableFuture;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textTitle;
        TextView textSubtitle;
        TextView textReleaseAndDuration;
        TextView textDescription;
        ImageView cover;
        Button playButton;
        Button moreButton;

        ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.podcast_title_label);
            textSubtitle = itemView.findViewById(R.id.podcast_subtitle_label);
            textReleaseAndDuration = itemView.findViewById(R.id.podcast_releases_and_duration_label);
            textDescription = itemView.findViewById(R.id.podcast_description_label);
            cover = itemView.findViewById(R.id.podcast_cover_image_view);
            playButton = itemView.findViewById(R.id.podcast_play_button);
            moreButton = itemView.findViewById(R.id.podcast_more_button);

            playButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // MediaManager.startQueue(mediaBrowserListenableFuture, context, podcastEpisodes.get(getBindingAdapterPosition()));
            // activity.setBottomSheetInPeek(true);
        }
    }
}