package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.subsonic.models.ArtistWithAlbumsID3;
import com.cappielloantonio.play.subsonic.models.IndexID3;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistRepository {
    private Application application;

    private MutableLiveData<List<Artist>> starredArtists = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Artist>> listLiveRandomArtists = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Artist>> listLiveArtists = new MutableLiveData<>(new ArrayList<>());

    public ArtistRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Artist>> getStarredArtists() {
        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Artist> artists = new ArrayList<>(MappingUtil.mapArtist(response.body().getStarred2().getArtists()));
                            getArtistInfo(artists, starredArtists);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                    }
                });

        return starredArtists;
    }

    public MutableLiveData<List<Artist>> getArtists(boolean random, int size) {
        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getArtists()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Artist> artists = new ArrayList<>();

                            for (IndexID3 index : response.body().getArtists().getIndices()) {
                                artists.addAll(MappingUtil.mapArtist(index.getArtists()));
                            }

                            listLiveArtists.setValue(artists);
                            Collections.shuffle(artists);
                            getArtistInfo(artists.subList(0, artists.size() / size > 0 ? size : artists.size()), listLiveRandomArtists);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                    }
                });

        if (random) {
            return listLiveRandomArtists;
        } else {
            return listLiveArtists;
        }
    }

    public void getArtistInfo(List<Artist> artists, MutableLiveData<List<Artist>> list) {
        for (Artist artist : artists) {
            App.getSubsonicClientInstance(application, false)
                    .getBrowsingClient()
                    .getArtist(artist.getId())
                    .enqueue(new Callback<SubsonicResponse>() {
                        @Override
                        public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                            addToMutableLiveData(list, MappingUtil.mapArtistWithAlbum(response.body().getArtist()));
                        }

                        @Override
                        public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                        }
                    });
        }
    }

    private void addToMutableLiveData(MutableLiveData<List<Artist>> liveData, Artist artist) {
        List<Artist> liveArtists = liveData.getValue();
        liveArtists.add(artist);
        liveData.setValue(liveArtists);
    }
}
