package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private static final String TAG = "AlbumAdapter";

    private List<Album> albums;
    private LayoutInflater inflater;
    private Context context;

    public AlbumAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.albums = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_library_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album album = albums.get(position);

        holder.textAlbumName.setText(MusicUtil.getReadableString(album.getTitle()));
        holder.textArtistName.setText(MusicUtil.getReadableString(album.getArtistName()));

        CustomGlideRequest.Builder
                .from(context, album.getPrimary(), CustomGlideRequest.ALBUM_PIC)
                .build()
                .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public Album getItem(int position) {
        return albums.get(position);
    }

    public void setItems(List<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textAlbumName;
        TextView textArtistName;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textAlbumName = itemView.findViewById(R.id.album_name_label);
            textArtistName = itemView.findViewById(R.id.artist_name_label);
            cover = itemView.findViewById(R.id.album_cover_image_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("album_object", albums.get(getBindingAdapterPosition()));

            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.searchFragment) {
                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_albumPageFragment, bundle);
            } else if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.libraryFragment) {
                Navigation.findNavController(view).navigate(R.id.action_libraryFragment_to_albumPageFragment, bundle);
            } else if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.albumCatalogueFragment) {
                Navigation.findNavController(view).navigate(R.id.action_albumCatalogueFragment_to_albumPageFragment, bundle);
            } else if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.homeFragment) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_albumPageFragment, bundle);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("album_object", albums.get(getBindingAdapterPosition()));
            Navigation.findNavController(v).navigate(R.id.albumBottomSheetDialog, bundle);
            return true;
        }
    }
}
