package com.cappielloantonio.tempo.ui.fragment.bottomsheetdialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.subsonic.models.Share;
import com.cappielloantonio.tempo.ui.dialog.ShareUpdateDialog;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.UIUtil;
import com.cappielloantonio.tempo.viewmodel.HomeViewModel;
import com.cappielloantonio.tempo.viewmodel.ShareBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

@UnstableApi
public class ShareBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private ShareBottomSheetViewModel shareBottomSheetViewModel;
    private Share share;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_share_dialog, container, false);

        share = this.requireArguments().getParcelable(Constants.SHARE_OBJECT);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        shareBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(ShareBottomSheetViewModel.class);
        shareBottomSheetViewModel.setShare(share);

        init(view);

        return view;
    }

    private void init(View view) {
        ImageView shareCover = view.findViewById(R.id.share_cover_image_view);

        CustomGlideRequest.Builder
                .from(requireContext(), shareBottomSheetViewModel.getShare().getEntries().get(0).getCoverArtId(), CustomGlideRequest.ResourceType.Unknown)
                .build()
                .into(shareCover);

        TextView shareTitle = view.findViewById(R.id.share_title_text_view);
        shareTitle.setText(shareBottomSheetViewModel.getShare().getDescription());
        shareTitle.setSelected(true);

        TextView shareSubtitle = view.findViewById(R.id.share_subtitle_text_view);
        shareSubtitle.setText(requireContext().getString(R.string.share_subtitle_item, UIUtil.getReadableDate(share.getExpires())));
        shareSubtitle.setSelected(true);

        TextView copyLink = view.findViewById(R.id.copy_link_text_view);
        copyLink.setOnClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(getString(R.string.app_name), shareBottomSheetViewModel.getShare().getUrl());
            clipboardManager.setPrimaryClip(clipData);
            dismissBottomSheet();
        });

        TextView updateShare = view.findViewById(R.id.update_share_preferences_text_view);
        updateShare.setOnClickListener(v -> {
            // refreshShares();
            showUpdateShareDialog();
            dismissBottomSheet();
        });

        TextView deleteShare = view.findViewById(R.id.delete_share_text_view);
        deleteShare.setOnClickListener(v -> {
            deleteShare();
            refreshShares();
            dismissBottomSheet();
        });
    }

    @Override
    public void onClick(View v) {
        dismissBottomSheet();
    }

    private void dismissBottomSheet() {
        dismiss();
    }

    private void showUpdateShareDialog() {
        ShareUpdateDialog dialog = new ShareUpdateDialog();
        dialog.show(requireActivity().getSupportFragmentManager(), null);
    }

    private void refreshShares() {
        homeViewModel.refreshShares(getParentFragment());
    }

    private void deleteShare() {
        shareBottomSheetViewModel.deleteShare();
    }
}