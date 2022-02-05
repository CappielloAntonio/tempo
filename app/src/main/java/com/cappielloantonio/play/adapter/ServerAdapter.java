package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.interfaces.SystemCallback;
import com.cappielloantonio.play.model.Server;
import com.cappielloantonio.play.repository.SystemRepository;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.dialog.ServerSignupDialog;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ViewHolder> {
    private static final String TAG = "ServerAdapter";

    private final LayoutInflater mInflater;
    private final MainActivity mainActivity;
    private final Context context;

    private List<Server> servers;

    public ServerAdapter(MainActivity mainActivity, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.servers = new ArrayList<>();
        this.mainActivity = mainActivity;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_login_server, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView serverName;
        TextView serverAddress;

        ViewHolder(View itemView) {
            super(itemView);

            serverName = itemView.findViewById(R.id.server_name_text_view);
            serverAddress = itemView.findViewById(R.id.server_address_text_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            serverName.setSelected(true);
        }

        @Override
        public void onClick(View view) {
            Server server = servers.get(getBindingAdapterPosition());
            saveServerPreference(server.getServerId(), server.getAddress(), server.getUsername(), server.getPassword(), server.isLowSecurity());

            SystemRepository systemRepository = new SystemRepository(App.getInstance());
            systemRepository.checkUserCredential(new SystemCallback() {
                @Override
                public void onError(Exception exception) {
                    Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String password, String token, String salt) {
                    enter();
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("server_object", servers.get(getBindingAdapterPosition()));

            ServerSignupDialog dialog = new ServerSignupDialog();
            dialog.setArguments(bundle);
            dialog.show(mainActivity.getSupportFragmentManager(), null);

            return true;
        }

        private void enter() {
            mainActivity.goFromLogin();
        }

        private void saveServerPreference(String serverId, String server, String user, String password, boolean isLowSecurity) {
            PreferenceUtil.getInstance(context).setServerId(serverId);
            PreferenceUtil.getInstance(context).setServer(server);
            PreferenceUtil.getInstance(context).setUser(user);
            PreferenceUtil.getInstance(context).setPassword(password);
            PreferenceUtil.getInstance(context).setLowSecurity(isLowSecurity);

            App.getSubsonicClientInstance(context, true);
        }
    }
}
