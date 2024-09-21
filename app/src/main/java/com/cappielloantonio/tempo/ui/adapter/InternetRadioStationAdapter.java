package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.ItemHomeInternetRadioStationBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.InternetRadioStation;
import com.cappielloantonio.tempo.util.Constants;

import java.util.Collections;
import java.util.List;

@UnstableApi
public class InternetRadioStationAdapter extends RecyclerView.Adapter<InternetRadioStationAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<InternetRadioStation> internetRadioStations;

    public InternetRadioStationAdapter(ClickCallback click) {
        this.click = click;
        this.internetRadioStations = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeInternetRadioStationBinding view = ItemHomeInternetRadioStationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InternetRadioStation internetRadioStation = internetRadioStations.get(position);

        holder.item.internetRadioStationTitleTextView.setText(internetRadioStation.getName());
        holder.item.internetRadioStationSubtitleTextView.setText(internetRadioStation.getStreamUrl());

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), internetRadioStation.getStreamUrl(), CustomGlideRequest.ResourceType.Radio)
                .build(true)
                .into(holder.item.internetRadioStationCoverImageView);
    }

    @Override
    public int getItemCount() {
        return internetRadioStations.size();
    }

    public void setItems(List<InternetRadioStation> internetRadioStations) {
        this.internetRadioStations = internetRadioStations;
        notifyDataSetChanged();
    }

    public InternetRadioStation getItem(int position) {
        return internetRadioStations.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHomeInternetRadioStationBinding item;

        ViewHolder(ItemHomeInternetRadioStationBinding item) {
            super(item.getRoot());

            this.item = item;

            item.internetRadioStationTitleTextView.setSelected(true);
            item.internetRadioStationSubtitleTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());

            item.internetRadioStationMoreButton.setOnClickListener(v -> onLongClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.INTERNET_RADIO_STATION_OBJECT, internetRadioStations.get(getBindingAdapterPosition()));

            click.onInternetRadioStationClick(bundle);
        }

        private boolean onLongClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.INTERNET_RADIO_STATION_OBJECT, internetRadioStations.get(getBindingAdapterPosition()));

            click.onInternetRadioStationLongClick(bundle);

            return true;
        }
    }
}
