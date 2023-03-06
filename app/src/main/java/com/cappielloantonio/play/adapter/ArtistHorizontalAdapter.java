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
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class ArtistHorizontalAdapter extends RecyclerView.Adapter<ArtistHorizontalAdapter.ViewHolder> {
    private final Context context;
    private final ClickCallback click;

    private List<ArtistID3> artists;

    public ArtistHorizontalAdapter(Context context, ClickCallback click) {
        this.context = context;
        this.click = click;
        this.artists = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArtistID3 artist = artists.get(position);

        holder.artistName.setText(MusicUtil.getReadableString(artist.getName()));

        if (artist.getAlbumCount() > 0) {
            holder.artistInfo.setText("Album count: " + artist.getAlbumCount());
        } else {
            holder.artistInfo.setVisibility(View.GONE);
        }

        setArtistCover(artist, holder.cover);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setItems(List<ArtistID3> artists) {
        this.artists = artists;
        notifyDataSetChanged();
    }

    public ArtistID3 getItem(int id) {
        return artists.get(id);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void setArtistCover(ArtistID3 artist, ImageView cover) {
        CustomGlideRequest.Builder
                .from(context, artist.getCoverArtId(), CustomGlideRequest.ARTIST_PIC, null)
                .build()
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(cover);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        TextView artistInfo;

        ImageView more;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            artistName = itemView.findViewById(R.id.artist_name_text_view);
            artistInfo = itemView.findViewById(R.id.artist_info_text_view);
            more = itemView.findViewById(R.id.artist_more_button);
            cover = itemView.findViewById(R.id.artist_cover_image_view);

            artistName.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());

            more.setOnClickListener(v -> onLongClick());
        }

        private void onClick() {
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
