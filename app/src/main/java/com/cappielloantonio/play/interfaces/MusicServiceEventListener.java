package com.cappielloantonio.play.interfaces;

public interface MusicServiceEventListener {

    void onServiceConnected();

    void onServiceDisconnected();

    void onQueueChanged();

    void onPlayMetadataChanged();

    void onPlayStateChanged();

    void onRepeatModeChanged();
}
