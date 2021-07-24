package com.cappielloantonio.play.subsonic.api.system;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface SystemService {
    @GET("ping")
    Call<SubsonicResponse> ping(@QueryMap Map<String, String> params);

    @GET("getLicense")
    Call<SubsonicResponse> getLicense(@QueryMap Map<String, String> params);
}
