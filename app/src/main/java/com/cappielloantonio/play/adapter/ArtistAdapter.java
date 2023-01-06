package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@UnstableApi
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private final Context context;
    private final ClickCallback click;
    private final boolean mix;
    private final boolean bestOf;

    private List<Artist> artists;

    public ArtistAdapter(Context context, ClickCallback click, Boolean mix, Boolean bestOf) {
        this.context = context;
        this.click = click;
        this.mix = mix;
        this.bestOf = bestOf;
        this.artists = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_library_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artist artist = artists.get(position);

        holder.textArtistName.setText(MusicUtil.getReadableString(artist.getName()));

        setArtistCover(artist, holder.cover);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public Artist getItem(int position) {
        return artists.get(position);
    }

    public void setItems(List<Artist> artists) {
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

    private void setArtistCover(Artist artist, ImageView cover) {
        ArtistRepository artistRepository = new ArtistRepository(App.getInstance());
        LiveData<Artist> livedata = artistRepository.getArtistFullInfo(artist.getId());
        livedata.observeForever(new Observer<Artist>() {
            @Override
            public void onChanged(Artist artist) {
                CustomGlideRequest.Builder
                        .from(context, artist.getId(), CustomGlideRequest.ARTIST_PIC, artist.getImageUrl())
                        .build()
                        .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                        .into(cover);

                livedata.removeObserver(this);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textArtistName;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textArtistName = itemView.findViewById(R.id.artist_name_label);
            cover = itemView.findViewById(R.id.artist_cover_image_view);

            textArtistName.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artists.get(getBindingAdapterPosition()));
            bundle.putBoolean("is_mix", mix);
            bundle.putBoolean("is_best_of", bestOf);

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
