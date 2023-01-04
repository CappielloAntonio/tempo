package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.util.MusicUtil;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class PodcastEpisodeAdapter extends RecyclerView.Adapter<PodcastEpisodeAdapter.ViewHolder> {
    private final Context context;
    private final ClickCallback click;

    private List<Media> podcastEpisodes;

    public PodcastEpisodeAdapter(Context context, ClickCallback click) {
        this.context = context;
        this.click = click;
        this.podcastEpisodes = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_podcast_episode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Media podcastEpisode = podcastEpisodes.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d");

        holder.textTitle.setText(MusicUtil.getReadableString(podcastEpisode.getTitle()));
        holder.textSubtitle.setText(MusicUtil.getReadableString(podcastEpisode.getArtistName()));
        holder.textReleaseAndDuration.setText(context.getString(R.string.podcast_release_date_duration_formatter, simpleDateFormat.format(podcastEpisode.getPublishDate()), MusicUtil.getReadablePodcastDurationString(podcastEpisode.getDuration())));
        holder.textDescription.setText(MusicUtil.getReadableString(podcastEpisode.getDescription()));

        CustomGlideRequest.Builder
                .from(context, podcastEpisode.getCoverArtId(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return podcastEpisodes.size();
    }

    public void setItems(List<Media> podcastEpisodes) {
        this.podcastEpisodes = podcastEpisodes;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

            itemView.setOnClickListener(v -> onClick());

            moreButton.setOnLongClickListener(v -> openMore());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("podcast_object", podcastEpisodes.get(getBindingAdapterPosition()));

            click.onPodcastClick(bundle);
        }

        private boolean openMore() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("podcast_object", podcastEpisodes.get(getBindingAdapterPosition()));

            click.onPodcastLongClick(bundle);

            return false;
        }
    }
}