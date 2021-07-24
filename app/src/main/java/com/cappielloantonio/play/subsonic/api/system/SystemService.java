package com.cappielloantonio.play.subsonic.api.system;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SystemService {
    @GET("ping")
    Call<List<SubsonicResponse>> ping();
}
