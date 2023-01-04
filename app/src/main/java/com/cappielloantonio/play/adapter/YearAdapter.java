package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Media;

import java.util.Collections;
import java.util.List;

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.ViewHolder> {
    private final Context context;
    private final ClickCallback click;

    private List<Integer> years;

    public YearAdapter(Context context, ClickCallback click) {
        this.context = context;
        this.click = click;
        this.years = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_year, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int year = years.get(position);

        holder.textYear.setText(Integer.toString(year));
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
        TextView textYear;

        ViewHolder(View itemView) {
            super(itemView);

            textYear = itemView.findViewById(R.id.year_label);

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
