package com.cappielloantonio.play.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.databinding.ItemLoginServerBinding;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Server;

import java.util.ArrayList;
import java.util.List;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<Server> servers;

    public ServerAdapter(ClickCallback click) {
        this.click = click;
        this.servers = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLoginServerBinding view = ItemLoginServerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Server server = servers.get(position);

        holder.item.serverNameTextView.setText(server.getServerName());
        holder.item.serverAddressTextView.setText(server.getAddress());
    }

    @Override
    public int getItemCount() {
        return servers.size();
    }

    public void setItems(List<Server> servers) {
        this.servers = servers;
        notifyDataSetChanged();
    }

    public Server getItem(int id) {
        return servers.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLoginServerBinding item;

        ViewHolder(ItemLoginServerBinding item) {
            super(item.getRoot());

            this.item = item;

            item.serverNameTextView.setSelected(true);

            itemView.setOnClickListener(v -> onClick());
            itemView.setOnLongClickListener(v -> onLongClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("server_object", servers.get(getBindingAdapterPosition()));

            click.onServerClick(bundle);
        }

        public boolean onLongClick() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("server_object", servers.get(getBindingAdapterPosition()));

            click.onServerLongClick(bundle);

            return false;
        }
    }
}
