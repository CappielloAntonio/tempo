package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogServerSignupBinding;
import com.cappielloantonio.tempo.interfaces.SystemCallback;
import com.cappielloantonio.tempo.model.Server;
import com.cappielloantonio.tempo.repository.SystemRepository;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.util.Preferences;
import com.cappielloantonio.tempo.viewmodel.LoginViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;
import java.util.UUID;

@UnstableApi
public class ServerSignupDialog extends DialogFragment {
    private static final String TAG = "ServerSignupDialog";

    private DialogServerSignupBinding bind;
    private LoginViewModel loginViewModel;

    private MainActivity activity;

    private String serverName = "darklyn";
    private String username;
    private String password;
    private String server = "https://navidrome.darklyn.online";
    private boolean lowSecurity = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogServerSignupBinding.inflate(getLayoutInflater());

        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        return new MaterialAlertDialogBuilder(getActivity())
                .setView(bind.getRoot())
                .setTitle(R.string.server_signup_dialog_title)
                .setNeutralButton(R.string.server_signup_dialog_neutral_button, (dialog, id) -> { })
                .setPositiveButton(R.string.server_signup_dialog_positive_button, (dialog, id) -> { })
                .setNegativeButton(R.string.server_signup_dialog_negative_button, (dialog, id) -> dialog.cancel())
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();

        setServerInfo();
        setButtonAction();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void setServerInfo() {
        if (getArguments() != null) {
            loginViewModel.setServerToEdit(requireArguments().getParcelable("server_object"));

            if (loginViewModel.getServerToEdit() != null) {
                bind.usernameTextView.setText(loginViewModel.getServerToEdit().getUsername());
                bind.passwordTextView.setText("");
            }
        } else {
            loginViewModel.setServerToEdit(null);
        }
    }

    private void setButtonAction() {
        androidx.appcompat.app.AlertDialog alertDialog = (androidx.appcompat.app.AlertDialog) Objects.requireNonNull(getDialog());

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (validateInput()) {
                saveServerPreference();
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            loginViewModel.deleteServer(null);
            Objects.requireNonNull(getDialog()).dismiss();
        });
    }

    private boolean validateInput() {
        username = Objects.requireNonNull(bind.usernameTextView.getText()).toString().trim();
        password = Objects.requireNonNull(bind.passwordTextView.getText()).toString();

        if (TextUtils.isEmpty(username)) {
            bind.usernameTextView.setError(getString(R.string.error_required));
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            bind.passwordTextView.setError(getString(R.string.error_required));
            return false;
        }

        return true;
    }

    private void saveServerPreference() {
        String serverID = loginViewModel.getServerToEdit() != null ? loginViewModel.getServerToEdit().getServerId() : UUID.randomUUID().toString();
        Server srv = new Server(serverID, this.serverName, this.username, this.password, this.server, System.currentTimeMillis(), this.lowSecurity);
        loginViewModel.addServer(srv);

        SystemRepository systemRepository = new SystemRepository();
        systemRepository.checkUserCredential(new SystemCallback() {
            @Override
            public void onError(Exception exception) {
                Preferences.setServer(null);
                Preferences.setPassword(null);
                loginViewModel.deleteServer(srv);

                App.getSubsonicClientInstance(true);
                Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String password, String token, String salt) {
                Preferences.setServer(server);
                Preferences.setPassword(password);

                App.getSubsonicClientInstance(true);
            }
        });
    }
}
