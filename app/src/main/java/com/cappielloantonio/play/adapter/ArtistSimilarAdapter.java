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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class ArtistSimilarAdapter extends RecyclerView.Adapter<ArtistSimilarAdapter.ViewHolder> {
    private static final String TAG = "AlbumArtistPageAdapter";

    private final LayoutInflater inflater;
    private final Context context;

    private List<Artist> artists;

    public ArtistSimilarAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.artists = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_library_similar_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

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

    private void setArtistCover(Artist artist, ImageView cover) {
        ArtistRepository artistRepository = new ArtistRepository(App.getInstance());
        LiveData<Artist> liveData = artistRepository.getArtistFullInfo(artist.getId());
        liveData.observeForever(new Observer<Artist>() {
            @Override
            public void onChanged(Artist artist) {
                CustomGlideRequest.Builder
                        .from(
                                context,
                                artist.getId(),
                                CustomGlideRequest.ARTIST_PIC,
                                artist.getImageUrl()
                        )
                        .build()
                        .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                        .into(cover);

                liveData.removeObserver(this);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textArtistName;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textArtistName = itemView.findViewById(R.id.artist_name_label);
            cover = itemView.findViewById(R.id.similar_artist_cover_image_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            textArtistName.setSelected(true);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artists.get(getBindingAdapterPosition()));
            Navigation.findNavController(view).navigate(R.id.artistPageFragment, bundle);
        }

        @Override
        public boolean onLongClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artists.get(getBindingAdapterPosition()));
            Navigation.findNavController(view).navigate(R.id.artistBottomSheetDialog, bundle);
            return true;
        }
    }
}
