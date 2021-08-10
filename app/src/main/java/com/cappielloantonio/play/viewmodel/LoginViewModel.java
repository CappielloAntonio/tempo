package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Server;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ServerRepository;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginViewModel extends AndroidViewModel {
    private ServerRepository serverRepository;
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
        if(server != null) {
            serverRepository.delete(server);
        }
        else if(toEdit != null) {
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
