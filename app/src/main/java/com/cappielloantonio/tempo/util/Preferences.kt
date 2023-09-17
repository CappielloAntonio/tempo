package com.cappielloantonio.tempo.util

import com.cappielloantonio.tempo.App

object Preferences {
    const val THEME = "theme"
    private const val SERVER = "server"
    private const val USER = "user"
    private const val PASSWORD = "password"
    private const val TOKEN = "token"
    private const val SALT = "salt"
    private const val LOW_SECURITY = "low_security"
    private const val BATTERY_OPTIMIZATION = "battery_optimization"
    private const val SERVER_ID = "server_id"
    private const val PLAYBACK_SPEED = "playback_speed"
    private const val SKIP_SILENCE = "skip_silence"
    private const val IMAGE_CACHE_SIZE = "image_cache_size"
    private const val IMAGE_SIZE = "image_size"
    private const val MAX_BITRATE_WIFI = "max_bitrate_wifi"
    private const val MAX_BITRATE_MOBILE = "max_bitrate_mobile"
    private const val AUDIO_TRANSCODE_FORMAT_WIFI = "audio_transcode_format_wifi"
    private const val AUDIO_TRANSCODE_FORMAT_MOBILE = "audio_transcode_format_mobile"
    private const val WIFI_ONLY = "wifi_only"
    private const val DATA_SAVING_MODE = "data_saving_mode"
    private const val SERVER_UNREACHABLE = "server_unreachable"
    private const val SYNC_STARRED_TRACKS_FOR_OFFLINE_USE = "sync_starred_tracks_for_offline_use"
    private const val QUEUE_SYNCING = "queue_syncing"
    private const val QUEUE_SYNCING_COUNTDOWN = "queue_syncing_countdown"
    private const val ROUNDED_CORNER = "rounded_corner"
    private const val ROUNDED_CORNER_SIZE = "rounded_corner_size"
    private const val PODCAST_SECTION_VISIBILITY = "podcast_section_visibility"
    private const val RADIO_SECTION_VISIBILITY = "radio_section_visibility"
    private const val MUSIC_DIRECTORY_SECTION_VISIBILITY = "music_directory_section_visibility"
    private const val REPLAY_GAIN_MODE = "replay_gain_mode"
    private const val AUDIO_TRANSCODE_PRIORITY = "audio_transcode_priority"
    private const val DOWNLOAD_STORAGE = "download_storage"
    private const val DEFAULT_DOWNLOAD_VIEW_TYPE = "default_download_view_type"
    private const val AUDIO_TRANSCODE_DOWNLOAD = "audio_transcode_download"
    private const val AUDIO_TRANSCODE_DOWNLOAD_PRIORITY = "audio_transcode_download_priority"
    private const val MAX_BITRATE_DOWNLOAD = "max_bitrate_download"
    private const val AUDIO_TRANSCODE_FORMAT_DOWNLOAD = "audio_transcode_format_download"
    private const val SHARE = "share"
    private const val ESTIMATE_CONTENT_LENGTH = "estimate_content_length"

    @JvmStatic
    fun getServer(): String? {
        return App.getInstance().preferences.getString(SERVER, null)
    }

    @JvmStatic
    fun setServer(server: String?) {
        App.getInstance().preferences.edit().putString(SERVER, server).apply()
    }

    @JvmStatic
    fun getUser(): String? {
        return App.getInstance().preferences.getString(USER, null)
    }

    @JvmStatic
    fun setUser(user: String?) {
        App.getInstance().preferences.edit().putString(USER, user).apply()
    }

    @JvmStatic
    fun getPassword(): String? {
        return App.getInstance().preferences.getString(PASSWORD, null)
    }

    @JvmStatic
    fun setPassword(password: String?) {
        App.getInstance().preferences.edit().putString(PASSWORD, password).apply()
    }

    @JvmStatic
    fun getToken(): String? {
        return App.getInstance().preferences.getString(TOKEN, null)
    }

    @JvmStatic
    fun setToken(token: String?) {
        App.getInstance().preferences.edit().putString(TOKEN, token).apply()
    }

    @JvmStatic
    fun getSalt(): String? {
        return App.getInstance().preferences.getString(SALT, null)
    }

    @JvmStatic
    fun setSalt(salt: String?) {
        App.getInstance().preferences.edit().putString(SALT, salt).apply()
    }

