package com.cappielloantonio.tempo.service

import android.content.Context
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.media3.common.HeartRating
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Rating
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaConstants
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionError
import androidx.media3.session.SessionResult
import com.cappielloantonio.tempo.App
import com.cappielloantonio.tempo.R
import com.cappielloantonio.tempo.repository.AutomotiveRepository
import com.cappielloantonio.tempo.subsonic.base.ApiResponse
import com.cappielloantonio.tempo.util.Constants.CUSTOM_COMMAND_TOGGLE_HEART_LOADING
import com.cappielloantonio.tempo.util.Constants.CUSTOM_COMMAND_TOGGLE_HEART_OFF
import com.cappielloantonio.tempo.util.Constants.CUSTOM_COMMAND_TOGGLE_HEART_ON
import com.cappielloantonio.tempo.util.Constants.CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_OFF
import com.cappielloantonio.tempo.util.Constants.CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_ON
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class MediaLibrarySessionCallback(
    context: Context,
    automotiveRepository: AutomotiveRepository
) :
    MediaLibraryService.MediaLibrarySession.Callback {

    init {
        MediaBrowserTree.initialize(automotiveRepository)
    }

    private val customCommandToggleShuffleModeOn = CommandButton.Builder()
        .setDisplayName(context.getString(R.string.exo_controls_shuffle_on_description))
        .setSessionCommand(
            SessionCommand(
                CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_ON, Bundle.EMPTY
            )
        ).setIconResId(R.drawable.exo_icon_shuffle_off).build()

    private val customCommandToggleShuffleModeOff = CommandButton.Builder()
        .setDisplayName(context.getString(R.string.exo_controls_shuffle_off_description))
        .setSessionCommand(
            SessionCommand(
                CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_OFF, Bundle.EMPTY
            )
        ).setIconResId(R.drawable.exo_icon_shuffle_on).build()

    private val customCommandToggleHeartOn = CommandButton.Builder()
        .setDisplayName(context.getString(R.string.exo_controls_heart_on_description))
        .setSessionCommand(
            SessionCommand(
                CUSTOM_COMMAND_TOGGLE_HEART_ON, Bundle.EMPTY
            )
        )
        .setIconResId(R.drawable.media3_icon_heart_unfilled)
        .build()

    private val customCommandToggleHeartOff = CommandButton.Builder()
        .setDisplayName(context.getString(R.string.exo_controls_heart_off_description))
        .setSessionCommand(
            SessionCommand(CUSTOM_COMMAND_TOGGLE_HEART_OFF, Bundle.EMPTY)
        )
        .setIconResId(R.drawable.media3_icon_heart_filled)
        .build()

    // Fake Command while waiting for like update command
    private val customCommandToggleHeartLoading = CommandButton.Builder()
        .setDisplayName(context.getString(R.string.cast_expanded_controller_loading))
        .setSessionCommand(
            SessionCommand(CUSTOM_COMMAND_TOGGLE_HEART_LOADING, Bundle.EMPTY)
        )
        .setIconResId(R.drawable.ic_bookmark_sync)
        .build()

    private val customLayoutCommandButtons = listOf(
        customCommandToggleShuffleModeOn,
        customCommandToggleShuffleModeOff,
        customCommandToggleHeartOn,
        customCommandToggleHeartOff,
        customCommandToggleHeartLoading,
    )

    @OptIn(UnstableApi::class)
    val mediaNotificationSessionCommands =
        MediaSession.ConnectionResult.DEFAULT_SESSION_AND_LIBRARY_COMMANDS.buildUpon()
            .also { builder ->
                customLayoutCommandButtons.forEach { commandButton ->
                    commandButton.sessionCommand?.let { builder.add(it) }
                }
            }.build()

    @OptIn(UnstableApi::class)
    override fun onConnect(
        session: MediaSession, controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {

        session.player.addListener(object : Player.Listener {
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                updateMediaNotificationCustomLayout(session)
            }

            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                updateMediaNotificationCustomLayout(session)
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updateMediaNotificationCustomLayout(session)
            }
        })

        // FIXME: I'm not sure this if is required anymore
        if (session.isMediaNotificationController(controller) || session.isAutomotiveController(
                controller
            ) || session.isAutoCompanionController(controller)
        ) {
            val customLayout =
                if (session.player.shuffleModeEnabled) customCommandToggleShuffleModeOff else customCommandToggleShuffleModeOn

            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(mediaNotificationSessionCommands)
                .setCustomLayout(ImmutableList.of(customLayout)).build()
        }

        return MediaSession.ConnectionResult.AcceptedResultBuilder(session).build()
    }

    // Update the mediaNotification after some changes
    @OptIn(UnstableApi::class)
    private fun updateMediaNotificationCustomLayout(
        session: MediaSession,
        isRatingPending: Boolean = false
    ) {
        val customLayout = mutableListOf(
            if (session.player.shuffleModeEnabled) customCommandToggleShuffleModeOff else customCommandToggleShuffleModeOn
        )

        if (session.player.currentMediaItem != null) {
            if (isRatingPending) {
                customLayout.add(customCommandToggleHeartLoading)
            } else if ((session.player.mediaMetadata.userRating as HeartRating?)?.isHeart == true) {
                customLayout.add(customCommandToggleHeartOff)
            } else {
                customLayout.add(customCommandToggleHeartOn)
            }
        }

        session.setCustomLayout(
            session.mediaNotificationControllerInfo!!,
            customLayout
        )
    }

    // Setting rating without a mediaId will set the currently listened mediaId
    override fun onSetRating(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        rating: Rating
    ): ListenableFuture<SessionResult> {
        return onSetRating(session, controller, session.player.currentMediaItem!!.mediaId, rating)
    }

    override fun onSetRating(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaId: String,
        rating: Rating
    ): ListenableFuture<SessionResult> {
        val isStaring = (rating as HeartRating).isHeart

        val networkCall = if (isStaring)
            App.getSubsonicClientInstance(false)
                .mediaAnnotationClient
                .star(mediaId, null, null)
        else
            App.getSubsonicClientInstance(false)
                .mediaAnnotationClient
                .unstar(mediaId, null, null)

        return CallbackToFutureAdapter.getFuture { completer ->
            networkCall.enqueue(object : Callback<ApiResponse?> {
                @OptIn(UnstableApi::class)
                override fun onResponse(
                    call: Call<ApiResponse?>,
                    response: Response<ApiResponse?>
                ) {
                    if (response.isSuccessful) {

                        // Search if the media item in the player should be updated
                        for (i in 0 until session.player.mediaItemCount) {
                            val mediaItem = session.player.getMediaItemAt(i)
                            if (mediaItem.mediaId == mediaId) {
                                // Update the metadata with the new rating
                                val newMetadata = mediaItem.mediaMetadata.buildUpon()
                                    .setUserRating(HeartRating(isStaring)).build()
                                // Update the mediaItem at index with the new rating
                                session.player.replaceMediaItem(
                                    i,
                                    mediaItem.buildUpon().setMediaMetadata(newMetadata).build()
                                )
                            }
                        }

                        updateMediaNotificationCustomLayout(session)
                        completer.set(SessionResult(SessionResult.RESULT_SUCCESS))
                    } else {
                        updateMediaNotificationCustomLayout(session)
                        completer.set(
                            SessionResult(
                                SessionError(
                                    response.code(),
                                    response.message()
                                )
                            )
                        )
                    }
                }

                @OptIn(UnstableApi::class)
                override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                    updateMediaNotificationCustomLayout(session)
                    completer.set(
                        SessionResult(
                            SessionError(
                                SessionError.ERROR_UNKNOWN,
                                "An error as occurred"
                            )
                        )
                    )
                }
            })
        }
    }

    @OptIn(UnstableApi::class)
    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {

        val mediaItemId = args.getString(
            MediaConstants.EXTRA_KEY_MEDIA_ID,
            session.player.currentMediaItem?.mediaId
        )

        if (CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_ON == customCommand.customAction) {
            session.player.shuffleModeEnabled = true
            updateMediaNotificationCustomLayout(session)

            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        } else if (CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_OFF == customCommand.customAction) {
            session.player.shuffleModeEnabled = false
            updateMediaNotificationCustomLayout(session)

            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        } else if (CUSTOM_COMMAND_TOGGLE_HEART_ON == customCommand.customAction || CUSTOM_COMMAND_TOGGLE_HEART_OFF == customCommand.customAction) {

            updateMediaNotificationCustomLayout(
                session,
                // Set isRatingPending only if the mediaItem is the same as the currently playing mediaItem
                session.player.currentMediaItem?.mediaId == mediaItemId
            )

            val isStaring = CUSTOM_COMMAND_TOGGLE_HEART_ON == customCommand.customAction

            return onSetRating(session, controller, HeartRating(isStaring))
        }

        return Futures.immediateFuture(
            SessionResult(
                SessionError(
                    SessionError.ERROR_NOT_SUPPORTED,
                    customCommand.customAction
                )
            )
        )
    }

    override fun onGetLibraryRoot(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        return Futures.immediateFuture(LibraryResult.ofItem(MediaBrowserTree.getRootItem(), params))
    }

    override fun onGetChildren(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        return MediaBrowserTree.getChildren(parentId)
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: List<MediaItem>
    ): ListenableFuture<List<MediaItem>> {
        return super.onAddMediaItems(
            mediaSession,
            controller,
            MediaBrowserTree.getItems(mediaItems)
        )
    }

    override fun onSearch(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        session.notifySearchResultChanged(browser, query, 60, params)
        return Futures.immediateFuture(LibraryResult.ofVoid())
    }

    override fun onGetSearchResult(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        return MediaBrowserTree.search(query)
    }
}
