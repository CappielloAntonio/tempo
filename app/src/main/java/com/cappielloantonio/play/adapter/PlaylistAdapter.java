package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
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
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.fragment.dialog.PlaylistEditorDialog;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private static final String TAG = "PlaylistAdapter";

    private final MainActivity activity;
    private final Context context;
    private final LayoutInflater mInflater;

    private List<Playlist> playlists;

    public PlaylistAdapter(MainActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.playlists = new ArrayList<>();
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
        holder.textPlaylistSongCount.setText("Song count: " + String.valueOf(playlist.getSongCount()));

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
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("playlist_object", playlists.get(getBindingAdapterPosition()));
            Navigation.findNavController(view).navigate(R.id.action_libraryFragment_to_playlistPageFragment, bundle);
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
