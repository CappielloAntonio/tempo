package com.cappielloantonio.tempo.github.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LatestRelease(
        @SerializedName("url")
        var url: String? = null,
        @SerializedName("assets_url")
        var assetsUrl: String? = null,
        @SerializedName("upload_url")
        var uploadUrl: String? = null,
        @SerializedName("html_url")
        var htmlUrl: String? = null,
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("author")
        var author: Author? = Author(),
        @SerializedName("node_id")
        var nodeId: String? = null,
        @SerializedName("tag_name")
        var tagName: String? = null,
        @SerializedName("target_commitish")
        var targetCommitish: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("draft")
        var draft: Boolean? = null,
        @SerializedName("prerelease")
        var prerelease: Boolean? = null,
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("published_at")
        var publishedAt: String? = null,
        @SerializedName("assets")
        var assets: ArrayList<Assets> = arrayListOf(),
        @SerializedName("tarball_url")
        var tarballUrl: String? = null,
        @SerializedName("zipball_url")
        var zipballUrl: String? = null,
        @SerializedName("body")
        var body: String? = null,
        @SerializedName("reactions")
        var reactions: Reactions? = Reactions()
)