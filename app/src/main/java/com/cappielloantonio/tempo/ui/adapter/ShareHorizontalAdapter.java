package com.cappielloantonio.tempo.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.ItemHorizontalShareBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.Share;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.util.UIUtil;

import java.util.Collections;
import java.util.List;

public class ShareHorizontalAdapter extends RecyclerView.Adapter<ShareHorizontalAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<Share> shares;

    public ShareHorizontalAdapter(ClickCallback click) {
        this.click = click;
        this.shares = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHorizontalShareBinding view = ItemHorizontalShareBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Share share = shares.get(position);

        holder.item.shareTitleTextView.setText(share.getDescription());
        holder.item.shareSubtitleTextView.setText(holder.itemView.getContext().getString(R.string.share_subtitle_item, UIUtil.getReadableDate(share.getExpires())));

        if (share.getEntries() != null && !share.getEntries().isEmpty()) CustomGlideRequest.Builder
                .from(holder.itemView.getContext(), share.getEntries().get(0).getCoverArtId(), CustomGlideRequest.ResourceType.Album)
                .build()
                .into(holder.item.shareCoverImageView);
    }

    @Override
    public int getItemCount() {
        return shares.size();
    }

    public void setItems(List<Share> shares) {
        this.shares = shares;
        notifyDataSetChanged();
    }

    public Share getItem(int id) {
        return shares.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHorizontalShareBinding item;

        ViewHolder(ItemHorizontalShareBinding item) {
            super(item.getRoot());

            this.item = item;

            item.shareTitleTextView.setSelected(true);
            item.shareSubtitleTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());

            item.shareButton.setOnClickListener(v -> onLongClick());
        }

        private void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.SHARE_OBJECT, shares.get(getBindingAdapterPosition()));

            click.onShareClick(bundle);
        }

        private boolean onLongClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.SHARE_OBJECT, shares.get(getBindingAdapterPosition()));

            click.onShareLongClick(bundle);

            return true;
        }
    }
}
