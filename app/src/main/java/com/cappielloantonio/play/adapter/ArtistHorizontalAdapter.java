package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArtistHorizontalAdapter extends RecyclerView.Adapter<ArtistHorizontalAdapter.ViewHolder> {
    private static final String TAG = "ArtistHorizontalAdapter";

    private List<Artist> artists;
    private LayoutInflater mInflater;
    private MainActivity mainActivity;
    private Context context;
    private FragmentManager fragmentManager;

    public ArtistHorizontalAdapter(MainActivity mainActivity, Context context, FragmentManager fragmentManager) {
        this.mainActivity = mainActivity;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.mInflater = LayoutInflater.from(context);
        this.artists = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_horizontal_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artist artist = artists.get(position);

        holder.artistName.setText(Html.fromHtml(artist.getName(), Html.FROM_HTML_MODE_COMPACT));
        holder.artistInfo.setText("Album count: " + String.valueOf(artist.getAlbumCount()));

        CustomGlideRequest.Builder
                .from(context, artist.getPrimary(), artist.getPrimaryBlurHash(), CustomGlideRequest.ARTIST_PIC)
                .build()
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
                Navigation.findNavController(view).navigate(R.id.action_artistListPageFragment_to_artistPageFragment, bundle);
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
