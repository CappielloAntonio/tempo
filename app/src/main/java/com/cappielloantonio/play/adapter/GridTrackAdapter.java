package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Chronology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridTrackAdapter extends RecyclerView.Adapter<GridTrackAdapter.ViewHolder> {
    private final Context context;
    private final ClickCallback click;

    private List<Chronology> items;

    public GridTrackAdapter(Context context, ClickCallback click) {
        this.context = context;
        this.click = click;
        this.items = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_grid_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chronology item = items.get(position);

        CustomGlideRequest.Builder
                .from(context, item.getCoverArtId(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.cover);
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
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.track_cover_image_view);

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
