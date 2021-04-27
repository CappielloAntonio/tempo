package com.cappielloantonio.play.interfaces;

import com.cappielloantonio.play.model.Song;

public interface Playback {
    void setDataSource(Song song);

    void queueDataSource(Song song);

    void setCallbacks(PlaybackCallbacks callbacks);

    boolean isReady();

    boolean isPlaying();

    boolean isLoading();

    void start();

    void pause();

    void stop();

    int getProgress();

    void setProgress(int progress);

    int getDuration();

    int getVolume();

    void setVolume(int volume);

    interface PlaybackCallbacks {
        void onStateChanged(int state);

        void onReadyChanged(boolean ready, int reason);

        void onTrackChanged(int reason);
    }
}
