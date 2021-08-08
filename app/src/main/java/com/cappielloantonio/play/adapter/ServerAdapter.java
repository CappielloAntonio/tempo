package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.interfaces.SystemCallback;
import com.cappielloantonio.play.model.Server;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SystemRepository;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ViewHolder> {
    private static final String TAG = "ServerAdapter";

    private List<Server> servers;
    private final LayoutInflater mInflater;
    private MainActivity mainActivity;
    private Context context;

    public ServerAdapter(MainActivity mainActivity, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.servers = new ArrayList<>();
        this.mainActivity = mainActivity;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    public List<Server> getItems() {
        return this.servers;
    }

    public void setItems(List<Server> servers) {
        this.servers = servers;
        notifyDataSetChanged();
    }

    public Server getItem(int id) {
        return servers.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView serverName;
        TextView serverAddress;

        ViewHolder(View itemView) {
            super(itemView);

            serverName = itemView.findViewById(R.id.server_name_text_view);
            serverAddress = itemView.findViewById(R.id.server_address_text_view);

            itemView.setOnClickListener(this);

            serverName.setSelected(true);
        }

        @Override
        public void onClick(View view) {
            Server server = servers.get(getBindingAdapterPosition());
            saveServerPreference(server.getAddress(), server.getUsername(), server.getToken(), server.getSalt());
            refreshSubsonicClientInstance();

            SystemRepository systemRepository = new SystemRepository(App.getInstance());
            systemRepository.checkUserCredential(new SystemCallback() {
                @Override
                public void onError(Exception exception) {
                    Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String token, String salt) {
                    saveServerPreference(null, null, token, salt);
                    enter();
                }
            });
        }

        private void enter() {
            mainActivity.goFromLogin();
        }

        private void saveServerPreference(String server, String user, String token, String salt) {
            if (user != null) PreferenceUtil.getInstance(context).setUser(user);
            if (server != null) PreferenceUtil.getInstance(context).setServer(server);

            if (token != null && salt != null) {
                PreferenceUtil.getInstance(context).setToken(token);
                PreferenceUtil.getInstance(context).setSalt(salt);
            }
        }

        private void refreshSubsonicClientInstance() {
            PreferenceUtil.getInstance(context).setServerId(servers.get(getBindingAdapterPosition()).getServerId());
            App.getSubsonicClientInstance(context, true);
        }
    }
}
