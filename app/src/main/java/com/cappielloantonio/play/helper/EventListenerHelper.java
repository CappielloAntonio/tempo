package com.cappielloantonio.play.helper;

import android.util.Log;

import org.jellyfin.apiclient.interaction.ApiClient;
import org.jellyfin.apiclient.interaction.ApiEventListener;
import org.jellyfin.apiclient.model.apiclient.RemoteLogoutReason;
import org.jellyfin.apiclient.model.apiclient.SessionUpdatesEventArgs;
import org.jellyfin.apiclient.model.dto.UserDto;
import org.jellyfin.apiclient.model.entities.LibraryUpdateInfo;
import org.jellyfin.apiclient.model.session.BrowseRequest;
import org.jellyfin.apiclient.model.session.GeneralCommand;
import org.jellyfin.apiclient.model.session.MessageCommand;
import org.jellyfin.apiclient.model.session.PlayRequest;
import org.jellyfin.apiclient.model.session.PlaystateRequest;
import org.jellyfin.apiclient.model.session.SessionInfoDto;
import org.jellyfin.apiclient.model.session.UserDataChangeInfo;

public class EventListenerHelper extends ApiEventListener {
    private static final String TAG = "EventListenerHelper";

    @Override
    public void onRemoteLoggedOut(ApiClient client, RemoteLogoutReason reason) {
        Log.i(TAG, "onRemoteLoggedOut: " + reason);
    }

    @Override
    public void onUserUpdated(ApiClient client, UserDto userDto) {
        Log.i(TAG, "onUserUpdated: " + userDto.getName());
    }

    @Override
    public void onLibraryChanged(ApiClient client, LibraryUpdateInfo info) {
        Log.i(TAG, "onLibraryChanged");
    }

    @Override
    public void onUserConfigurationUpdated(ApiClient client, UserDto userDto) {
        Log.i(TAG, "onUserConfigurationUpdated");
    }

    @Override
    public void onBrowseCommand(ApiClient client, BrowseRequest command) {
        Log.i(TAG, "onBrowseCommand: " + command.getItemName());
    }

    @Override
    public void onPlayCommand(ApiClient client, PlayRequest command) {
        Log.i(TAG, "onPlayCommand: " + command.getPlayCommand());
    }

    @Override
    public void onPlaystateCommand(ApiClient client, PlaystateRequest command) {
        Log.i(TAG, "onPlayStateCommand");
    }

    @Override
    public void onMessageCommand(ApiClient client, MessageCommand command) {
        Log.i(TAG, "onMessageCommand");
    }

    @Override
    public void onGeneralCommand(ApiClient client, GeneralCommand command) {
        Log.i(TAG, "onGeneralCommand: " + command.getName());
    }

    @Override
    public void onSendStringCommand(ApiClient client, String value) {
        Log.i(TAG, "onSendStringCommand");
    }

    @Override
    public void onSetVolumeCommand(ApiClient client, int value) {
        Log.i(TAG, "onSetVolumeCommand");
    }

    @Override
    public void onSetAudioStreamIndexCommand(ApiClient client, int value) {
        Log.i(TAG, "onSetAudioStreamIndexCommand");
    }

    @Override
    public void onSetSubtitleStreamIndexCommand(ApiClient client, int value) {
        Log.i(TAG, "onSetSubtitleStreamIndexCommand");
    }

    @Override
    public void onUserDataChanged(ApiClient client, UserDataChangeInfo info) {
        Log.i(TAG, "onUserDataChanged");
    }

    @Override
    public void onSessionsUpdated(ApiClient client, SessionUpdatesEventArgs args) {
        Log.i(TAG, "onSessionsUpdated");
    }

    @Override
    public void onPlaybackStart(ApiClient client, SessionInfoDto info) {
        Log.i(TAG, "onPlaybackStart");
    }

    @Override
    public void onPlaybackStopped(ApiClient client, SessionInfoDto info) {
        Log.i(TAG, "onPlaybackStopped");
    }

    @Override
    public void onSessionEnded(ApiClient client, SessionInfoDto info) {
        Log.i(TAG, "onSessionEnded");
    }
}