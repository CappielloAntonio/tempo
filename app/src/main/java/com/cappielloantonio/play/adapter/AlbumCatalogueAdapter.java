package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlbumCatalogueAdapter extends RecyclerView.Adapter<AlbumCatalogueAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "AlbumCatalogueAdapter";

    private List<Album> albums;
    private List<Album> albumsFull;
    private LayoutInflater inflater;
    private MainActivity activity;
    private Context context;
    private Filter filtering = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Album> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(albumsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Album item : albumsFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            albums.clear();
            albums.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public AlbumCatalogueAdapter(MainActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.albums = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_library_catalogue_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album album = albums.get(position);

        holder.textAlbumName.setText(Html.fromHtml(album.getTitle(), Html.FROM_HTML_MODE_COMPACT));
        holder.textArtistName.setText(Html.fromHtml(album.getArtistName(), Html.FROM_HTML_MODE_COMPACT));

        CustomGlideRequest.Builder
                .from(context, album.getPrimary(), album.getBlurHash(), CustomGlideRequest.ALBUM_PIC)
                .build()
                .override(300)
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
        this.albumsFull = new ArrayList<>(albums);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filtering;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textAlbumName;
        TextView textArtistName;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textAlbumName = itemView.findViewById(R.id.album_name_label);
            textArtistName = itemView.findViewById(R.id.artist_name_label);
            cover = itemView.findViewById(R.id.album_catalogue_cover_image_view);

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
            }

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
