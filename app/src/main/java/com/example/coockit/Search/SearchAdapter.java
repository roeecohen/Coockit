package com.example.coockit.Search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.coockit.R;
import com.squareup.picasso.Picasso;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context mContext;
    private Activity mActivity;

    public SearchAdapter(Context context, Activity act) {
        mContext = context;
        mActivity = act;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {
        holder.title.setText((String)Results.getmTitles().get(position));
        holder.url.setText((String)Results.getmUrls().get(position));
        holder.ingredients.setText((String)Results.getmIngredients().get(position));
        Picasso.get().load((String)Results.getmPictures().get(position)).into(holder.img);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse((String)Results.getmClickUrls().get(position)) );
                mActivity.startActivity( browse );
            }
        });
    }

    @Override
    public int getItemCount() {
        return Results.getmTitles().size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView url;
        private TextView ingredients;
        private ImageView img;
        private CardView card;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            url = (TextView) itemView.findViewById(R.id.url);
            ingredients = (TextView) itemView.findViewById(R.id.ingredients);
            img = (ImageView) itemView.findViewById(R.id.img);
            card= (CardView) itemView.findViewById(R.id.search_card);
        }
    }
}



