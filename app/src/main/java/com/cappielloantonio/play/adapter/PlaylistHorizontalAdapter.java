package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.dialog.PlaylistEditorDialog;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlaylistHorizontalAdapter extends RecyclerView.Adapter<PlaylistHorizontalAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "PlaylistDialogHorizontalAdapter";

    private List<Playlist> playlists;
    private List<Playlist> playlistsFull;

    private final MainActivity activity;
    private final Context context;
    private final LayoutInflater mInflater;

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

    public PlaylistHorizontalAdapter(MainActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.playlists = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_horizontal_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);

        holder.playlistTitle.setText(MusicUtil.getReadableString(playlist.getName()));
        holder.playlistTrackCount.setText(context.getString(R.string.playlist_counted_tracks, playlist.getSongCount()));
        holder.playlistDuration.setText(MusicUtil.getReadableDurationString(playlist.getDuration(), false));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public Playlist getItem(int id) {
        return playlists.get(id);
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
        TextView playlistTitle;
        TextView playlistTrackCount;
        TextView playlistDuration;

        ViewHolder(View itemView) {
            super(itemView);

            playlistTitle = itemView.findViewById(R.id.playlist_dialog_title_text_view);
            playlistTrackCount = itemView.findViewById(R.id.playlist_dialog_count_text_view);
            playlistDuration = itemView.findViewById(R.id.playlist_dialog_duration_text_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            playlistTitle.setSelected(true);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("playlist_object", playlists.get(getBindingAdapterPosition()));

            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.libraryFragment) {
                bundle.putBoolean("is_offline", false);
                Navigation.findNavController(view).navigate(R.id.action_libraryFragment_to_playlistPageFragment, bundle);
            } else if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.downloadFragment) {
                bundle.putBoolean("is_offline", true);
                Navigation.findNavController(view).navigate(R.id.action_downloadFragment_to_playlistPageFragment, bundle);
            } else if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.playlistCatalogueFragment) {
                bundle.putBoolean("is_offline", false);
                Navigation.findNavController(view).navigate(R.id.action_playlistCatalogueFragment_to_playlistPageFragment, bundle);
            }

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
