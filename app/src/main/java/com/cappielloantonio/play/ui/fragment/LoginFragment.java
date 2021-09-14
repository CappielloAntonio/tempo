package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.ServerAdapter;
import com.cappielloantonio.play.databinding.FragmentLoginBinding;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.dialog.ServerSignupDialog;
import com.cappielloantonio.play.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private FragmentLoginBinding bind;
    private MainActivity activity;
    private LoginViewModel loginViewModel;

    private ServerAdapter serverAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.login_page_menu, menu);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        bind = FragmentLoginBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        initAppBar();
        initServerListView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);

        bind.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ((bind.serverInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                bind.toolbar.setTitle(R.string.login_title);
            } else {
                bind.toolbar.setTitle(R.string.empty_string);
            }
        });
    }

    private void initServerListView() {
        bind.serverListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.serverListRecyclerView.setHasFixedSize(true);

        serverAdapter = new ServerAdapter(activity, requireContext());
        bind.serverListRecyclerView.setAdapter(serverAdapter);
        loginViewModel.getServerList().observe(requireActivity(), servers -> {
            if (servers.size() > 0) {
                if (bind != null) bind.noServerAddedTextView.setVisibility(View.GONE);
                if (bind != null) bind.serverListRecyclerView.setVisibility(View.VISIBLE);
                serverAdapter.setItems(servers);
            } else {
                if (bind != null) bind.noServerAddedTextView.setVisibility(View.VISIBLE);
                if (bind != null) bind.serverListRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            ServerSignupDialog dialog = new ServerSignupDialog();
            dialog.show(activity.getSupportFragmentManager(), null);
            return true;
        }

        return false;
    }
}
