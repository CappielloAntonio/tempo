package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArtistHorizontalAdapter extends RecyclerView.Adapter<ArtistHorizontalAdapter.ViewHolder> {
    private static final String TAG = "ArtistHorizontalAdapter";

    private List<Artist> artists;
    private final LayoutInflater mInflater;
    private final Context context;
    private final boolean isDownloaded;

    public ArtistHorizontalAdapter(Context context, boolean isDownloaded) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.artists = new ArrayList<>();
        this.isDownloaded = isDownloaded;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_horizontal_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artist artist = artists.get(position);

        holder.artistName.setText(MusicUtil.getReadableString(artist.getName()));

        if (artist.getAlbumCount() > 0) {
            holder.artistInfo.setText("Album count: " + String.valueOf(artist.getAlbumCount()));
        } else {
            holder.artistInfo.setVisibility(View.GONE);
        }

        CustomGlideRequest.Builder
                .from(context, artist.getId(), CustomGlideRequest.ARTIST_PIC, null)
                .build()
                .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setItems(List<Artist> artists) {
        this.artists = artists;
        notifyDataSetChanged();
    }

    public Artist getItem(int id) {
        return artists.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            more.setOnClickListener(v -> {
                openMore(v);
            });

            artistName.setSelected(true);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artists.get(getBindingAdapterPosition()));

            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.homeFragment) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_artistPageFragment, bundle);
            } else if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.artistListPageFragment) {
                if (!isDownloaded) Navigation.findNavController(view).navigate(R.id.action_artistListPageFragment_to_artistPageFragment, bundle);
                else Navigation.findNavController(view).navigate(R.id.action_artistListPageFragment_to_albumListPageFragment, bundle);
            } else if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.downloadFragment) {
                Navigation.findNavController(view).navigate(R.id.action_downloadFragment_to_albumListPageFragment, bundle);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            openMore(v);
            return true;
        }

        private void openMore(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artists.get(getBindingAdapterPosition()));
            Navigation.findNavController(view).navigate(R.id.artistBottomSheetDialog, bundle);
        }
    }
}
