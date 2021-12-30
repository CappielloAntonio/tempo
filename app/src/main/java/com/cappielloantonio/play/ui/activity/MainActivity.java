package com.cappielloantonio.play.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.broadcast.receiver.ConnectivityStatusBroadcastReceiver;
import com.cappielloantonio.play.databinding.ActivityMainBinding;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.base.BaseActivity;
import com.cappielloantonio.play.ui.dialog.ConnectionAlertDialog;
import com.cappielloantonio.play.ui.dialog.ServerUnreachableDialog;
import com.cappielloantonio.play.ui.fragment.PlayerBottomSheetFragment;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.viewmodel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Objects;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    public ActivityMainBinding bind;
    private MainViewModel mainViewModel;

    private FragmentManager fragmentManager;
    private NavHostFragment navHostFragment;
    private BottomNavigationView bottomNavigationView;
    public NavController navController;
    private BottomSheetBehavior bottomSheetBehavior;

    ConnectivityStatusBroadcastReceiver connectivityStatusBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivityMainBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        setContentView(view);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        connectivityStatusBroadcastReceiver = new ConnectivityStatusBroadcastReceiver(this);
        connectivityStatusReceiverManager(true);

        checkConnectionType();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pingServer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityStatusReceiverManager(false);
        bind = null;
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            collapseBottomSheet();
        else
            super.onBackPressed();
    }

    public void init() {
        fragmentManager = getSupportFragmentManager();

        initBottomSheet();
        initNavigation();
        initServiceContent();

        if (PreferenceUtil.getInstance(this).getToken() != null) {
            goFromLogin();
        } else {
            goToLogin();
        }
    }

    // BOTTOM SHEET/NAVIGATION
    @SuppressLint("UnsafeOptInUsageError")
    private void initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.player_bottom_sheet));
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback);
        fragmentManager.beginTransaction().replace(R.id.player_bottom_sheet, new PlayerBottomSheetFragment(), "PlayerBottomSheet").commit();

        setBottomSheetInPeek(mainViewModel.isQueueLoaded());
    }

    public void setBottomSheetInPeek(Boolean isVisible) {
        if (isVisible) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    public void setBottomSheetVisibility(boolean visibility) {
        if (visibility) {
            findViewById(R.id.player_bottom_sheet).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.player_bottom_sheet).setVisibility(View.GONE);
        }
    }

    public void collapseBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void setBottomSheetDraggableState(Boolean isDraggable) {
        bottomSheetBehavior.setDraggable(isDraggable);
    }

    private final BottomSheetBehavior.BottomSheetCallback bottomSheetCallback =
            new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View view, int state) {
                    PlayerBottomSheetFragment playerBottomSheetFragment = (PlayerBottomSheetFragment) getSupportFragmentManager().findFragmentByTag("PlayerBottomSheet");

                    switch (state) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            resetMusicSession();
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            if (playerBottomSheetFragment != null) {
                                playerBottomSheetFragment.scrollOnTop();
                                playerBottomSheetFragment.goBackToFirstPage();
                            }
                        case BottomSheetBehavior.STATE_SETTLING:
                            if (playerBottomSheetFragment != null) {
                                playerBottomSheetFragment.scrollOnTop();
                            }
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            if (playerBottomSheetFragment != null) {
                                playerBottomSheetFragment.scrollOnTop();
                                setBottomSheetDraggableState(playerBottomSheetFragment.isViewPagerInFirstPage());
                            }
                        case BottomSheetBehavior.STATE_DRAGGING:
                        case BottomSheetBehavior.STATE_HALF_EXPANDED:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View view, float slideOffset) {
                    PlayerBottomSheetFragment playerBottomSheetFragment = (PlayerBottomSheetFragment) getSupportFragmentManager().findFragmentByTag("PlayerBottomSheet");
                    if (playerBottomSheetFragment != null) {
                        float condensedSlideOffset = Math.max(0.0f, Math.min(0.2f, slideOffset - 0.2f)) / 0.2f;
                        playerBottomSheetFragment.getPlayerHeader().setAlpha(1 - condensedSlideOffset);
                        playerBottomSheetFragment.getPlayerHeader().setVisibility(condensedSlideOffset > 0.99 ? View.GONE : View.VISIBLE);
                    }
                }
            };

    private void initNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        /*
         * In questo modo intercetto il cambio schermata tramite navbar e se il bottom sheet è aperto,
         * lo chiudo
         */
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED && (
                    destination.getId() == R.id.homeFragment ||
                            destination.getId() == R.id.libraryFragment ||
                            destination.getId() == R.id.searchFragment ||
                            destination.getId() == R.id.settingsFragment)
            ) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public void setBottomNavigationBarVisibility(boolean visibility) {
        if (visibility) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    private void initServiceContent() {
        MediaManager.check(getMediaBrowserListenableFuture(), this);
    }

    private void goToLogin() {
        setBottomNavigationBarVisibility(false);
        setBottomSheetVisibility(false);

        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.landingFragment) {
            navController.navigate(R.id.action_landingFragment_to_loginFragment);
        } else if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.settingsFragment) {
            navController.navigate(R.id.action_settingsFragment_to_loginFragment);
        } else if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.homeFragment) {
            navController.navigate(R.id.action_homeFragment_to_loginFragment);
        }
    }

    private void goToHome() {
        bottomNavigationView.setVisibility(View.VISIBLE);

        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.landingFragment) {
            navController.navigate(R.id.action_landingFragment_to_homeFragment);
        } else if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.loginFragment) {
            navController.navigate(R.id.action_loginFragment_to_homeFragment);
        }
    }

    public void goFromLogin() {
        setBottomSheetInPeek(mainViewModel.isQueueLoaded());
        goToHome();
    }

    public void quit() {
        resetUserSession();
        resetMusicSession();
        resetViewModel();
        goToLogin();
    }

    private void resetUserSession() {
        PreferenceUtil.getInstance(this).setUser(null);
        PreferenceUtil.getInstance(this).setServer(null);
        PreferenceUtil.getInstance(this).setPassword(null);
        PreferenceUtil.getInstance(this).setToken(null);
        PreferenceUtil.getInstance(this).setSalt(null);
        PreferenceUtil.getInstance(this).setServerId(null);
    }

    private void resetMusicSession() {
        QueueRepository queueRepository = new QueueRepository(App.getInstance());
        queueRepository.deleteAll();
        // MusicPlayerRemote.quitPlaying();
    }

    private void resetViewModel() {
        this.getViewModelStore().clear();
    }

    // CONNECTION
    private void connectivityStatusReceiverManager(boolean isActive) {
        if (isActive) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(connectivityStatusBroadcastReceiver, filter);
        } else {
            unregisterReceiver(connectivityStatusBroadcastReceiver);
        }
    }

    private void pingServer() {
        if (PreferenceUtil.getInstance(this).getToken() != null) {
            mainViewModel.ping().observe(this, isPingSuccessfull -> {
                if (!isPingSuccessfull) {
                    ServerUnreachableDialog dialog = new ServerUnreachableDialog();
                    dialog.show(getSupportFragmentManager(), null);
                }
            });
        }
    }

    private void checkConnectionType() {
        if (PreferenceUtil.getInstance(this).isWifiOnly()) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getActiveNetworkInfo().getType() != ConnectivityManager.TYPE_WIFI) {
                ConnectionAlertDialog dialog = new ConnectionAlertDialog();
                dialog.show(getSupportFragmentManager(), null);
            }
        }
    }
}