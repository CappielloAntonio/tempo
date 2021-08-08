package com.cappielloantonio.play.ui.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.DialogServerSignupBinding;
import com.cappielloantonio.play.interfaces.SystemCallback;
import com.cappielloantonio.play.model.Server;
import com.cappielloantonio.play.repository.SystemRepository;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.viewmodel.LoginViewModel;

import java.util.Objects;
import java.util.UUID;

public class ServerSignupDialog extends DialogFragment {
    private static final String TAG = "ServerSignupDialog";

    private DialogServerSignupBinding bind;
    private MainActivity activity;
    private Context context;
    private LoginViewModel loginViewModel;

    private SystemRepository systemRepository;

    private String serverName;
    private String username;
    private String password;
    private String server;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        context = requireContext();

        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        systemRepository = new SystemRepository(App.getInstance());

        bind = DialogServerSignupBinding.inflate(LayoutInflater.from(requireContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(bind.getRoot())
                .setTitle("Add server")
                .setPositiveButton("Enter", (dialog, id) -> { })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent, null));
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent, null));

        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (validateInput()) {
                saveServerPreference(server, username, password, null, null);
                authenticate();
                ((AlertDialog) Objects.requireNonNull(getDialog())).dismiss();
            }
        });
    }

    private boolean validateInput() {
        serverName = bind.serverNameTextView.getText().toString().trim();
        username = bind.usernameTextView.getText().toString().trim();
        password = bind.passwordTextView.getText().toString();
        server = bind.serverTextView.getText().toString().trim();

        if (TextUtils.isEmpty(serverName)) {
            bind.serverNameTextView.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(username)) {
            bind.usernameTextView.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(server)) {
            bind.serverTextView.setError("Required");
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
        if (user != null) PreferenceUtil.getInstance(context).setUser(user);
        if (server != null) PreferenceUtil.getInstance(context).setServer(server);
        if (password != null) PreferenceUtil.getInstance(context).setPassword(password);

        if (token != null && salt != null) {
            String serverID = UUID.randomUUID().toString();

            PreferenceUtil.getInstance(context).setPassword(null);
            PreferenceUtil.getInstance(context).setToken(token);
            PreferenceUtil.getInstance(context).setSalt(salt);
            PreferenceUtil.getInstance(context).setServerId(serverID);

            loginViewModel.addServer(new Server(serverID, this.serverName, this.username, this.server, token, salt, System.currentTimeMillis()));

            return;
        }

        App.getSubsonicClientInstance(requireContext(), true);
    }
}
