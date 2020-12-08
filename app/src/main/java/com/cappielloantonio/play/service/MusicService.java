package com.cappielloantonio.play.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.cappielloantonio.play.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MusicService extends Service implements AudioManager.OnAudioFocusChangeListener {
    private AudioManager audioManager;
    private MediaSource mediaSource;
    private SimpleExoPlayer player;

    @Override
    public void onCreate() {
        audioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("playStop");
        if (input != null && input.equals("play")) {
            if (requestFocus()) {
                initPlayer();
                play();
            }
        } else {
            stop();
        }
        startForeground(1, getNotification());
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentText("Running ...")
                .setContentTitle("Play")
                .setOngoing(true)
                .setSmallIcon(R.drawable.exo_controls_shuffle_on)
                .setChannelId("PlayApp");
        return builder.build();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            stop();
            System.exit(0);
        }
    }

    private boolean requestFocus() {
        return (audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
    }

    private void play() {
        player.setForegroundMode(true);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }

    private void stop() {
        player.setPlayWhenReady(false);
        player.stop();
    }

    private void initPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(getApplicationContext(), "exoPlayerSample"));
        mediaSource = new ProgressiveMediaSource.Factory(defaultDataSourceFactory).createMediaSource(Uri.parse("http://192.168.1.81:8096/Audio/5656e9fd11e38ba95ba4871bc061991a/universal?UserId=34addd030b4545e5ac4300dc322c9f73&DeviceId=e40853e4e7ab76f1&MaxStreamingBitrate=10000000&Container=flac|flac,mp3|mp3,opus|opus,m4a|aac,ogg|vorbis,ogg|opus,mka|opus&TranscodingContainer=ts&TranscodingProtocol=hls&AudioCodec=aac&api_key=7e6626ca220d4b01961022e148868d41"));
    }
}
