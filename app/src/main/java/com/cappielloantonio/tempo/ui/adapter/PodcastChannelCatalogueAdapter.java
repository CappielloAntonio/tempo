package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.ItemHomeCataloguePodcastChannelBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.PodcastChannel;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PodcastChannelCatalogueAdapter extends RecyclerView.Adapter<PodcastChannelCatalogueAdapter.ViewHolder> implements Filterable {
    private final ClickCallback click;
    private final Filter filtering = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PodcastChannel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(podcastChannelsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PodcastChannel item : podcastChannelsFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            podcastChannels.clear();
            podcastChannels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    private List<PodcastChannel> podcastChannels;
    private List<PodcastChannel> podcastChannelsFull;

    public PodcastChannelCatalogueAdapter(ClickCallback click) {
        this.click = click;
        this.podcastChannels = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeCataloguePodcastChannelBinding view = ItemHomeCataloguePodcastChannelBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PodcastChannel podcastChannel = podcastChannels.get(position);

        holder.item.podcastChannelTitleLabel.setText(MusicUtil.getReadableString(podcastChannel.getTitle()));

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), podcastChannel.getCoverArtId())
                .build()
                .into(holder.item.podcastChannelCatalogueCoverImageView);
    }

    @Override
    public int getItemCount() {
        return podcastChannels.size();
    }

    public PodcastChannel getItem(int position) {
        return podcastChannels.get(position);
    }

    public void setItems(List<PodcastChannel> podcastChannels) {
        this.podcastChannels = podcastChannels;
        this.podcastChannelsFull = new ArrayList<>(podcastChannels);
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

    @Override
    public Filter getFilter() {
        return filtering;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHomeCataloguePodcastChannelBinding item;

        ViewHolder(ItemHomeCataloguePodcastChannelBinding item) {
            super(item.getRoot());

            this.item = item;

            item.podcastChannelTitleLabel.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());
        }

        private void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.PODCAST_CHANNEL_OBJECT, podcastChannels.get(getBindingAdapterPosition()));

            click.onAlbumClick(bundle);
        }

        private boolean onLongClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.PODCAST_CHANNEL_OBJECT, podcastChannels.get(getBindingAdapterPosition()));

            click.onAlbumLongClick(bundle);

            return true;
        }
    }
}
