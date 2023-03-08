package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.subsonic.models.SimilarArtistID3;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class ArtistSimilarAdapter extends RecyclerView.Adapter<ArtistSimilarAdapter.ViewHolder> {
    private final Context context;
    private final ClickCallback click;

    private List<SimilarArtistID3> artists;

    public ArtistSimilarAdapter(Context context, ClickCallback click) {
        this.context = context;
        this.click = click;
        this.artists = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_library_similar_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimilarArtistID3 artist = artists.get(position);

        holder.textArtistName.setText(MusicUtil.getReadableString(artist.getName()));

        setArtistCover(artist, holder.cover);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public SimilarArtistID3 getItem(int position) {
        return artists.get(position);
    }

    public void setItems(List<SimilarArtistID3> artists) {
        this.artists = artists;
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

    private void setArtistCover(SimilarArtistID3 artist, ImageView cover) {
        CustomGlideRequest.Builder
                .from(context, artist.getCoverArtId(), CustomGlideRequest.ARTIST_PIC, null)
                .build()
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(cover);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textArtistName;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textArtistName = itemView.findViewById(R.id.artist_name_label);
            cover = itemView.findViewById(R.id.similar_artist_cover_image_view);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());

            textArtistName.setSelected(true);
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artists.get(getBindingAdapterPosition()));

            click.onArtistClick(bundle);
        }

        public boolean onLongClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artists.get(getBindingAdapterPosition()));

            click.onArtistLongClick(bundle);

            return false;
        }
    }
}
