package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.databinding.FragmentLoginBinding;
import com.cappielloantonio.play.interfaces.SystemCallback;
import com.cappielloantonio.play.repository.SystemRepository;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private FragmentLoginBinding bind;
    private MainActivity activity;

    private String username;
    private String password;
    private String server;

    private SystemRepository systemRepository;

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
        systemRepository = new SystemRepository(App.getInstance());

        bind.loginButton.setOnClickListener(v -> {
            if (validateInput()) {
                saveServerPreference(server, username, password, null, null);
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
            Toast.makeText(requireContext(), "Empty server url", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void authenticate() {
        systemRepository.checkUserCredential(new SystemCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, exception.getMessage());
                Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(String token, String salt) {
                saveServerPreference(null, null, null, token, salt);
                enter();
            }
        });
    }

    private void enter() {
        activity.goFromLogin();
    }

    private void saveServerPreference(String server, String user, String password, String token, String salt) {
        if (user != null) PreferenceUtil.getInstance(requireContext()).setUser(user);
        if (server != null) PreferenceUtil.getInstance(requireContext()).setServer(server);
        if (password != null) PreferenceUtil.getInstance(requireContext()).setPassword(password);

        if (token != null && salt != null) {
            PreferenceUtil.getInstance(requireContext()).setPassword(null);
            PreferenceUtil.getInstance(requireContext()).setToken(token);
            PreferenceUtil.getInstance(requireContext()).setSalt(salt);
        }
    }
}
