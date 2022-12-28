package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.media3.session.MediaBrowser;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Chronology;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MappingUtil;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

public class GridTrackAdapter extends RecyclerView.Adapter<GridTrackAdapter.ViewHolder> {
    private static final String TAG = "SimilarTrackAdapter";

    private final MainActivity activity;
    private final Context context;
    private final LayoutInflater mInflater;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;
    private List<Chronology> items;

    public GridTrackAdapter(MainActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home_grid_track, parent, false);
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

    public void setMediaBrowserListenableFuture(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        this.mediaBrowserListenableFuture = mediaBrowserListenableFuture;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.track_cover_image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            List<Media> media = MappingUtil.mapChronology(items);
            MediaManager.startQueue(mediaBrowserListenableFuture, context, media, getBindingAdapterPosition());
            activity.setBottomSheetInPeek(true);
        }
    }
}
