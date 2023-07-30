package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.ItemHomePodcastEpisodeBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.PodcastEpisode;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PodcastEpisodeAdapter extends RecyclerView.Adapter<PodcastEpisodeAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<PodcastEpisode> podcastEpisodes;
    private List<PodcastEpisode> podcastEpisodesFull;

    public PodcastEpisodeAdapter(ClickCallback click) {
        this.click = click;
        this.podcastEpisodes = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomePodcastEpisodeBinding view = ItemHomePodcastEpisodeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PodcastEpisode podcastEpisode = podcastEpisodes.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d");

        holder.item.podcastTitleLabel.setText(MusicUtil.getReadableString(podcastEpisode.getTitle()));
        holder.item.podcastSubtitleLabel.setText(MusicUtil.getReadableString(podcastEpisode.getArtist()));
        holder.item.podcastReleasesAndDurationLabel.setText(holder.itemView.getContext().getString(R.string.podcast_release_date_duration_formatter, simpleDateFormat.format(podcastEpisode.getPublishDate()), MusicUtil.getReadablePodcastDurationString(podcastEpisode.getDuration())));
        holder.item.podcastDescriptionText.setText(MusicUtil.getReadableString(podcastEpisode.getDescription()));

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), podcastEpisode.getCoverArtId())
                .build()
                .into(holder.item.podcastCoverImageView);

        holder.item.podcastPlayButton.setEnabled(podcastEpisode.getStatus().equals("completed"));
        holder.item.podcastMoreButton.setVisibility(podcastEpisode.getStatus().equals("completed") ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return podcastEpisodes.size();
    }

    public void setItems(List<PodcastEpisode> podcastEpisodes) {
        this.podcastEpisodesFull = podcastEpisodes;
        this.podcastEpisodes = podcastEpisodesFull.stream().filter(podcastEpisode -> Objects.equals(podcastEpisode.getStatus(), "completed")).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHomePodcastEpisodeBinding item;

        ViewHolder(ItemHomePodcastEpisodeBinding item) {
            super(item.getRoot());

            this.item = item;

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> openMore());

            item.podcastPlayButton.setOnClickListener(v -> onClick());
            item.podcastMoreButton.setOnClickListener(v -> openMore());
        }

        public void onClick() {
            PodcastEpisode podcastEpisode = podcastEpisodes.get(getBindingAdapterPosition());

            if (podcastEpisode.getStatus().equals("completed")) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.PODCAST_OBJECT, podcastEpisodes.get(getBindingAdapterPosition()));

                click.onPodcastEpisodeClick(bundle);
            }
        }

        private boolean openMore() {
            PodcastEpisode podcastEpisode = podcastEpisodes.get(getBindingAdapterPosition());

            if (podcastEpisode.getStatus().equals("completed")) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.PODCAST_OBJECT, podcastEpisodes.get(getBindingAdapterPosition()));

                click.onPodcastEpisodeLongClick(bundle);

                return true;
            }

            return false;
        }
    }

    public void sort(String order) {
        switch (order) {
            case Constants.PODCAST_FILTER_BY_DOWNLOAD:
                podcastEpisodes = podcastEpisodesFull.stream().filter(podcastEpisode -> Objects.equals(podcastEpisode.getStatus(), "completed")).collect(Collectors.toList());
                break;
            case Constants.PODCAST_FILTER_BY_ALL:
                podcastEpisodes = podcastEpisodesFull;
                break;
        }

        notifyDataSetChanged();
    }
}