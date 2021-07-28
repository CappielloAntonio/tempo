package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistRepository {
    private Application application;

    private MutableLiveData<List<Artist>> starredArtists = new MutableLiveData<>();

    public ArtistRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Artist>> getStarredArtists() {
        App.getSubsonicClientInstance(application, false).getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Artist> artists = new ArrayList<>(MappingUtil.mapArtist(response.body().getStarred2().getArtists()));
                            starredArtists.setValue(artists);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                    }
                });

        return starredArtists;
    }
}
