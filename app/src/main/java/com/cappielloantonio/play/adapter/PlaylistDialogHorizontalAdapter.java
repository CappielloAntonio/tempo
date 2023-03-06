package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.subsonic.models.Playlist;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.Collections;
import java.util.List;

public class PlaylistDialogHorizontalAdapter extends RecyclerView.Adapter<PlaylistDialogHorizontalAdapter.ViewHolder> {
    private final Context context;
    private final ClickCallback click;

    private List<Playlist> playlists;

    public PlaylistDialogHorizontalAdapter(Context context, ClickCallback click) {
        this.context = context;
        this.click = click;
        this.playlists = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_playlist_dialog, parent, false);
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

    public void setItems(List<Playlist> playlists) {
        this.playlists = playlists;
        notifyDataSetChanged();
    }

    public Playlist getItem(int id) {
        return playlists.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView playlistTitle;
        TextView playlistTrackCount;
        TextView playlistDuration;

        ViewHolder(View itemView) {
            super(itemView);

            playlistTitle = itemView.findViewById(R.id.playlist_dialog_title_text_view);
            playlistTrackCount = itemView.findViewById(R.id.playlist_dialog_count_text_view);
            playlistDuration = itemView.findViewById(R.id.playlist_dialog_duration_text_view);

            playlistTitle.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
        }

        public void onClick() {


            Bundle bundle = new Bundle();
            bundle.putParcelable("playlist_object", playlists.get(getBindingAdapterPosition()));

            click.onPlaylistClick(bundle);
        }
    }
}
