package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.model.RecentSearch;

import java.util.List;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {
    private static final String TAG = "RecentSearchAdapter";

    private List<RecentSearch> searches;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener itemClickListener;

    public RecentSearchAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_search_recent_searches, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecentSearch search = searches.get(position);

        holder.recentSearch.setText(search.getSearch());
    }

    @Override
    public int getItemCount() {
        return searches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recentSearch;

        ViewHolder(View itemView) {
            super(itemView);

            recentSearch = itemView.findViewById(R.id.recent_search_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setItems(List<RecentSearch> searches) {
        this.searches = searches;
        notifyDataSetChanged();
    }

    public RecentSearch getItem(int id) {
        return searches.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
