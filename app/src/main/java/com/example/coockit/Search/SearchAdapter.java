package com.example.coockit.Search;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.coockit.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context mContext;

    public interface VolleyCallBack {
        void onSuccess();
    }

    public SearchAdapter(Intent intent,Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.title.setText((String)SearchResults.getmTitles().get(position));
        holder.url.setText((String)SearchResults.getmUrls().get(position));
        holder.ingredients.setText((String)SearchResults.getmIngredients().get(position));
        Picasso.get().load((String)SearchResults.getmPictures().get(position)).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return SearchResults.getmTitles().size();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView url;
        private TextView ingredients;
        private ImageView img;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            url = (TextView) itemView.findViewById(R.id.url);
            ingredients = (TextView) itemView.findViewById(R.id.ingredients);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}



