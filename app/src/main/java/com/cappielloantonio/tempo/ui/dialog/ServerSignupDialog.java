package com.cappielloantonio.tempo.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogServerSignupBinding;
import com.cappielloantonio.tempo.model.Server;
import com.cappielloantonio.tempo.ui.adapter.CustomHeadersAdapter;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.viewmodel.LoginViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;
import java.util.Map;
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
    private boolean allowCustomHeaders = false;

    private Map<String, String> customHeaders = new HashMap<>();

    CustomHeadersAdapter adapter = new CustomHeadersAdapter(customHeaders, this::deleteCustomHeaders);

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogServerSignupBinding.inflate(getLayoutInflater());
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        bind.customHeadersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bind.customHeadersRecyclerView.setAdapter(adapter);

        return new AlertDialog.Builder(getActivity(), R.style.FullScreenDialog)
                .setView(bind.getRoot())
                .setTitle(R.string.server_signup_dialog_title)
                .setNeutralButton(R.string.server_signup_dialog_neutral_button, (dialog, id) -> {
                })
                .setPositiveButton(R.string.server_signup_dialog_positive_button, (dialog, id) -> {
                })
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
                bind.allowCustomHeadersCheckbox.setChecked(!Objects.requireNonNull(loginViewModel.getServerToEdit().getCustomHeaders()).isEmpty());

                customHeaders = loginViewModel.getServerToEdit().getCustomHeaders();
                adapter.updateData(customHeaders);

                if (customHeaders != null && !customHeaders.isEmpty()) {
                    bind.customHeadersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    bind.customHeadersRecyclerView.setVisibility(View.VISIBLE);
                    bind.customHeaderAddButton.setVisibility(View.VISIBLE);
                }
            }
        } else {
            loginViewModel.setServerToEdit(null);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setButtonAction() {
        androidx.appcompat.app.AlertDialog alertDialog = (androidx.appcompat.app.AlertDialog) Objects.requireNonNull(getDialog());

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (validateInput()) {
                saveServerPreference();
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> Toast.makeText(requireContext(), R.string.server_signup_dialog_action_delete_toast, Toast.LENGTH_SHORT).show());

        Button customActionButton = bind.customHeaderAddButton;
        customActionButton.setOnClickListener(v -> {
            showCustomHeaderInputDialog();
        });

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL).setOnLongClickListener(v -> {
            loginViewModel.deleteServer(null);
            Objects.requireNonNull(getDialog()).dismiss();
            return true;
        });

        bind.allowCustomHeadersCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                bind.customHeadersRecyclerView.setVisibility(View.VISIBLE);
                bind.customHeaderAddButton.setVisibility(View.VISIBLE);
            } else {
                bind.customHeadersRecyclerView.setVisibility(View.GONE);
                bind.customHeaderAddButton.setVisibility(View.GONE);
            }
        });
    }

    private boolean validateInput() {
        serverName = Objects.requireNonNull(bind.serverNameTextView.getText()).toString().trim();
        username = Objects.requireNonNull(bind.usernameTextView.getText()).toString().trim();
        password = bind.lowSecurityCheckbox.isChecked() ? MusicUtil.passwordHexEncoding(Objects.requireNonNull(bind.passwordTextView.getText()).toString()) : Objects.requireNonNull(bind.passwordTextView.getText()).toString();
        server = bind.serverTextView.getText() != null && !bind.serverTextView.getText().toString().trim().isBlank() ? bind.serverTextView.getText().toString().trim() : null;
        localAddress = bind.localAddressTextView.getText() != null && !bind.localAddressTextView.getText().toString().trim().isBlank() ? bind.localAddressTextView.getText().toString().trim() : null;
        lowSecurity = bind.lowSecurityCheckbox.isChecked();
        allowCustomHeaders = bind.allowCustomHeadersCheckbox.isChecked();

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

    private void showCustomHeaderInputDialog() {
        // Create a dialog for user input
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_custom_header_input, null);
        EditText keyInput = dialogView.findViewById(R.id.server_header_key_view);
        EditText valueInput = dialogView.findViewById(R.id.server_header_value_view);

        new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String headerKey = keyInput.getText().toString().trim();
                    String headerValue = valueInput.getText().toString().trim();

                    // Validate inputs
                    if (!headerKey.isEmpty() && !headerValue.isEmpty()) {
                        customHeaders.put(headerKey, headerValue);
                        adapter.updateData(customHeaders);
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.server_signup_dialog_hint_custom_values_required), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).create().show();
    }

    private void deleteCustomHeaders(String key) {
        customHeaders.remove(key);
        adapter.updateData(customHeaders);
        Toast.makeText(getContext(), getString(R.string.server_signup_dialog_custom_header_delete_success), Toast.LENGTH_SHORT).show();
    }

    private void saveServerPreference() {
        String serverID = loginViewModel.getServerToEdit() != null ? loginViewModel.getServerToEdit().getServerId() : UUID.randomUUID().toString();
        loginViewModel.addServer(new Server(
                serverID,
                this.serverName,
                this.username,
                this.password,
                this.server,
                this.localAddress,
                System.currentTimeMillis(),
                this.lowSecurity,
                allowCustomHeaders ? customHeaders : new HashMap<>()
        ));
    }
}
