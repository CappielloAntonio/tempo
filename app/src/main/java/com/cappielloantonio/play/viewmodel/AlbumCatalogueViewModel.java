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
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class AlbumCatalogueViewModel extends AndroidViewModel {
    private MutableLiveData<List<Album>> albumList = new MutableLiveData<>(new ArrayList<>());

    private int page = 0;

    public AlbumCatalogueViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Album>> getAlbumList() {
        return albumList;
    }

    public void loadAlbums(Context context, int size) {
        retrieveAlbums(context, new MediaCallback() {
            @Override
            public void onError(Exception exception) {
            }

            @Override
            public void onLoadMedia(List<?> media) {
                List<Album> liveAlbum = albumList.getValue();
                if(liveAlbum == null) liveAlbum = new ArrayList<>();
                liveAlbum.addAll(MappingUtil.mapAlbum((List<AlbumID3>) media));
                albumList.setValue(liveAlbum);

                if (media.size() == size) {
                    loadAlbums(context, size);
                }
            }
        }, size, size * page++);
    }


    private void retrieveAlbums(Context context, MediaCallback callback, int size, int offset) {
        App.getSubsonicClientInstance(context, false)
                .getAlbumSongListClient()
                .getAlbumList2("alphabeticalByName", size, offset, null, null)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, retrofit2.Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<AlbumID3> albumList = new ArrayList<>();
                            albumList.addAll(response.body().getAlbumList2().getAlbums());
                            callback.onLoadMedia(albumList);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }
}
