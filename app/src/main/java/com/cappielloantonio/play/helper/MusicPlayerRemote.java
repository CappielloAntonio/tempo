package com.cappielloantonio.play.helper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.service.MusicService;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MusicPlayerRemote {
    private static final String TAG = "MusicPlayerRemote";

    public static MusicService musicService;

    private static final WeakHashMap<Context, ServiceBinder> mConnectionMap = new WeakHashMap<>();

    public static ServiceToken bindToService(@NonNull final Context context, final ServiceConnection callback) {
        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }

        final ContextWrapper contextWrapper = new ContextWrapper(realActivity);
        contextWrapper.startService(new Intent(contextWrapper, MusicService.class));

        final ServiceBinder binder = new ServiceBinder(callback);

        if (contextWrapper.bindService(new Intent().setClass(contextWrapper, MusicService.class), binder, Context.BIND_AUTO_CREATE)) {
            mConnectionMap.put(contextWrapper, binder);
            return new ServiceToken(contextWrapper);
        }

        return null;
    }

    public static void unbindFromService(@Nullable final ServiceToken token) {
        if (token == null) {
            return;
        }

        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceBinder mBinder = mConnectionMap.remove(mContextWrapper);
        if (mBinder == null) {
            return;
        }

        mContextWrapper.unbindService(mBinder);
        if (mConnectionMap.isEmpty()) {
            musicService = null;
        }
    }

    public static final class ServiceBinder implements ServiceConnection {
        private final ServiceConnection mCallback;

        public ServiceBinder(final ServiceConnection callback) {
            mCallback = callback;
        }

        @Override
        public void onServiceConnected(final ComponentName className, final IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            if (mCallback != null) {
                mCallback.onServiceConnected(className, service);
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName className) {
            if (mCallback != null) {
                mCallback.onServiceDisconnected(className);
            }

            musicService = null;
        }
    }

    public static final class ServiceToken {
        public ContextWrapper mWrappedContext;

        public ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }

    public static void playSongAt(final int position) {
        if (musicService != null) {
            musicService.playSongAt(position);
        }
    }

    public static void setPosition(final int position) {
        if (musicService != null) {
            musicService.setPosition(position);
        }
    }

    public static void pauseSong() {
        if (musicService != null) {
            musicService.pause();
        }
    }

    public static void playNextSong() {
        if (musicService != null) {
            musicService.playNextSong();
        }
    }

    public static void playPreviousSong() {
        if (musicService != null) {
            musicService.playPreviousSong(true);
        }
    }

    public static void back() {
        if (musicService != null) {
            musicService.back(true);
        }
    }

    public static boolean isPlaying() {
        return musicService != null && musicService.isPlaying();
    }

    public static boolean isLoading() {
        return musicService != null && musicService.isLoading();
    }

    public static void quitPlaying() {
        if (musicService != null) {
            musicService.quitPlaying();
        }
    }

    public static void resumePlaying() {
        if (musicService != null) {
            musicService.play();
        }
    }

    public static void openQueue(final List<Song> queue, final int startPosition, final boolean startPlaying) {
        Log.d(TAG, "MusicPlayerRemote - openQueue:      S " + queue.size() + "      P " + startPosition + "      SP " + startPlaying);

        if (!tryToHandleOpenPlayingQueue(queue, startPosition, startPlaying) && musicService != null) {
            musicService.openQueue(queue, startPosition, startPlaying);
        }
    }

    private static boolean tryToHandleOpenPlayingQueue(final List<Song> queue, final int startPosition, final boolean startPlaying) {
        if (getPlayingQueue() == queue) {
            if (startPlaying) {
                playSongAt(startPosition);
            } else {
                setPosition(startPosition);
            }

            return true;
        }

        return false;
    }

    public static Song getCurrentSong() {
        if (musicService != null) {
            return musicService.getCurrentSong();
        }

        return null;
    }

    public static int getPosition() {
        if (musicService != null) {
            return musicService.getPosition();
        }

        return -1;
    }

    public static List<Song> getPlayingQueue() {
        if (musicService != null) {
            return musicService.getPlayingQueue();
        }

        return new ArrayList<>();
    }

    public static int getSongProgressMillis() {
        if (musicService != null) {
            return musicService.getSongProgressMillis();
        }

        return -1;
    }

    public static int getSongDurationMillis() {
        if (musicService != null) {
            return musicService.getSongDurationMillis();
        }

        return -1;
    }

    public static int seekTo(int millis) {
        if (musicService != null) {
            return musicService.seek(millis);
        }

        return -1;
    }

    public static boolean playNext(Song song) {
        if (musicService != null) {
            QueueRepository queueRepository = new QueueRepository(App.getInstance());

            if (getPlayingQueue().size() > 0) {
                musicService.addSong(getPosition() + 1, song);
                queueRepository.insertAllAndStartNew(getPlayingQueue());
            } else {
                List<Song> songToEnqueue = new ArrayList<>();
                songToEnqueue.add(song);
                queueRepository.insertAllAndStartNew(songToEnqueue);
                openQueue(songToEnqueue, 0, true);
            }
            return true;
        }

        return false;
    }

    public static boolean playNext(@NonNull List<Song> songs) {
        if (musicService != null) {
            QueueRepository queueRepository = new QueueRepository(App.getInstance());

            if (getPlayingQueue().size() > 0) {
                musicService.addSongs(getPosition() + 1, songs);
                queueRepository.insertAllAndStartNew(getPlayingQueue());
            } else {
                List<Song> songToEnqueue = new ArrayList<>();
                songToEnqueue.addAll(songs);
                queueRepository.insertAllAndStartNew(songToEnqueue);
                openQueue(songToEnqueue, 0, true);
            }

            return true;
        }

        return false;
    }

    public static boolean enqueue(Song song) {
        if (musicService != null) {
            QueueRepository queueRepository = new QueueRepository(App.getInstance());

            if (getPlayingQueue().size() > 0) {
                musicService.addSong(song);
                queueRepository.insertAllAndStartNew(getPlayingQueue());
            } else {
                List<Song> songToEnqueue = new ArrayList<>();
                songToEnqueue.add(song);
                queueRepository.insertAllAndStartNew(songToEnqueue);
                openQueue(songToEnqueue, 0, true);
            }
            return true;
        }

        return false;
    }

    public static boolean enqueue(@NonNull List<Song> songs) {
        if (musicService != null) {
            QueueRepository queueRepository = new QueueRepository(App.getInstance());

            if (getPlayingQueue().size() > 0) {
                musicService.addSongs(songs);
                queueRepository.insertAllAndStartNew(getPlayingQueue());
            } else {
                List<Song> songToEnqueue = new ArrayList<>();
                songToEnqueue.addAll(songs);
                queueRepository.insertAllAndStartNew(songToEnqueue);
                openQueue(songToEnqueue, 0, true);
            }

            return true;
        }

        return false;
    }

    public static boolean removeFromQueue(int position) {
        if (musicService != null && position >= 0 && position < getPlayingQueue().size()) {
            musicService.removeSong(position);
            return true;
        }

        return false;
    }

    public static boolean moveSong(int from, int to) {
        if (musicService != null && from >= 0 && to >= 0 && from < getPlayingQueue().size() && to < getPlayingQueue().size()) {
            musicService.moveSong(from, to);
            return true;
        }

        return false;
    }

    public static boolean clearQueue() {
        if (musicService != null) {
            musicService.clearQueue();
            return true;
        }

        return false;
    }
}
