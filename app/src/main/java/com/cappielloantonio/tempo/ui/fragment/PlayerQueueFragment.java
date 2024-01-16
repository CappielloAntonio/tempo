package com.cappielloantonio.tempo.ui.fragment;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.databinding.InnerFragmentPlayerQueueBinding;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.ui.adapter.PlayerSongQueueAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.viewmodel.PlayerBottomSheetViewModel;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@UnstableApi
public class PlayerQueueFragment extends Fragment implements ClickCallback {
    private static final String TAG = "PlayerQueueFragment";

    private InnerFragmentPlayerQueueBinding bind;

    private PlayerBottomSheetViewModel playerBottomSheetViewModel;
    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    private PlayerSongQueueAdapter playerSongQueueAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = InnerFragmentPlayerQueueBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        playerBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PlayerBottomSheetViewModel.class);

        initQueueRecyclerView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeBrowser();
        bindMediaController();
    }

    @Override
    public void onResume() {
        super.onResume();
        setMediaBrowserListenableFuture();
        updateNowPlayingItem();
    }

    @Override
    public void onStop() {
        releaseBrowser();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initializeBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    private void bindMediaController() {
        mediaBrowserListenableFuture.addListener(() -> {
            try {
                MediaBrowser mediaBrowser = mediaBrowserListenableFuture.get();
                initShuffleButton(mediaBrowser);
                initCleanButton(mediaBrowser);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
    }

    private void setMediaBrowserListenableFuture() {
        playerSongQueueAdapter.setMediaBrowserListenableFuture(mediaBrowserListenableFuture);
    }

    private void initQueueRecyclerView() {
        bind.playerQueueRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playerQueueRecyclerView.setHasFixedSize(true);

        playerSongQueueAdapter = new PlayerSongQueueAdapter(this);
        bind.playerQueueRecyclerView.setAdapter(playerSongQueueAdapter);
        playerBottomSheetViewModel.getQueueSong().observe(getViewLifecycleOwner(), queue -> {
            if (queue != null) {
                playerSongQueueAdapter.setItems(queue.stream().map(item -> (Child) item).collect(Collectors.toList()));
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            int originalPosition = -1;
            int fromPosition = -1;
            int toPosition = -1;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                if (originalPosition == -1) {
                    originalPosition = viewHolder.getBindingAdapterPosition();
                }

                fromPosition = viewHolder.getBindingAdapterPosition();
                toPosition = target.getBindingAdapterPosition();

                /*
                 * Per spostare un elemento nella coda devo:
                 *    - Spostare graficamente la traccia da una posizione all'altra con Collections.swap()
                 *    - Spostare nel db la traccia, tramite QueueRepository
                 *    - Notificare il Service dell'avvenuto spostamento con MusicPlayerRemote.moveSong()
                 *
                 * In onMove prendo la posizione di inizio e fine, ma solo al rilascio dell'elemento procedo allo spostamento
                 * In questo modo evito che ad ogni cambio di posizione vada a riscrivere nel db
                 * Al rilascio dell'elemento chiamo il metodo clearView()
                 */

                Collections.swap(playerSongQueueAdapter.getItems(), fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

                return false;
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if (originalPosition != -1 && fromPosition != -1 && toPosition != -1) {
                    MediaManager.swap(mediaBrowserListenableFuture, playerSongQueueAdapter.getItems(), originalPosition, toPosition);
                }

                originalPosition = -1;
                fromPosition = -1;
                toPosition = -1;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                MediaManager.remove(mediaBrowserListenableFuture, playerSongQueueAdapter.getItems(), viewHolder.getBindingAdapterPosition());
                viewHolder.getBindingAdapter().notifyDataSetChanged();
            }
        }).attachToRecyclerView(bind.playerQueueRecyclerView);
    }

    private void initShuffleButton(MediaBrowser mediaBrowser) {
        bind.playerShuffleQueueFab.setOnClickListener(view -> {
            int startPosition = mediaBrowser.getCurrentMediaItemIndex() + 1;
            int endPosition = playerSongQueueAdapter.getItems().size() - 1;

            if (startPosition < endPosition) {
                ArrayList<Integer> pool = new ArrayList<>();

                for (int i = startPosition; i <= endPosition; i++) {
                    pool.add(i);
                }

                while (pool.size() >= 2) {
                    int fromPosition = (int) (Math.random() * (pool.size()));
                    int positionA = pool.get(fromPosition);
                    pool.remove(fromPosition);

                    int toPosition = (int) (Math.random() * (pool.size()));
                    int positionB = pool.get(toPosition);
                    pool.remove(toPosition);

                    Collections.swap(playerSongQueueAdapter.getItems(), positionA, positionB);
                    bind.playerQueueRecyclerView.getAdapter().notifyItemMoved(positionA, positionB);
                }

                MediaManager.shuffle(mediaBrowserListenableFuture, playerSongQueueAdapter.getItems(), startPosition, endPosition);
            }
        });
    }

    private void initCleanButton(MediaBrowser mediaBrowser) {
        bind.playerCleanQueueButton.setOnClickListener(view -> {
            int startPosition = mediaBrowser.getCurrentMediaItemIndex() + 1;
            int endPosition = playerSongQueueAdapter.getItems().size();

            MediaManager.removeRange(mediaBrowserListenableFuture, playerSongQueueAdapter.getItems(), startPosition, endPosition);
            bind.playerQueueRecyclerView.getAdapter().notifyItemRangeRemoved(startPosition, endPosition);
        });
    }

    private void updateNowPlayingItem() {
        playerSongQueueAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMediaClick(Bundle bundle) {
        MediaManager.startQueue(mediaBrowserListenableFuture, bundle.getParcelableArrayList(Constants.TRACKS_OBJECT), bundle.getInt(Constants.ITEM_POSITION));
    }
}