package com.cappielloantonio.play.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.databinding.ItemHomeYearBinding;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Media;

import java.util.Collections;
import java.util.List;

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.ViewHolder> {
    private final ClickCallback click;

    private List<Integer> years;

    public YearAdapter(ClickCallback click) {
        this.click = click;
        this.years = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeYearBinding view = ItemHomeYearBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int year = years.get(position);

        holder.item.yearLabel.setText(Integer.toString(year));
    }

    @Override
    public int getItemCount() {
        return years.size();
    }

    public Integer getItem(int position) {
        return years.get(position);
    }

    public void setItems(List<Integer> years) {
        this.years = years;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHomeYearBinding item;

        ViewHolder(ItemHomeYearBinding item) {
            super(item.getRoot());

            this.item = item;

            itemView.setOnClickListener(v -> onClick());
        }

        public void onClick() {
            Bundle bundle = new Bundle();
            bundle.putString(Media.BY_YEAR, Media.BY_YEAR);
            bundle.putInt("year_object", years.get(getBindingAdapterPosition()));

            click.onYearClick(bundle);
        }
    }
}
