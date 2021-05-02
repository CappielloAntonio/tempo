package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.databinding.FragmentLoginBinding;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;

import org.jellyfin.apiclient.interaction.Response;
import org.jellyfin.apiclient.model.system.SystemInfo;
import org.jellyfin.apiclient.model.users.AuthenticationResult;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private FragmentLoginBinding bind;
    private MainActivity activity;

    private String username;
    private String password;
    private String server;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentLoginBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        init();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        bind.loginButton.setOnClickListener(v -> {
            if (validateInput()) {
                saveServerPreference(username, server);
                authenticate();
            }
        });
    }

    private boolean validateInput() {
        username = bind.usernameTextView.getText().toString().trim();
        password = bind.passwordTextView.getText().toString();
        server = bind.serverTextView.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(requireContext(), "Empty username", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(server)) {
            Toast.makeText(requireContext(), "Empty server", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveServerPreference(String user, String server) {
        PreferenceUtil.getInstance(requireContext()).setUser(user);
        PreferenceUtil.getInstance(requireContext()).setServer(server);
    }

    private void authenticate() {
        App.getApiClientInstance(requireContext()).ChangeServerLocation(server);
        App.getApiClientInstance(requireContext()).AuthenticateUserAsync(username, password, new Response<AuthenticationResult>() {
            @Override
            public void onResponse(AuthenticationResult result) {
                if (result.getAccessToken() == null) return;
                enter(result.getUser().getId(), result.getAccessToken());
            }

            @Override
            public void onError(Exception exception) {
                if (exception.getMessage().contains("AuthFailureError")) {
                    Toast.makeText(requireContext(), "Fail to authenticate", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Server unreachable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void enter(String user, String token) {
        App.getApiClientInstance(requireContext()).GetSystemInfoAsync(new Response<SystemInfo>() {
            @Override
            public void onResponse(SystemInfo result) {
                if (result.getVersion().charAt(0) == '1') {
                    PreferenceUtil.getInstance(requireContext()).setUser(user);
                    PreferenceUtil.getInstance(requireContext()).setToken(token);

                    activity.goFromLogin();
                } else {
                    Toast.makeText(requireContext(), "Error version", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
