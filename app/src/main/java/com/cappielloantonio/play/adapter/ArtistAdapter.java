package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.media3.session.MediaBrowser;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private static final String TAG = "ArtistAdapter";

    private final LayoutInflater inflater;
    private final MainActivity mainActivity;
    private final Context context;

    private List<Artist> artists;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    public ArtistAdapter(MainActivity mainActivity, Context context) {
        this.mainActivity = mainActivity;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.artists = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_library_artist, parent, false);
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

    public void setMediaBrowserListenableFuture(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        this.mediaBrowserListenableFuture = mediaBrowserListenableFuture;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textArtistName;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textArtistName = itemView.findViewById(R.id.artist_name_label);
            cover = itemView.findViewById(R.id.artist_cover_image_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            textArtistName.setSelected(true);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artists.get(getBindingAdapterPosition()));

            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.searchFragment) {
                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_artistPageFragment, bundle);
            } else if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.libraryFragment) {
                Navigation.findNavController(view).navigate(R.id.action_libraryFragment_to_artistPageFragment, bundle);
            } else if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.artistCatalogueFragment) {
                Navigation.findNavController(view).navigate(R.id.action_artistCatalogueFragment_to_artistPageFragment, bundle);
            } else if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.homeFragment) {
                Snackbar.make(mainActivity.bind.getRoot(), R.string.artist_adapter_radio_station_starting, Snackbar.LENGTH_LONG)
                        .setAnchorView(mainActivity.bind.playerBottomSheet)
                        .show();

                if (mediaBrowserListenableFuture != null) {
                    ArtistRepository artistRepository = new ArtistRepository(App.getInstance());
                    artistRepository.getInstantMix(artists.get(getBindingAdapterPosition()), 20, new MediaCallback() {
                        @Override
                        public void onError(Exception exception) {
                            Log.e(TAG, "onError() " + exception.getMessage());
                        }

                        @Override
                        public void onLoadMedia(List<?> media) {
                            if (media.size() > 0) {
                                MediaManager.startQueue(mediaBrowserListenableFuture, context, (ArrayList<Media>) media, 0);
                                mainActivity.setBottomSheetInPeek(true);
                            } else {
                                Toast.makeText(context, context.getString(R.string.artist_error_retrieving_radio), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artists.get(getBindingAdapterPosition()));
            Navigation.findNavController(v).navigate(R.id.artistBottomSheetDialog, bundle);
            return true;
        }
    }
}
