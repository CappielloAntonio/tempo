package com.cappielloantonio.tempo.subsonic

import com.cappielloantonio.tempo.App
import com.cappielloantonio.tempo.subsonic.utils.CacheUtil
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitClient(subsonic: Subsonic) {
    var retrofit: Retrofit

    init {

        retrofit = Retrofit.Builder()
            .baseUrl(subsonic.url)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(getOkHttpClient(subsonic))
            .build()
    }

    private fun getOkHttpClient(subsonic: Subsonic): OkHttpClient {
        val cacheUtil = CacheUtil(60, 60 * 60 * 24 * 30)

        // BrowsingClient 60
        // MediaAnnotationClient 0
        // MediaLibraryScanningClient 0
        // MediaRetrievalClient 0
        // PlaylistClient 0
        // PodcastClient 60
        // SearchClient 60
        // SystemClient 60
        // AlbumSongListClient 60

        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(getHttpLoggingInterceptor())
            .addInterceptor(cacheUtil.offlineInterceptor)
            // .addNetworkInterceptor(cacheUtil.onlineInterceptor)
            .cache(getCache())

        if (subsonic.basicAuthHeader != null) {
            okHttpClient.addInterceptor(BasicAuthInterceptor(subsonic.basicAuthHeader))
        }

        return okHttpClient.build();
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggingInterceptor
    }

    private fun getCache(): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(App.getContext().cacheDir, cacheSize.toLong())
    }

    class BasicAuthInterceptor(private val basicAuthHeader: String) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
            val authenticatedRequest: Request = request.newBuilder()
                    .header("Authorization", basicAuthHeader).build()
            return chain.proceed(authenticatedRequest)
        }
    }
}