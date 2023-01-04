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
import com.cappielloantonio.play.model.Server;

import java.util.ArrayList;
import java.util.List;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ViewHolder> {
    private final Context context;
    private final ClickCallback click;

    private List<Server> servers;

    public ServerAdapter(Context context, ClickCallback click) {
        this.context = context;
        this.click = click;
        this.servers = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_login_server, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Server server = servers.get(position);

        holder.serverName.setText(server.getServerName());
        holder.serverAddress.setText(server.getAddress());
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
        TextView serverName;
        TextView serverAddress;

        ViewHolder(View itemView) {
            super(itemView);

            serverName = itemView.findViewById(R.id.server_name_text_view);
            serverAddress = itemView.findViewById(R.id.server_address_text_view);

            serverName.setSelected(true);

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
