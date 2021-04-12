package com.cappielloantonio.play.service

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.drm.DrmSessionManager
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.*

import kotlinx.coroutines.*

import java.net.HttpURLConnection
import java.net.URL

class UnknownMediaSourceFactory(dataSourceFactory: DataSource.Factory) : MediaSourceFactory {
    private val hlsMediaSource : HlsMediaSource.Factory
    private val progressiveMediaSource : ProgressiveMediaSource.Factory

    private var loadErrorHandlingPolicy: LoadErrorHandlingPolicy

    override fun setDrmSessionManager(drmSessionManager: DrmSessionManager?): MediaSourceFactory {
        return this
    }

    override fun setDrmHttpDataSourceFactory(drmHttpDataSourceFactory: HttpDataSource.Factory?): MediaSourceFactory {
        return this
    }

    override fun setDrmUserAgent(drmUserAgent: String?): MediaSourceFactory {
        return this
    }

    override fun setLoadErrorHandlingPolicy(loadErrorHandlingPolicy: LoadErrorHandlingPolicy?): MediaSourceFactory {
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy!!
        return this
    }

    override fun getSupportedTypes(): IntArray {
        return intArrayOf()
    }

    override fun createMediaSource(mediaItem: MediaItem): MediaSource {
        val type: String? = runBlocking {
            httpGet(mediaItem.playbackProperties!!.uri.toString())
        }

        val sourceFactory: MediaSourceFactory = if (type == "application/x-mpegURL") {
            hlsMediaSource
        } else {
            progressiveMediaSource
        }

        return sourceFactory.createMediaSource(mediaItem)
    }

    private suspend fun httpGet(url: String?): String? {
        return withContext(Dispatchers.IO) {
            val request = URL(url)
            val conn = request.openConnection() as HttpURLConnection

            return@withContext conn.getHeaderField("Content-Type")
        }
    }

    init {
        hlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
        progressiveMediaSource = ProgressiveMediaSource.Factory(dataSourceFactory, DefaultExtractorsFactory())

        loadErrorHandlingPolicy = DefaultLoadErrorHandlingPolicy()
    }
}