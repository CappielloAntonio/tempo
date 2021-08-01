package com.cappielloantonio.play.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class GenreCatalogueAdapter extends RecyclerView.Adapter<GenreCatalogueAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "GenreCatalogueAdapter";

    private List<Genre> genres;
    private List<Genre> genresFull;
    private LayoutInflater mInflater;
    private MainActivity activity;
    private Context context;
    private ItemClickListener itemClickListener;
    private Filter filtering = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Genre> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(genresFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Genre item : genresFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            genres.clear();
            genres.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public GenreCatalogueAdapter(MainActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.genres = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_library_catalogue_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Genre genre = genres.get(position);

        holder.textGenre.setText(MusicUtil.getReadableInfo(genre.getName()));
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public Genre getItem(int position) {
        return genres.get(position);
    }

    public void setItems(List<Genre> genres) {
        this.genres = genres;
        this.genresFull = new ArrayList<>(genres);
        notifyDataSetChanged();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return filtering;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textGenre;

        ViewHolder(View itemView) {
            super(itemView);

            textGenre = itemView.findViewById(R.id.genre_label);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getBindingAdapterPosition());

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
