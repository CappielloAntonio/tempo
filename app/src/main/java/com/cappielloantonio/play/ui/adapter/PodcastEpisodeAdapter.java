package com.cappielloantonio.play.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.ItemHomePodcastEpisodeBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.subsonic.models.PodcastEpisode;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.util.MusicUtil;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class PodcastEpisodeAdapter extends RecyclerView.Adapter<PodcastEpisodeAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<PodcastEpisode> podcastEpisodes;

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
    }

    @Override
    public int getItemCount() {
        return podcastEpisodes.size();
    }

    public void setItems(List<PodcastEpisode> podcastEpisodes) {
        this.podcastEpisodes = podcastEpisodes;
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
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.PODCAST_OBJECT, podcastEpisodes.get(getBindingAdapterPosition()));

            click.onPodcastEpisodeClick(bundle);
        }

        private boolean openMore() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.PODCAST_OBJECT, podcastEpisodes.get(getBindingAdapterPosition()));

            click.onPodcastEpisodeLongClick(bundle);

            return true;
        }
    }
}