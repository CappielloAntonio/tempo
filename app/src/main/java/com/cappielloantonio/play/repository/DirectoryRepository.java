package com.cappielloantonio.play.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.Directory;
import com.cappielloantonio.play.subsonic.models.Indexes;
import com.cappielloantonio.play.subsonic.models.MusicFolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectoryRepository {
    private static final String TAG = "DirectoryRepository";

    public MutableLiveData<List<MusicFolder>> getMusicFolders() {
        MutableLiveData<List<MusicFolder>> liveMusicFolders = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getMusicFolders()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getMusicFolders() != null) {
                            liveMusicFolders.setValue(response.body().getSubsonicResponse().getMusicFolders().getMusicFolders());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return liveMusicFolders;
    }

    public MutableLiveData<Indexes> getIndexes(String musicFolderId, Long ifModifiedSince) {
        MutableLiveData<Indexes> liveIndexes = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getIndexes(musicFolderId, ifModifiedSince)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getIndexes() != null) {
                            liveIndexes.setValue(response.body().getSubsonicResponse().getIndexes());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return liveIndexes;
    }

    public MutableLiveData<Directory> getMusicDirectory(String id) {
        MutableLiveData<Directory> liveMusicDirectory = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getMusicDirectory(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getDirectory() != null) {
                            liveMusicDirectory.setValue(response.body().getSubsonicResponse().getDirectory());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });

        return liveMusicDirectory;
    }
}
