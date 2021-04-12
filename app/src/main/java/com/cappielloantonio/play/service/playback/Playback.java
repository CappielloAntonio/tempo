package com.cappielloantonio.play.service.playback;

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

    int getDuration();

    void setProgress(int progress);

    void setVolume(int volume);

    int getVolume();

    interface PlaybackCallbacks {
        void onStateChanged(int state);

        void onReadyChanged(boolean ready, int reason);

        void onTrackChanged(int reason);
    }
}
