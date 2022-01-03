package com.cappielloantonio.play.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.adapter.PlayerSongQueueAdapter;
import com.cappielloantonio.play.databinding.InnerFragmentPlayerQueueBinding;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;

public class PlayerQueueFragment extends Fragment {
    private static final String TAG = "PlayerCoverFragment";

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
    }

    @Override
    public void onResume() {
        super.onResume();
        setMediaBrowserListenableFuture();
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

    @SuppressLint("UnsafeOptInUsageError")
    private void initializeBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    private void setMediaBrowserListenableFuture() {
        playerSongQueueAdapter.setMediaBrowserListenableFuture(mediaBrowserListenableFuture);
    }

    private void initQueueRecyclerView() {
        bind.playerQueueRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playerQueueRecyclerView.setHasFixedSize(true);

        playerSongQueueAdapter = new PlayerSongQueueAdapter(requireContext());
        bind.playerQueueRecyclerView.setAdapter(playerSongQueueAdapter);
        playerBottomSheetViewModel.getQueueSong().observe(requireActivity(), queue -> {
            if (queue != null) {
                playerSongQueueAdapter.setItems(MappingUtil.mapQueue(queue));
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
}