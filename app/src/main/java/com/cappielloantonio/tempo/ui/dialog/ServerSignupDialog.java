package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogServerSignupBinding;
import com.cappielloantonio.tempo.model.Server;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.viewmodel.LoginViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;
import java.util.UUID;

public class ServerSignupDialog extends DialogFragment {
    private static final String TAG = "ServerSignupDialog";

    private DialogServerSignupBinding bind;
    private LoginViewModel loginViewModel;

    private String serverName;
    private String username;
    private String password;
    private String server;
    private String localAddress;
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
                bind.serverNameTextView.setText(loginViewModel.getServerToEdit().getServerName());
                bind.usernameTextView.setText(loginViewModel.getServerToEdit().getUsername());
                bind.passwordTextView.setText("");
                bind.serverTextView.setText(loginViewModel.getServerToEdit().getAddress());
                bind.localAddressTextView.setText(loginViewModel.getServerToEdit().getLocalAddress());
                bind.lowSecurityCheckbox.setChecked(loginViewModel.getServerToEdit().isLowSecurity());
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

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> Toast.makeText(requireContext(), R.string.server_signup_dialog_action_delete_toast, Toast.LENGTH_SHORT).show());

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL).setOnLongClickListener(v -> {
            loginViewModel.deleteServer(null);
            Objects.requireNonNull(getDialog()).dismiss();
            return true;
        });
    }

    private boolean validateInput() {
        serverName = Objects.requireNonNull(bind.serverNameTextView.getText()).toString().trim();
        username = Objects.requireNonNull(bind.usernameTextView.getText()).toString().trim();
        password = bind.lowSecurityCheckbox.isChecked() ? MusicUtil.passwordHexEncoding(Objects.requireNonNull(bind.passwordTextView.getText()).toString()) : Objects.requireNonNull(bind.passwordTextView.getText()).toString();
        server = bind.serverTextView.getText() != null && !bind.serverTextView.getText().toString().trim().isBlank() ? bind.serverTextView.getText().toString().trim() : null;
        localAddress = bind.localAddressTextView.getText() != null && !bind.localAddressTextView.getText().toString().trim().isBlank() ? bind.localAddressTextView.getText().toString().trim() : null;
        lowSecurity = bind.lowSecurityCheckbox.isChecked();

        if (TextUtils.isEmpty(serverName)) {
            bind.serverNameTextView.setError(getString(R.string.error_required));
            return false;
        }

        if (TextUtils.isEmpty(username)) {
            bind.usernameTextView.setError(getString(R.string.error_required));
            return false;
        }

        if (TextUtils.isEmpty(server)) {
            bind.serverTextView.setError(getString(R.string.error_required));
            return false;
        }

        if (!TextUtils.isEmpty(localAddress) && !localAddress.matches("^https?://(.*)")) {
            bind.localAddressTextView.setError(getString(R.string.error_server_prefix));
            return false;
        }

        if (!server.matches("^https?://(.*)")) {
            bind.serverTextView.setError(getString(R.string.error_server_prefix));
            return false;
        }

        return true;
    }

    private void saveServerPreference() {
        String serverID = loginViewModel.getServerToEdit() != null ? loginViewModel.getServerToEdit().getServerId() : UUID.randomUUID().toString();
        loginViewModel.addServer(new Server(serverID, this.serverName, this.username, this.password, this.server, this.localAddress, System.currentTimeMillis(), this.lowSecurity));
    }
}
