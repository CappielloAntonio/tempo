package com.cappielloantonio.play.service;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.service.notification.PlayingNotification;
import com.cappielloantonio.play.service.playback.Playback;
import com.cappielloantonio.play.util.PreferenceUtil;

import org.jellyfin.apiclient.interaction.EmptyResponse;
import org.jellyfin.apiclient.interaction.Response;
import org.jellyfin.apiclient.model.session.PlaybackProgressInfo;
import org.jellyfin.apiclient.model.session.PlaybackStartInfo;
import org.jellyfin.apiclient.model.session.PlaybackStopInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MusicService extends Service implements Playback.PlaybackCallbacks {
    public static final String PACKAGE_NAME = "com.dkanada.gramophone";

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

    public static final int TRACK_STARTED = 9;
    public static final int TRACK_CHANGED = 1;
    public static final int TRACK_ENDED = 2;

    public static final int RELEASE_WAKELOCK = 0;
    public static final int PLAY_SONG = 3;
    public static final int PREPARE_NEXT = 4;
    public static final int SET_POSITION = 5;
    public static final int FOCUS_CHANGE = 6;
    public static final int DUCK = 7;
    public static final int UNDUCK = 8;

    public static final int LOAD_QUEUE = 9;

    private final IBinder musicBinder = new MusicBinder();

    public boolean pendingQuit = false;

    private Playback playback;

    private List<Song> playingQueue = new ArrayList<>();

    private int position = -1;
    private int nextPosition = -1;

    private boolean notHandledMetaChangedForCurrentTrack;
    private boolean queuesRestored;
    private boolean pausedByTransientLossOfFocus;

    private PlayingNotification playingNotification;
    private AudioManager audioManager;
    private MediaSessionCompat mediaSession;
    private PowerManager.WakeLock wakeLock;

    private PlaybackHandler playerHandler;
    private ThrottledSeekHandler throttledSeekHandler;
    private QueueHandler queueHandler;
    private ProgressHandler progressHandler;

    private HandlerThread playerHandlerThread;
    private HandlerThread progressHandlerThread;
    private HandlerThread queueHandlerThread;

    private final BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            if (intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
                pause();
            }
        }
    };

    private final AudioManager.OnAudioFocusChangeListener audioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(final int focusChange) {
            playerHandler.obtainMessage(FOCUS_CHANGE, focusChange, 0).sendToTarget();
        }
    };

    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO;

    @Override
    public void onCreate() {
        super.onCreate();

        final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        wakeLock.setReferenceCounted(false);

        playback = new MultiPlayer(this);
        playback.setCallbacks(this);

        playerHandlerThread = new HandlerThread(PlaybackHandler.class.getName());
        playerHandlerThread.start();
        playerHandler = new PlaybackHandler(this, playerHandlerThread.getLooper());

        progressHandlerThread = new HandlerThread(ProgressHandler.class.getName());
        progressHandlerThread.start();
        progressHandler = new ProgressHandler(this, progressHandlerThread.getLooper());

        queueHandlerThread = new HandlerThread(QueueHandler.class.getName(), Process.THREAD_PRIORITY_BACKGROUND);
        queueHandlerThread.start();
        queueHandler = new QueueHandler(this, queueHandlerThread.getLooper());

        throttledSeekHandler = new ThrottledSeekHandler(playerHandler);

        registerReceiver(becomingNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

        initNotification();
        initMediaSession();
        restoreState();

        mediaSession.setActive(true);
    }

    private AudioManager getAudioManager() {
        if (audioManager == null) {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }

        return audioManager;
    }

    private void initMediaSession() {
        ComponentName mediaButtonReceiverComponentName = new ComponentName(getApplicationContext(), MediaButtonIntentReceiver.class);

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setComponent(mediaButtonReceiverComponentName);

        PendingIntent mediaButtonReceiverPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, 0);

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
                playNextSong(true);
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
                                Toast.makeText(getApplicationContext(), R.string.playlist_is_empty, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.playlist_is_empty, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ACTION_REWIND:
                        back(true);
                        break;
                    case ACTION_SKIP:
                        playNextSong(true);
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
        unregisterReceiver(becomingNoisyReceiver);

        progressHandler.sendEmptyMessage(TRACK_ENDED);
        mediaSession.setActive(false);
        quit();
        releaseResources();
        wakeLock.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    private static final class QueueHandler extends Handler {
        @NonNull
        private final WeakReference<MusicService> mService;

        public QueueHandler(final MusicService service, @NonNull final Looper looper) {
            super(looper);
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            final MusicService service = mService.get();
            switch (msg.what) {
                case LOAD_QUEUE:
                    service.restoreQueuesAndPositionIfNecessary();
                    break;
            }
        }
    }

    public void saveState() {
        savePosition();
        saveProgress();
    }

    private void savePosition() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(PreferenceUtil.POSITION, getPosition()).apply();
    }

    private void saveProgress() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(PreferenceUtil.PROGRESS, getSongProgressMillis()).apply();
    }

    private void restoreState() {
        queueHandler.removeMessages(LOAD_QUEUE);
        queueHandler.sendEmptyMessage(LOAD_QUEUE);
    }

    private synchronized void restoreQueuesAndPositionIfNecessary() {
        if (!queuesRestored && playingQueue.isEmpty()) {
            QueueRepository queueRepository = new QueueRepository(App.getInstance());

            List<Song> restoredQueue = queueRepository.getSongs();

            int restoredPosition = PreferenceManager.getDefaultSharedPreferences(this).getInt(PreferenceUtil.POSITION, -1);
            int restoredPositionInTrack = PreferenceManager.getDefaultSharedPreferences(this).getInt(PreferenceUtil.PROGRESS, -1);

            if (restoredQueue.size() > 0 && restoredPosition != -1) {
                this.playingQueue = restoredQueue;

                position = restoredPosition;
                openCurrent();

                if (restoredPositionInTrack > 0) seek(restoredPositionInTrack);

                notHandledMetaChangedForCurrentTrack = true;
                sendChangeInternal(META_CHANGED);
                sendChangeInternal(QUEUE_CHANGED);
            }
        }

        queuesRestored = true;
    }

    private void quit() {
        pause();
        playingNotification.stop();

        getAudioManager().abandonAudioFocus(audioFocusListener);
        stopSelf();
    }

    private void releaseResources() {
        playerHandler.removeCallbacksAndMessages(null);
        playerHandlerThread.quitSafely();

        progressHandler.removeCallbacksAndMessages(null);
        progressHandlerThread.quitSafely();

        queueHandler.removeCallbacksAndMessages(null);
        queueHandlerThread.quitSafely();

        playback.stop();
        mediaSession.release();
    }

    public boolean isPlaying() {
        return playback != null && playback.isPlaying();
    }

    public int getPosition() {
        return position;
    }

    public void playNextSong(boolean force) {
        playSongAt(getNextPosition(force));
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
            nextPosition = getNextPosition(false);
            playback.queueDataSource(getSongAt(nextPosition));
        }
    }

    private boolean requestFocus() {
        return getAudioManager().requestAudioFocus(audioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
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
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, song.getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, song.getAlbumName())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.getTitle())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.getDuration())
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, getPosition() + 1)
                .putLong(MediaMetadataCompat.METADATA_KEY_YEAR, song.getYear())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, null);

        metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getPlayingQueue().size());
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

    public int getNextPosition(boolean force) {
        return getPosition() + 1;
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

    public void setPosition(final int position) {
        // handle this on the handlers thread to avoid blocking the ui thread
        playerHandler.removeMessages(SET_POSITION);
        playerHandler.obtainMessage(SET_POSITION, position, 0).sendToTarget();
    }

    private void playSongAtImpl(int position) {
        openTrackAndPrepareNextAt(position);
    }

    public void pause() {
        pausedByTransientLossOfFocus = false;
        if (playback.isPlaying()) {
            playback.pause();
            notifyChange(STATE_CHANGED);
        }
    }

    public void play() {
        synchronized (this) {
            if (requestFocus()) {
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

                        // fixes a bug where the volume would stay ducked
                        // happens when audio focus GAIN event not sent
                        playerHandler.removeMessages(DUCK);
                        playerHandler.sendEmptyMessage(UNDUCK);
                    }
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.audio_focus_denied), Toast.LENGTH_SHORT).show();
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

    public long getQueueDurationMillis(int position) {
        long duration = 0;
        for (int i = position + 1; i < playingQueue.size(); i++) {
            duration += playingQueue.get(i).getDuration();
        }

        return duration;
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
                // because playing queue size might have changed
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

    public void releaseWakeLock() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override
    public void onTrackStarted() {
        progressHandler.sendEmptyMessage(TRACK_STARTED);

        notifyChange(STATE_CHANGED);
        prepareNext();
    }

    @Override
    public void onTrackWentToNext() {
        playerHandler.sendEmptyMessage(TRACK_CHANGED);
        progressHandler.sendEmptyMessage(TRACK_CHANGED);
    }

    @Override
    public void onTrackEnded() {
        playerHandler.sendEmptyMessage(TRACK_ENDED);
        progressHandler.sendEmptyMessage(TRACK_ENDED);
    }

    private static final class PlaybackHandler extends Handler {
        private final WeakReference<MusicService> mService;
        private int currentDuckVolume = 100;

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
                case DUCK:
                    if (PreferenceUtil.getInstance(service).getAudioDucking()) {
                        currentDuckVolume -= 5;
                        if (currentDuckVolume > 20) {
                            sendEmptyMessageDelayed(DUCK, 10);
                        } else {
                            currentDuckVolume = 20;
                        }
                    } else {
                        currentDuckVolume = 100;
                    }

                    service.playback.setVolume(currentDuckVolume);
                    break;

                case UNDUCK:
                    if (PreferenceUtil.getInstance(service).getAudioDucking()) {
                        currentDuckVolume += 3;
                        if (currentDuckVolume < 100) {
                            sendEmptyMessageDelayed(UNDUCK, 10);
                        } else {
                            currentDuckVolume = 100;
                        }
                    } else {
                        currentDuckVolume = 100;
                    }

                    service.playback.setVolume(currentDuckVolume);
                    break;

                case TRACK_CHANGED:
                    service.position = service.nextPosition;
                    service.prepareNextImpl();
                    service.notifyChange(META_CHANGED);
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
                        service.playNextSong(false);
                    }

                    sendEmptyMessage(RELEASE_WAKELOCK);
                    break;

                case RELEASE_WAKELOCK:
                    service.releaseWakeLock();
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

                case FOCUS_CHANGE:
                    switch (msg.arg1) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                            if (!service.isPlaying() && service.pausedByTransientLossOfFocus) {
                                service.play();
                                service.pausedByTransientLossOfFocus = false;
                            }
                            removeMessages(DUCK);
                            sendEmptyMessage(UNDUCK);
                            break;

                        case AudioManager.AUDIOFOCUS_LOSS:
                            // Lost focus for an unbounded amount of time: stop playback and release media playback
                            service.pause();
                            break;

                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            // Lost focus for a short time, but we have to stop
                            // playback. We don't release the media playback because playback
                            // is likely to resume
                            boolean wasPlaying = service.isPlaying();
                            service.pause();
                            service.pausedByTransientLossOfFocus = wasPlaying;
                            break;

                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            // Lost focus for a short time, but it's ok to keep playing
                            // at an attenuated level
                            removeMessages(UNDUCK);
                            sendEmptyMessage(DUCK);
                            break;
                    }
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

    private static final class ProgressHandler extends Handler {
        private final WeakReference<MusicService> mService;

        private ScheduledExecutorService executorService;
        private Future<?> task;

        public ProgressHandler(MusicService service, Looper looper) {
            super(looper);

            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(@NonNull final Message msg) {
            switch (msg.what) {
                case TRACK_STARTED:
                    onStart();
                case TRACK_CHANGED:
                    onNext();
                    break;
                case TRACK_ENDED:
                    onStop();
            }
        }

        public void onStart() {
            if (executorService != null) executorService.shutdownNow();

            executorService = Executors.newScheduledThreadPool(1);
            task = executorService.scheduleAtFixedRate(this::onProgress, 10, 10, TimeUnit.SECONDS);
        }

        public void onNext() {
            PlaybackStartInfo startInfo = new PlaybackStartInfo();

            startInfo.setItemId(mService.get().getCurrentSong().getId());
            startInfo.setVolumeLevel(mService.get().playback.getVolume());
            startInfo.setCanSeek(true);
            startInfo.setIsPaused(false);

            App.getApiClientInstance(App.getInstance()).ensureWebSocket();
            App.getApiClientInstance(App.getInstance()).ReportPlaybackStartAsync(startInfo, new EmptyResponse());
        }

        public void onProgress() {
            PlaybackProgressInfo progressInfo = new PlaybackProgressInfo();

            // TODO these cause a wrong thread error
            long progress = mService.get().getSongProgressMillis();
            double duration = mService.get().getSongDurationMillis();
            if (progress / duration > 0.9) {
                Song current = mService.get().getCurrentSong();
                String user = App.getApiClientInstance(App.getInstance()).getCurrentUserId();
                Date time = new Date(System.currentTimeMillis());

                App.getApiClientInstance(App.getInstance()).MarkPlayedAsync(current.getId(), user, time, new Response<>());
            }

            progressInfo.setItemId(mService.get().getCurrentSong().getId());
            progressInfo.setPositionTicks(progress * 10000);
            progressInfo.setVolumeLevel(mService.get().playback.getVolume());
            progressInfo.setIsPaused(!mService.get().playback.isPlaying());
            progressInfo.setCanSeek(true);

            App.getApiClientInstance(App.getInstance()).ReportPlaybackProgressAsync(progressInfo, new EmptyResponse());
        }

        public void onStop() {
            PlaybackStopInfo info = new PlaybackStopInfo();
            long progress = mService.get().getSongProgressMillis();

            info.setItemId(mService.get().getCurrentSong().getId());
            info.setPositionTicks(progress * 10000);

            task.cancel(true);
            executorService.shutdownNow();
        }
    }
}
