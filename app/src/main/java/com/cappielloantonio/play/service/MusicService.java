package com.cappielloantonio.play.service;

import static com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_AUTO;
import static com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.broadcast.receiver.MediaButtonIntentReceiver;
import com.cappielloantonio.play.interfaces.Playback;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.ui.notification.PlayingNotification;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service implements Playback.PlaybackCallbacks {
    private static final String TAG = "MusicService";

    public static final String PACKAGE_NAME = "com.antoniocappiello.play";
    public static final String ACTION_TOGGLE = PACKAGE_NAME + ".toggle";
    public static final String ACTION_PLAY = PACKAGE_NAME + ".play";
    public static final String ACTION_PLAY_PLAYLIST = PACKAGE_NAME + ".play.playlist";
    public static final String ACTION_PAUSE = PACKAGE_NAME + ".pause";
    public static final String ACTION_STOP = PACKAGE_NAME + ".stop";
    public static final String ACTION_SKIP = PACKAGE_NAME + ".skip";
    public static final String ACTION_REWIND = PACKAGE_NAME + ".rewind";
    public static final String ACTION_QUIT = PACKAGE_NAME + ".quit";
    public static final String ACTION_PENDING_QUIT = PACKAGE_NAME + ".quit.pending";
    public static final String INTENT_EXTRA_PLAYLIST = PACKAGE_NAME + ".extra.playlist";
    public static final String STATE_CHANGED = PACKAGE_NAME + ".state.changed";
    public static final String META_CHANGED = PACKAGE_NAME + ".meta.changed";
    public static final String QUEUE_CHANGED = PACKAGE_NAME + ".queue.changed";

    public static final int TRACK_CHANGED = 1;
    public static final int TRACK_ENDED = 2;
    public static final int PLAY_SONG = 3;
    public static final int PREPARE_NEXT = 4;
    public static final int SET_POSITION = 5;

    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO;

    private final IBinder musicBinder = new MusicBinder();

    public boolean pendingQuit = false;
    private Playback playback;
    private List<Song> playingQueue = new ArrayList<>();
    private int position = -1;
    private int nextPosition = -1;
    private boolean notHandledMetaChangedForCurrentTrack;

    private PlayingNotification playingNotification;
    private MediaSessionCompat mediaSession;
    private PlaybackHandler playerHandler;
    private Handler uiThreadHandler;
    private ThrottledSeekHandler throttledSeekHandler;
    private HandlerThread playerHandlerThread;

    @Override
    public void onCreate() {
        super.onCreate();

        playback = new MultiPlayer(this);
        playback.setCallbacks(this);

        playerHandlerThread = new HandlerThread(PlaybackHandler.class.getName());
        playerHandlerThread.start();
        playerHandler = new PlaybackHandler(this, playerHandlerThread.getLooper());

        throttledSeekHandler = new ThrottledSeekHandler(playerHandler);
        uiThreadHandler = new Handler();

        initNotification();
        initMediaSession();
        restoreState();

        mediaSession.setActive(true);
    }

    private void initMediaSession() {
        ComponentName mediaButtonReceiverComponentName = new ComponentName(getApplicationContext(), MediaButtonIntentReceiver.class);

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setComponent(mediaButtonReceiverComponentName);

        PendingIntent mediaButtonReceiverPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, PendingIntent.FLAG_IMMUTABLE);

        mediaSession = new MediaSessionCompat(this, getResources().getString(R.string.app_name), mediaButtonReceiverComponentName, mediaButtonReceiverPendingIntent);
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                play();
            }

            @Override
            public void onPause() {
                pause();
            }

            @Override
            public void onSkipToNext() {
                playNextSong();
            }

            @Override
            public void onSkipToPrevious() {
                back(true);
            }

            @Override
            public void onStop() {
                quit();
            }

            @Override
            public void onSeekTo(long pos) {
                seek((int) pos);
            }

            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                return MediaButtonIntentReceiver.handleIntent(MusicService.this, mediaButtonEvent);
            }
        });

        mediaSession.setMediaButtonReceiver(mediaButtonReceiverPendingIntent);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getAction() != null) {
                String action = intent.getAction();
                switch (action) {
                    case ACTION_TOGGLE:
                        if (isPlaying()) {
                            pause();
                        } else {
                            play();
                        }
                        break;
                    case ACTION_PAUSE:
                        pause();
                        break;
                    case ACTION_PLAY:
                        play();
                        break;
                    case ACTION_PLAY_PLAYLIST:
                        Playlist playlist = intent.getParcelableExtra(INTENT_EXTRA_PLAYLIST);
                        if (playlist != null) {
                            List<Song> playlistSongs = new ArrayList<>();
                            if (!playlistSongs.isEmpty()) {
                                openQueue(playlistSongs, 0, true);
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.exo_info_empty_playlist, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.exo_info_empty_playlist, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ACTION_REWIND:
                        back(true);
                        break;
                    case ACTION_SKIP:
                        playNextSong();
                        break;
                    case ACTION_STOP:
                    case ACTION_QUIT:
                        pendingQuit = false;
                        quit();
                        break;
                    case ACTION_PENDING_QUIT:
                        pendingQuit = true;
                        break;
                }
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaSession.setActive(false);
        quit();
        releaseResources();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    public void saveState() {
        savePosition();
        saveProgress();
    }

    private void savePosition() {
        PreferenceUtil.getInstance(getApplicationContext()).setPosition(getPosition());
    }

    private void saveProgress() {
        Log.d(TAG, "saveProgress(): " + getSongProgressMillis());
        PreferenceUtil.getInstance(getApplicationContext()).setProgress(getSongProgressMillis());
    }

    private void restoreState() {
        QueueRepository queueRepository = new QueueRepository(App.getInstance());
        List<Song> restoredQueue = queueRepository.getSongs();

        int restoredPosition = PreferenceUtil.getInstance(getApplicationContext()).getPosition();
        int restoredPositionInTrack = PreferenceUtil.getInstance(getApplicationContext()).getProgress();

        if (restoredQueue.size() > 0 && restoredPosition != -1) {
            this.playingQueue = restoredQueue;

            position = restoredPosition;
            openCurrent();

            if (restoredPositionInTrack > 0) seek(restoredPositionInTrack);

            notHandledMetaChangedForCurrentTrack = true;
            handleChangeInternal(META_CHANGED);
            handleChangeInternal(QUEUE_CHANGED);
        }
    }

    private void quit() {
        pause();
        playingNotification.stop();
        stopSelf();
    }

    private void releaseResources() {
        playerHandler.removeCallbacksAndMessages(null);
        playerHandlerThread.quitSafely();

        playback.stop();
        mediaSession.release();
    }

    public boolean isPlaying() {
        return playback != null && playback.isPlaying();
    }

    public boolean isLoading() {
        return playback != null && playback.isLoading();
    }

    public void quitPlaying() {
        quit();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(final int position) {
        // handle this on the handlers thread to avoid blocking the ui thread
        playerHandler.removeMessages(SET_POSITION);
        playerHandler.obtainMessage(SET_POSITION, position, 0).sendToTarget();
    }

    public void playNextSong() {
        playSongAt(getNextPosition());
    }

    private void openTrackAndPrepareNextAt(int position) {
        synchronized (this) {
            this.position = position;

            openCurrent();
            playback.start();

            notifyChange(META_CHANGED);
            notHandledMetaChangedForCurrentTrack = false;
        }
    }

    private void openCurrent() {
        synchronized (this) {
            // current song will be null when queue is cleared
            if (getCurrentSong() == null) return;

            playback.setDataSource(getCurrentSong());
        }
    }

    private void prepareNext() {
        playerHandler.removeMessages(PREPARE_NEXT);
        playerHandler.obtainMessage(PREPARE_NEXT).sendToTarget();
    }

    private void prepareNextImpl() {
        synchronized (this) {
            nextPosition = getNextPosition();
            playback.queueDataSource(getSongAt(nextPosition));
        }

        increaseSongCount();
    }

    public void initNotification() {
        playingNotification = new PlayingNotification();
        playingNotification.init(this);
    }

    public void updateNotification() {
        if (playingNotification != null && getCurrentSong() != null) {
            playingNotification.update();
        }
    }

    private void updateMediaSessionState() {
        mediaSession.setPlaybackState(
                new PlaybackStateCompat.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED, getSongProgressMillis(), 1)
                        .build());
    }

    @SuppressLint("CheckResult")
    private void updateMediaSessionMetadata() {
        final Song song = getCurrentSong();

        if (song == null) {
            mediaSession.setMetadata(null);
            return;
        }

        final MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, MusicUtil.getReadableString(song.getArtistName()))
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, MusicUtil.getReadableString(song.getArtistName()))
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, MusicUtil.getReadableString(song.getAlbumName()))
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, MusicUtil.getReadableString(song.getTitle()))
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.getDuration() * 1000)
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, getPosition() + 1)
                .putLong(MediaMetadataCompat.METADATA_KEY_YEAR, song.getYear())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, null);

        metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getPlayingQueue().size());

        mediaSession.setMetadata(metaData.build());
    }

    public void runOnUiThread(Runnable runnable) {
        uiThreadHandler.post(runnable);
    }

    public Song getCurrentSong() {
        return getSongAt(getPosition());
    }

    public Song getSongAt(int position) {
        if (position >= 0 && position < getPlayingQueue().size()) {
            return getPlayingQueue().get(position);
        } else {
            return new Song();
        }
    }

    public int getNextPosition() {
        int position = getPosition() + 1;

        if (isLastTrack()) {
            position -= 1;
        }

        return position;
    }

    private boolean isLastTrack() {
        return getPosition() == getPlayingQueue().size() - 1;
    }

    public List<Song> getPlayingQueue() {
        return playingQueue;
    }

    public void openQueue(@Nullable final List<Song> playingQueue, final int startPosition, final boolean startPlaying) {
        if (playingQueue != null && !playingQueue.isEmpty() && startPosition >= 0 && startPosition < playingQueue.size()) {
            this.playingQueue = playingQueue;
            this.position = startPosition;

            if (startPlaying) {
                playSongAt(position);
            } else {
                setPosition(position);
            }

            notifyChange(QUEUE_CHANGED);
        }
    }

    public void addSong(int position, Song song) {
        playingQueue.add(position, song);
        notifyChange(QUEUE_CHANGED);
    }

    public void addSong(Song song) {
        playingQueue.add(song);
        notifyChange(QUEUE_CHANGED);
    }

    public void addSongs(int position, List<Song> songs) {
        playingQueue.addAll(position, songs);
        notifyChange(QUEUE_CHANGED);
    }

    public void addSongs(List<Song> songs) {
        playingQueue.addAll(songs);
        notifyChange(QUEUE_CHANGED);
    }

    public void removeSong(int position) {
        playingQueue.remove(position);
        reposition(position);
        notifyChange(QUEUE_CHANGED);
    }

    private void reposition(int deletedPosition) {
        int currentPosition = getPosition();
        if (deletedPosition < currentPosition) {
            position = currentPosition - 1;
        } else if (deletedPosition == currentPosition) {
            if (playingQueue.size() > deletedPosition) {
                setPosition(position);
            } else {
                setPosition(position - 1);
            }
        }
    }

    public void moveSong(int from, int to) {
        if (from == to) return;
        final int currentPosition = getPosition();
        Song songToMove = playingQueue.remove(from);
        playingQueue.add(to, songToMove);

        if (from > currentPosition && to <= currentPosition) {
            position = currentPosition + 1;
        } else if (from < currentPosition && to >= currentPosition) {
            position = currentPosition - 1;
        } else if (from == currentPosition) {
            position = to;
        }

        notifyChange(QUEUE_CHANGED);
    }

    public void clearQueue() {
        playingQueue.clear();

        setPosition(-1);
        notifyChange(QUEUE_CHANGED);
    }

    public void playSongAt(final int position) {
        // handle this on the handlers thread to avoid blocking the ui thread
        playerHandler.removeMessages(PLAY_SONG);
        playerHandler.obtainMessage(PLAY_SONG, position, 0).sendToTarget();
    }

    private void playSongAtImpl(int position) {
        openTrackAndPrepareNextAt(position);
    }

    public void pause() {
        if (playback.isPlaying()) {
            playback.pause();
            notifyChange(STATE_CHANGED);
        }
    }

    public void play() {
        synchronized (this) {
            if (!playback.isPlaying()) {
                if (!playback.isReady()) {
                    playSongAt(getPosition());
                } else {
                    playback.start();
                    if (notHandledMetaChangedForCurrentTrack) {
                        handleChangeInternal(META_CHANGED);
                        notHandledMetaChangedForCurrentTrack = false;
                    }
                    notifyChange(STATE_CHANGED);
                }
            }
        }
    }

    public void playPreviousSong(boolean force) {
        playSongAt(getPreviousPosition(force));
    }

    public void back(boolean force) {
        if (getSongProgressMillis() > 5000) {
            seek(0);
        } else {
            playPreviousSong(force);
        }
    }

    public int getPreviousPosition(boolean force) {
        return getPosition() - 1;
    }

    public int getSongProgressMillis() {
        return playback.getProgress();
    }

    public int getSongDurationMillis() {
        return playback.getDuration();
    }

    public int seek(int millis) {
        synchronized (this) {
            playback.setProgress(millis);
            throttledSeekHandler.notifySeek();
            return millis;
        }
    }

    private void notifyChange(@NonNull final String what) {
        handleChangeInternal(what);
        sendChangeInternal(what);
    }

    private void sendChangeInternal(final String what) {
        sendBroadcast(new Intent(what));
    }

    private void handleChangeInternal(@NonNull final String what) {
        switch (what) {
            case STATE_CHANGED:
                updateNotification();
                updateMediaSessionState();
                if (!isPlaying()) saveProgress();
                break;
            case META_CHANGED:
                updateNotification();
                updateMediaSessionMetadata();
                updateMediaSessionState();
                savePosition();
                saveProgress();
                break;
            case QUEUE_CHANGED:
                updateMediaSessionMetadata();
                saveState();
                if (playingQueue.size() > 0) {
                    prepareNext();
                } else {
                    playingNotification.stop();
                }
                break;
        }
    }

    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }

    private void increaseSongCount() {
        SongRepository songRepository = new SongRepository(App.getInstance());
        QueueRepository queueRepository = new QueueRepository(App.getInstance());

        songRepository.scrobble(getCurrentSong().getId());
        queueRepository.setTimestamp(getCurrentSong());
    }

    @Override
    public void onStateChanged(int state) {
        notifyChange(STATE_CHANGED);
    }

    @Override
    public void onReadyChanged(boolean ready, int reason) {
        notifyChange(STATE_CHANGED);

        if (ready) {
            prepareNext();
        }
    }

    @Override
    public void onTrackChanged(int reason) {
        if (reason == MEDIA_ITEM_TRANSITION_REASON_AUTO) {
            playerHandler.sendEmptyMessage(TRACK_CHANGED);
        } else if (reason == MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED) {
            prepareNext();
        }
    }

    private static final class PlaybackHandler extends Handler {
        private final WeakReference<MusicService> mService;

        public PlaybackHandler(final MusicService service, @NonNull final Looper looper) {
            super(looper);
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(@NonNull final Message msg) {
            final MusicService service = mService.get();
            if (service == null) {
                return;
            }

            switch (msg.what) {
                case TRACK_CHANGED:
                    if (service.isLastTrack()) {
                        service.pause();
                        service.seek(0);
                        service.notifyChange(STATE_CHANGED);
                    } else {
                        service.position = service.nextPosition;
                        service.prepareNextImpl();
                        service.notifyChange(META_CHANGED);
                        service.notifyChange(QUEUE_CHANGED);
                    }
                    break;

                case TRACK_ENDED:
                    // if there is a timer finished, don't continue
                    if (service.pendingQuit && service.isLastTrack()) {
                        service.notifyChange(STATE_CHANGED);
                        service.seek(0);

                        if (service.pendingQuit) {
                            service.pendingQuit = false;
                            service.quit();
                            break;
                        }
                    } else {
                        service.playNextSong();
                    }
                    break;

                case PLAY_SONG:
                    service.playSongAtImpl(msg.arg1);
                    service.notifyChange(STATE_CHANGED);
                    break;

                case SET_POSITION:
                    service.openTrackAndPrepareNextAt(msg.arg1);
                    service.notifyChange(STATE_CHANGED);
                    break;

                case PREPARE_NEXT:
                    service.prepareNextImpl();
                    break;
            }
        }
    }

    public class MusicBinder extends Binder {
        @NonNull
        public MusicService getService() {
            return MusicService.this;
        }
    }

    private class ThrottledSeekHandler implements Runnable {
        // milliseconds to throttle before calling run to aggregate events
        private static final long THROTTLE = 500;
        private final Handler mHandler;

        public ThrottledSeekHandler(Handler handler) {
            mHandler = handler;
        }

        public void notifySeek() {
            mHandler.removeCallbacks(this);
            mHandler.postDelayed(this, THROTTLE);
        }

        @Override
        public void run() {
            notifyChange(STATE_CHANGED);
        }
    }
}
