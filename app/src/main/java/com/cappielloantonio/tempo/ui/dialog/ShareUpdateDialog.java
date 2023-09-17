package com.cappielloantonio.tempo.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogShareUpdateBinding;
import com.cappielloantonio.tempo.util.UIUtil;
import com.cappielloantonio.tempo.viewmodel.HomeViewModel;
import com.cappielloantonio.tempo.viewmodel.ShareBottomSheetViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ShareUpdateDialog extends DialogFragment {
    private static final String TAG = "ShareUpdateDialog";

    private DialogShareUpdateBinding bind;
    private HomeViewModel homeViewModel;
    private ShareBottomSheetViewModel shareBottomSheetViewModel;

    private MaterialDatePicker<Long> datePicker;

    private String descriptionTextView;
    private String expirationTextView;
    private long expiration;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        shareBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(ShareBottomSheetViewModel.class);

        bind = DialogShareUpdateBinding.inflate(getLayoutInflater());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(bind.getRoot())
                .setTitle(R.string.share_update_dialog_title)
                .setPositiveButton(R.string.share_update_dialog_positive_button, (dialog, id) -> {
                })
                .setNegativeButton(R.string.share_update_dialog_negative_button, (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        setShareInfo();
        setShareCalendar();
        setButtonAction();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void setShareInfo() {
        if (shareBottomSheetViewModel.getShare() != null) {
            bind.shareDescriptionTextView.setText(shareBottomSheetViewModel.getShare().getDescription());
            // bind.shareExpirationTextView.setText(shareBottomSheetViewModel.getShare().getExpires());
        }
    }

    private void setShareCalendar() {
        expiration = shareBottomSheetViewModel.getShare().getExpires().getTime();

        bind.shareExpirationTextView.setText(UIUtil.getReadableDate(new Date(expiration)));

        bind.shareExpirationTextView.setFocusable(false);
        bind.shareExpirationTextView.setOnLongClickListener(null);

        bind.shareExpirationTextView.setOnClickListener(view -> {
            CalendarConstraints constraints = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build();

            datePicker = MaterialDatePicker.Builder.datePicker()
                    .setCalendarConstraints(constraints)
                    .setSelection(expiration)
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                expiration = selection;
                bind.shareExpirationTextView.setText(UIUtil.getReadableDate(new Date(selection)));
            });

            datePicker.show(requireActivity().getSupportFragmentManager(), null);
        });
    }

    private void setButtonAction() {
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (validateInput()) {
                updateShare();
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    private boolean validateInput() {
        descriptionTextView = Objects.requireNonNull(bind.shareDescriptionTextView.getText()).toString().trim();
        expirationTextView = Objects.requireNonNull(bind.shareExpirationTextView.getText()).toString().trim();

        if (TextUtils.isEmpty(descriptionTextView)) {
            bind.shareDescriptionTextView.setError(getString(R.string.error_required));
            return false;
        }

        if (TextUtils.isEmpty(expirationTextView)) {
            bind.shareExpirationTextView.setError(getString(R.string.error_required));
            return false;
        }

        return true;
    }

    private void updateShare() {
        shareBottomSheetViewModel.updateShare(descriptionTextView, expiration);
        homeViewModel.refreshShares(requireActivity());
    }
}