    @JvmStatic
    fun isLowScurity(): Boolean {
        return App.getInstance().preferences.getBoolean(LOW_SECURITY, false)
    }

    @JvmStatic
    fun setLowSecurity(isLowSecurity: Boolean) {
        App.getInstance().preferences.edit().putBoolean(LOW_SECURITY, isLowSecurity).apply()
    }

    @JvmStatic
    fun getServerId(): String? {
        return App.getInstance().preferences.getString(SERVER_ID, null)
    }

    @JvmStatic
    fun setServerId(serverId: String?) {
        App.getInstance().preferences.edit().putString(SERVER_ID, serverId).apply()
    }

    @JvmStatic
    fun askForOptimization(): Boolean {
        return App.getInstance().preferences.getBoolean(BATTERY_OPTIMIZATION, true)
    }

    @JvmStatic
    fun dontAskForOptimization() {
        App.getInstance().preferences.edit().putBoolean(BATTERY_OPTIMIZATION, false).apply()
    }

    @JvmStatic
    fun getPlaybackSpeed(): Float {
        return App.getInstance().preferences.getFloat(PLAYBACK_SPEED, 1f)
    }

    @JvmStatic
    fun setPlaybackSpeed(playbackSpeed: Float) {
        App.getInstance().preferences.edit().putFloat(PLAYBACK_SPEED, playbackSpeed).apply()
    }

    @JvmStatic
    fun isSkipSilenceMode(): Boolean {
        return App.getInstance().preferences.getBoolean(SKIP_SILENCE, false)
    }

    @JvmStatic
    fun setSkipSilenceMode(isSkipSilenceMode: Boolean) {
        App.getInstance().preferences.edit().putBoolean(SKIP_SILENCE, isSkipSilenceMode).apply()
    }

    @JvmStatic
    fun getImageCacheSize(): Int {
        return App.getInstance().preferences.getString(IMAGE_CACHE_SIZE, "500")!!.toInt()
    }

    @JvmStatic
    fun getImageSize(): Int {
        return App.getInstance().preferences.getString(IMAGE_SIZE, "-1")!!.toInt()
    }

    @JvmStatic
    fun getMaxBitrateWifi(): String {
        return App.getInstance().preferences.getString(MAX_BITRATE_WIFI, "0")!!
    }

    @JvmStatic
    fun getMaxBitrateMobile(): String {
        return App.getInstance().preferences.getString(MAX_BITRATE_MOBILE, "0")!!
    }

    @JvmStatic
    fun getAudioTranscodeFormatWifi(): String {
        return App.getInstance().preferences.getString(AUDIO_TRANSCODE_FORMAT_WIFI, "raw")!!
    }

    @JvmStatic
    fun getAudioTranscodeFormatMobile(): String {
        return App.getInstance().preferences.getString(AUDIO_TRANSCODE_FORMAT_MOBILE, "raw")!!
    }

    @JvmStatic
    fun isWifiOnly(): Boolean {
        return App.getInstance().preferences.getBoolean(WIFI_ONLY, false)
    }

    @JvmStatic
    fun isDataSavingMode(): Boolean {
        return App.getInstance().preferences.getBoolean(DATA_SAVING_MODE, false)
    }

    @JvmStatic
    fun setDataSavingMode(isDataSavingModeEnabled: Boolean) {
        App.getInstance().preferences.edit().putBoolean(DATA_SAVING_MODE, isDataSavingModeEnabled)
            .apply()
    }

    @JvmStatic
    fun isStarredSyncEnabled(): Boolean {
        return App.getInstance().preferences.getBoolean(SYNC_STARRED_TRACKS_FOR_OFFLINE_USE, false)
    }

    @JvmStatic
    fun setStarredSyncEnabled(isStarredSyncEnabled: Boolean) {
        App.getInstance().preferences.edit().putBoolean(
            SYNC_STARRED_TRACKS_FOR_OFFLINE_USE, isStarredSyncEnabled
        ).apply()
    }

    @JvmStatic
    fun showServerUnreachableDialog(): Boolean {
        return App.getInstance().preferences.getLong(
            SERVER_UNREACHABLE, 0
        ) + 360000 < System.currentTimeMillis()
    }

