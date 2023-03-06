package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.IndexID3;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ArtistCatalogueViewModel extends AndroidViewModel {
    private final MutableLiveData<List<ArtistID3>> artistList = new MutableLiveData<>(new ArrayList<>());

    public ArtistCatalogueViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ArtistID3>> getArtistList() {
        return artistList;
    }

    public void loadArtists(Context context) {
        App.getSubsonicClientInstance(context, false)
                .getBrowsingClient()
                .getArtists()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull retrofit2.Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getArtists() != null) {
                            List<ArtistID3> artists = new ArrayList<>();

                            for (IndexID3 index : response.body().getArtists().getIndices()) {
                                artists.addAll(index.getArtists());
                            }

                            artistList.setValue(artists);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });
    }
}
