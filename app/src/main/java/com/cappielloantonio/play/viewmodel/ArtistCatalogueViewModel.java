package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.IndexID3;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ArtistCatalogueViewModel extends AndroidViewModel {
    private MutableLiveData<List<Artist>> artistList = new MutableLiveData<>(new ArrayList<>());

    private ArtistRepository artistRepository;
    private String query = "";

    public ArtistCatalogueViewModel(@NonNull Application application) {
        super(application);

        artistRepository = new ArtistRepository(application);
    }

    public LiveData<List<Artist>> getArtistList() {
        return artistList;
    }

    public void loadArtists(Context context) {
        App.getSubsonicClientInstance(context, false)
                .getBrowsingClient()
                .getArtists()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, retrofit2.Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<ArtistID3> artists = new ArrayList<>();

                            for (IndexID3 index : response.body().getArtists().getIndices()) {
                                artists.addAll(index.getArtists());
                            }

                            artistList.setValue(MappingUtil.mapArtist(artists));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });
    }
}
