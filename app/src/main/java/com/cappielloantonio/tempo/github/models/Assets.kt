package com.cappielloantonio.tempo.github.models

import com.google.gson.annotations.SerializedName

data class Assets(
        @SerializedName("url")
        var url: String? = null,
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("node_id")
        var nodeId: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("label")
        var label: String? = null,
        @SerializedName("uploader")
        var uploader: Uploader? = Uploader(),
        @SerializedName("content_type")
        var contentType: String? = null,
        @SerializedName("state")
        var state: String? = null,
        @SerializedName("size")
        var size: Int? = null,
        @SerializedName("download_count")
        var downloadCount: Int? = null,
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("updated_at")
        var updatedAt: String? = null,
        @SerializedName("browser_download_url")
        var browserDownloadUrl: String? = null
)