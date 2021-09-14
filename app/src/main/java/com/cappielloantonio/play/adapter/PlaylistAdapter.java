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

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.dialog.PlaylistEditorDialog;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private static final String TAG = "PlaylistAdapter";

    private final MainActivity activity;
    private final Context context;
    private final LayoutInflater mInflater;
    private final boolean isDownloaded;

    private List<Playlist> playlists;

    public PlaylistAdapter(MainActivity activity, Context context, boolean isDownloaded) {
        this.activity = activity;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.playlists = new ArrayList<>();
        this.isDownloaded = isDownloaded;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_library_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);

        holder.textPlaylistName.setText(MusicUtil.getReadableString(playlist.getName()));
        holder.textPlaylistSongCount.setText(context.getString(R.string.playlist_info_song_count, playlist.getSongCount()));

        CustomGlideRequest.Builder
                .from(context, playlist.getPrimary(), CustomGlideRequest.PLAYLIST_PIC, null)
                .build()
                .into(holder.cover);

        if (isDownloaded) holder.textPlaylistSongCount.setVisibility(View.GONE);
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
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textPlaylistName;
        TextView textPlaylistSongCount;
        ImageView cover;

        ViewHolder(View itemView) {
            super(itemView);

            textPlaylistName = itemView.findViewById(R.id.playlist_name_text);
            textPlaylistSongCount = itemView.findViewById(R.id.playlist_song_counter_text);
            cover = itemView.findViewById(R.id.playlist_cover_image_view);

            itemView.setOnClickListener(this);
            if (!isDownloaded) itemView.setOnLongClickListener(this);
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
            }
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
