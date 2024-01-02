package com.cappielloantonio.tempo.service

import android.content.Context
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.cappielloantonio.tempo.R
import com.cappielloantonio.tempo.repository.AutomotiveRepository
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

open class MediaLibrarySessionCallback(
    context: Context,
    automotiveRepository: AutomotiveRepository
) :
    MediaLibraryService.MediaLibrarySession.Callback {
    private val TAG = "MediaLibraryServiceCall"

    init {
        MediaBrowserTree.initialize(context, automotiveRepository)
    }

    private val customLayoutCommandButtons: List<CommandButton> = listOf(
        CommandButton.Builder()
            .setDisplayName(context.getString(R.string.exo_controls_shuffle_on_description))
            .setSessionCommand(
                SessionCommand(
                    CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_ON, Bundle.EMPTY
                )
            ).setIconResId(R.drawable.exo_icon_shuffle_off).build(),

        CommandButton.Builder()
            .setDisplayName(context.getString(R.string.exo_controls_shuffle_off_description))
            .setSessionCommand(
                SessionCommand(
                    CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_OFF, Bundle.EMPTY
                )
            ).setIconResId(R.drawable.exo_icon_shuffle_on).build()
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
        if (session.isMediaNotificationController(controller) || session.isAutomotiveController(
                controller
            ) || session.isAutoCompanionController(controller)
        ) {
            val customLayout =
                customLayoutCommandButtons[if (session.player.shuffleModeEnabled) 1 else 0]

            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(mediaNotificationSessionCommands)
                .setCustomLayout(ImmutableList.of(customLayout)).build()
        }

        return MediaSession.ConnectionResult.AcceptedResultBuilder(session).build()
    }

    @OptIn(UnstableApi::class)
    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        if (CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_ON == customCommand.customAction) {
            session.player.shuffleModeEnabled = true
            session.setCustomLayout(
                session.mediaNotificationControllerInfo!!,
                ImmutableList.of(customLayoutCommandButtons[1])
            )

            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        } else if (CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_OFF == customCommand.customAction) {
            session.player.shuffleModeEnabled = false
            session.setCustomLayout(
                session.mediaNotificationControllerInfo!!,
                ImmutableList.of(customLayoutCommandButtons[0])
            )

            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        }

        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_ERROR_NOT_SUPPORTED))
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
        return MediaBrowserTree.getChildren(parentId, params)
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

    /* override fun onSearch(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        session.notifySearchResultChanged(browser, query, MediaBrowserTree.search(query).size, params)
        return Futures.immediateFuture(LibraryResult.ofVoid())
    } */

    /* override fun onGetSearchResult(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        return Futures.immediateFuture(
            LibraryResult.ofItemList(
                MediaBrowserTree.search(query), params
            )
        )
    } */

    companion object {
        private const val CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_ON =
            "android.media3.session.demo.SHUFFLE_ON"
        private const val CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_OFF =
            "android.media3.session.demo.SHUFFLE_OFF"
    }
}