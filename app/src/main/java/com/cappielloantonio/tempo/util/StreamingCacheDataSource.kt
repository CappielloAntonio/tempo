package com.cappielloantonio.tempo.util

import android.net.Uri
import android.util.Log
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.TransferListener
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.ContentMetadata

@UnstableApi
class StreamingCacheDataSource private constructor(
    private val cacheDataSource: CacheDataSource,
): DataSource {
    private val TAG = "StreamingCacheDataSource"

    private var currentDataSpec: DataSpec? = null

    class Factory(private val cacheDatasourceFactory: CacheDataSource.Factory): DataSource.Factory {
        override fun createDataSource(): DataSource {
            return StreamingCacheDataSource(cacheDatasourceFactory.createDataSource())
        }
    }

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        return cacheDataSource.read(buffer, offset, length)
    }

    override fun addTransferListener(transferListener: TransferListener) {
        return cacheDataSource.addTransferListener(transferListener)
    }

    override fun open(dataSpec: DataSpec): Long {
        val ret = cacheDataSource.open(dataSpec)
        currentDataSpec = dataSpec
        Log.d(TAG, "Opened $currentDataSpec")
        return ret
    }

    override fun getUri(): Uri? {
        return cacheDataSource.uri
    }

    override fun close() {
        cacheDataSource.close()
        Log.d(TAG, "Closed $currentDataSpec")
        val dataSpec = currentDataSpec
        if (dataSpec != null) {
            val cacheKey = cacheDataSource.cacheKeyFactory.buildCacheKey(dataSpec)
            val contentLength = ContentMetadata.getContentLength(cacheDataSource.cache.getContentMetadata(cacheKey));
            if (contentLength == C.LENGTH_UNSET.toLong()) {
                Log.d(TAG, "Removing partial cache for $cacheKey")
                cacheDataSource.cache.removeResource(cacheKey)
            } else {
                Log.d(TAG, "Key $cacheKey has been fully cached")
            }
        }
    }
}