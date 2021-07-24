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
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

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
                saveServerPreference(server, username, password);
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

    private void saveServerPreference(String server, String user, String password) {
        PreferenceUtil.getInstance(requireContext()).setUser(user);
        PreferenceUtil.getInstance(requireContext()).setServer(server);
        PreferenceUtil.getInstance(requireContext()).setPassword(password);
    }

    private void authenticate() {
        App.getSubsonicClientInstance(requireContext())
                .getSystemClient()
                .ping()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, retrofit2.Response<SubsonicResponse> response) {
                        if (!response.isSuccessful()) {
                            Log.d(TAG, "+++ onResponse() unsuccesful");
                            Log.d(TAG, "+++ " + response.message());
                            Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        Log.d(TAG, "+++ onResponse() succesful");
                        Toast.makeText(requireContext(), "Login succesful", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        Log.d(TAG, "+++ onFailure()");
                        Log.d(TAG, "+++ " + t.getMessage());
                        Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
