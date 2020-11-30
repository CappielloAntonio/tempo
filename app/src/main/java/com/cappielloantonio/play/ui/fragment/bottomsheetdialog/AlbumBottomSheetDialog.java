package com.cappielloantonio.play.ui.fragment.bottomsheetdialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.model.Album;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AlbumBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "AlbumBottomSheetDialog";

    private Album album;

    public AlbumBottomSheetDialog(Album album) {
        this.album = album;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_album_dialog, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);

        button1.setOnClickListener(v -> {
            Toast.makeText(requireContext(), album.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        button2.setOnClickListener(v -> {
            Toast.makeText(requireContext(), album.getArtistName(), Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}