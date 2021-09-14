package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.dialog.PlaylistEditorDialog;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class PlaylistCatalogueAdapter extends RecyclerView.Adapter<PlaylistCatalogueAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "PlaylistCatalogueAdapter";

    private final LayoutInflater mInflater;
    private final MainActivity activity;
    private final Context context;
    private final boolean isOffline;


    private final Filter filtering = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Playlist> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(playlistsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Playlist item : playlistsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
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
            playlists.clear();
            playlists.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    private List<Playlist> playlists;
    private List<Playlist> playlistsFull;

    public PlaylistCatalogueAdapter(MainActivity activity, Context context, boolean isOffline) {
        this.activity = activity;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.playlists = new ArrayList<>();
        this.isOffline = isOffline;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_library_catalogue_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);

        holder.textPlaylistName.setText(MusicUtil.getReadableString(playlist.getName()));

        CustomGlideRequest.Builder
                .from(context, playlist.getPrimary(), CustomGlideRequest.PLAYLIST_PIC, null)
                .build()
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public Playlist getItem(int position) {
        return playlists.get(position);
    }

    public void setItems(List<Playlist> playlists) {
        this.playlists = playlists;
        this.playlistsFull = new ArrayList<>(playlists);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filtering;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textPlaylistName;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textPlaylistName = itemView.findViewById(R.id.playlist_name_text);
            cover = itemView.findViewById(R.id.playlist_cover_image_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("playlist_object", playlists.get(getBindingAdapterPosition()));
            bundle.putBoolean("is_offline", isOffline);

            Navigation.findNavController(view).navigate(R.id.action_playlistCatalogueFragment_to_playlistPageFragment, bundle);

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        @Override
        public boolean onLongClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("playlist_object", playlists.get(getBindingAdapterPosition()));

            PlaylistEditorDialog dialog = new PlaylistEditorDialog();
            dialog.setArguments(bundle);
            dialog.show(activity.getSupportFragmentManager(), null);

            return true;
        }
    }
}