    @JvmStatic
    fun setServerUnreachableDatetime(datetime: Long) {
        App.getInstance().preferences.edit().putLong(SERVER_UNREACHABLE, datetime).apply()
    }

    @JvmStatic
    fun isSyncronizationEnabled(): Boolean {
        return App.getInstance().preferences.getBoolean(QUEUE_SYNCING, false)
    }

    @JvmStatic
    fun getSyncCountdownTimer(): Int {
        return App.getInstance().preferences.getString(QUEUE_SYNCING_COUNTDOWN, "5")!!.toInt()
    }

    @JvmStatic
    fun isCornerRoundingEnabled(): Boolean {
        return App.getInstance().preferences.getBoolean(ROUNDED_CORNER, false)
    }

    @JvmStatic
    fun getRoundedCornerSize(): Int {
        return App.getInstance().preferences.getString(ROUNDED_CORNER_SIZE, "12")!!.toInt()
    }

    @JvmStatic
    fun isPodcastSectionVisible(): Boolean {
        return App.getInstance().preferences.getBoolean(PODCAST_SECTION_VISIBILITY, true)
    }

    @JvmStatic
    fun setPodcastSectionHidden() {
        App.getInstance().preferences.edit().putBoolean(PODCAST_SECTION_VISIBILITY, false).apply()
    }

    @JvmStatic
    fun isRadioSectionVisible(): Boolean {
        return App.getInstance().preferences.getBoolean(RADIO_SECTION_VISIBILITY, true)
    }

    @JvmStatic
    fun setRadioSectionHidden() {
        App.getInstance().preferences.edit().putBoolean(RADIO_SECTION_VISIBILITY, false).apply()
    }

    @JvmStatic
    fun isMusicDirectorySectionVisible(): Boolean {
        return App.getInstance().preferences.getBoolean(MUSIC_DIRECTORY_SECTION_VISIBILITY, true)
    }

    @JvmStatic
    fun getReplayGainMode(): String? {
        return App.getInstance().preferences.getString(REPLAY_GAIN_MODE, "disabled")
    }

    @JvmStatic
    fun isServerPrioritized(): Boolean {
        return App.getInstance().preferences.getBoolean(AUDIO_TRANSCODE_PRIORITY, false)
    }

    @JvmStatic
    fun getDownloadStoragePreference(): Int {
        return App.getInstance().preferences.getString(DOWNLOAD_STORAGE, "0")!!.toInt()
    }

    @JvmStatic
    fun setDownloadStoragePreference(storagePreference: Int) {
        return App.getInstance().preferences.edit().putString(
            DOWNLOAD_STORAGE,
            storagePreference.toString()
        ).apply()
    }

    @JvmStatic
    fun getDefaultDownloadViewType(): String {
        return App.getInstance().preferences.getString(
            DEFAULT_DOWNLOAD_VIEW_TYPE,
            Constants.DOWNLOAD_TYPE_TRACK
        )!!
    }

    @JvmStatic
    fun setDefaultDownloadViewType(viewType: String) {
        return App.getInstance().preferences.edit().putString(
            DEFAULT_DOWNLOAD_VIEW_TYPE,
            viewType
        ).apply()
    }

    @JvmStatic
    fun preferTranscodedDownload(): Boolean {
        return App.getInstance().preferences.getBoolean(AUDIO_TRANSCODE_DOWNLOAD, false)
    }

    @JvmStatic
    fun isServerPrioritizedInTranscodedDownload(): Boolean {
        return App.getInstance().preferences.getBoolean(AUDIO_TRANSCODE_DOWNLOAD_PRIORITY, false)
    }

    @JvmStatic
    fun getBitrateTranscodedDownload(): String {
        return App.getInstance().preferences.getString(MAX_BITRATE_DOWNLOAD, "0")!!
    }

    @JvmStatic
    fun getAudioTranscodeFormatTranscodedDownload(): String {
        return App.getInstance().preferences.getString(AUDIO_TRANSCODE_FORMAT_DOWNLOAD, "raw")!!
    }

    @JvmStatic
    fun isSharingEnabled(): Boolean {
        return App.getInstance().preferences.getBoolean(SHARE, false)
    }

    @JvmStatic
    fun askForEstimateContentLength(): Boolean {
        return App.getInstance().preferences.getBoolean(ESTIMATE_CONTENT_LENGTH, false)
    }
}