package com.cappielloantonio.play.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.databinding.ItemHomeGridTrackBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Chronology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridTrackAdapter extends RecyclerView.Adapter<GridTrackAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<Chronology> items;

    public GridTrackAdapter(ClickCallback click) {
        this.click = click;
        this.items = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeGridTrackBinding view = ItemHomeGridTrackBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chronology item = items.get(position);

        CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), item.getCoverArtId(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.item.trackCoverImageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Chronology getItem(int position) {
        return items.get(position);
    }

    public void setItems(List<Chronology> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHomeGridTrackBinding item;

        ViewHolder(ItemHomeGridTrackBinding item) {
            super(item.getRoot());

            this.item = item;

            itemView.setOnClickListener(v -> onClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("songs_object", new ArrayList<>(items));
            bundle.putBoolean("is_chronology", true);
            bundle.putInt("position", getBindingAdapterPosition());

            click.onMediaClick(bundle);
        }
    }
}
