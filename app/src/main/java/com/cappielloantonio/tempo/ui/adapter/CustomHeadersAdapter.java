package com.cappielloantonio.tempo.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomHeadersAdapter extends RecyclerView.Adapter<CustomHeadersAdapter.ViewHolder> {
    private final List<Map.Entry<String, String>> localCustomHeaders;
    private final OnDeleteListener onDeleteListener;

    public interface OnDeleteListener {
        void onDelete(String key);
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView headerDetailsView;
        private final Button deleteButton;

        public ViewHolder(View view) {
            super(view);
            headerDetailsView = view.findViewById(R.id.custom_header_value_text_view);
            deleteButton = view.findViewById(R.id.delete_icon);
        }

        public TextView getTextView() {
            return headerDetailsView;
        }
    }

    public CustomHeadersAdapter(Map<String, String> data, OnDeleteListener listener) {
        this.localCustomHeaders = new ArrayList<>(data.entrySet());
        this.onDeleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_custom_header_value, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map.Entry<String, String> entry = localCustomHeaders.get(position);
        String key = entry.getKey();
        String value = entry.getValue();

        holder.getTextView().setText(String.format("%s: %s", key, value));

        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteListener != null) {
                this.onDeleteListener.onDelete(key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return localCustomHeaders.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(Map<String, String> newData) {
        localCustomHeaders.clear();
        localCustomHeaders.addAll(newData.entrySet());

        notifyDataSetChanged();
    }
}