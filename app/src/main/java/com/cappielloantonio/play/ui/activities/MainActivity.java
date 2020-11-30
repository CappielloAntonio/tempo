package com.cappielloantonio.play.ui.activities;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.broadcast.receiver.ConnectivityStatusBroadcastReceiver;
import com.cappielloantonio.play.databinding.ActivityMainBinding;
import com.cappielloantonio.play.ui.activities.base.BaseActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.util.SyncUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jellyfin.apiclient.interaction.EmptyResponse;
import org.jellyfin.apiclient.interaction.Response;
import org.jellyfin.apiclient.model.session.ClientCapabilities;
import org.jellyfin.apiclient.model.system.SystemInfo;

import java.util.Objects;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    public ActivityMainBinding activityMainBinding;

    private FragmentManager fragmentManager;
    private NavHostFragment navHostFragment;
    private BottomNavigationView bottomNavigationView;
    public NavController navController;

    ConnectivityStatusBroadcastReceiver connectivityStatusBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        connectivityStatusBroadcastReceiver = new ConnectivityStatusBroadcastReceiver(this);
        connectivityStatusReceiverManager(true);

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityStatusReceiverManager(false);
    }

    public void init() {
        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        if (PreferenceUtil.getInstance(this).getToken() != null) {
            checkPreviousSession();
        } else {
            goToLogin();
        }
    }

    private void checkPreviousSession() {
        App.getApiClientInstance(getApplicationContext()).ChangeServerLocation(PreferenceUtil.getInstance(this).getServer());
        App.getApiClientInstance(getApplicationContext()).SetAuthenticationInfo(PreferenceUtil.getInstance(this).getToken(), PreferenceUtil.getInstance(this).getUser());
        App.getApiClientInstance(getApplicationContext()).GetSystemInfoAsync(new Response<SystemInfo>() {
            @Override
            public void onResponse(SystemInfo result) {
                ClientCapabilities clientCapabilities = new ClientCapabilities();
                clientCapabilities.setSupportsMediaControl(true);
                clientCapabilities.setSupportsPersistentIdentifier(true);

                App.getApiClientInstance(getApplicationContext()).ensureWebSocket();
                App.getApiClientInstance(getApplicationContext()).ReportCapabilities(clientCapabilities, new EmptyResponse());

                goFromLogin();
            }

            @Override
            public void onError(Exception exception) {
                goFromLogin();
            }
        });
    }

    // True: VISIBLE
    // False: GONE
    public void setBottomNavigationBarVisibility(boolean visibility) {
        if (visibility) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    public void goToLogin() {
        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.landingFragment)
            navController.navigate(R.id.action_landingFragment_to_loginFragment);
    }

    public void goToSync() {
        bottomNavigationView.setVisibility(View.GONE);

        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.loginFragment) {
            Bundle bundle = SyncUtil.getSyncBundle(true, true, true, true, true, false);
            navController.navigate(R.id.action_loginFragment_to_syncFragment, bundle);
        } else if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.homeFragment) {
            Bundle bundle = SyncUtil.getSyncBundle(true, true, true, true, true, false);
            navController.navigate(R.id.action_homeFragment_to_syncFragment, bundle);
        } else if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.libraryFragment) {
            Bundle bundle = SyncUtil.getSyncBundle(false, false, true, false, false, true);
            navController.navigate(R.id.action_libraryFragment_to_syncFragment, bundle);
        }
    }

    public void goToHome() {
        bottomNavigationView.setVisibility(View.VISIBLE);

        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.landingFragment) {
            navController.navigate(R.id.action_landingFragment_to_homeFragment);
        } else if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.syncFragment) {
            navController.navigate(R.id.action_syncFragment_to_homeFragment);
        } else if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.loginFragment) {
            navController.navigate(R.id.action_loginFragment_to_homeFragment);
        }
    }

    public void goFromLogin() {
        if (PreferenceUtil.getInstance(getApplicationContext()).getSync()) {
            goToHome();
        } else {
            goToSync();
        }
    }

    private void connectivityStatusReceiverManager(boolean isActive) {
        if(isActive) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(connectivityStatusBroadcastReceiver, filter);
        }
        else {
            unregisterReceiver(connectivityStatusBroadcastReceiver);
        }
    }
}