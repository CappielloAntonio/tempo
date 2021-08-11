package com.cappielloantonio.play.subsonic.api.medialibraryscanning;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MediaLibraryScanningService {
    @GET("startScan")
    Call<SubsonicResponse> startScan(@QueryMap Map<String, String> params);

    @GET("getScanStatus")
    Call<SubsonicResponse> getScanStatus(@QueryMap Map<String, String> params);
}
