package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;

import java.util.List;

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.ViewHolder> {
    private static final String TAG = "YearAdapter";
    private List<Integer> years;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener itemClickListener;

    public YearAdapter(Context context, List<Integer> years) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.years = years;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home_year, parent, false);
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

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textYear;

        ViewHolder(View itemView) {
            super(itemView);

            textYear = itemView.findViewById(R.id.year_label);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(view, getBindingAdapterPosition());
        }
    }
}
