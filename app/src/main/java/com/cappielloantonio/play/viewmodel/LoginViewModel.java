package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Server;
import com.cappielloantonio.play.repository.ServerRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    private final ServerRepository serverRepository;

    private Server toEdit = null;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        serverRepository = new ServerRepository(application);
    }

    public LiveData<List<Server>> getServerList() {
        return serverRepository.getLiveServer();
    }

    public void addServer(Server server) {
        serverRepository.insert(server);
    }

    public void deleteServer(Server server) {
        if (server != null) {
            serverRepository.delete(server);
        } else if (toEdit != null) {
            serverRepository.delete(toEdit);
        }
    }

    public void setServerToEdit(Server server) {
        toEdit = server;
    }

    public Server getServerToEdit() {
        return toEdit;
    }
}
