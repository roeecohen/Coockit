package com.example.coockit.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coockit.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder {
    private TextView recipeName;
    private TextView ingerdients;
    private TextView directions;
    private TextView difficulty;
    private TextView prep;
    private ImageView imgView;

    public TextView getRecipeName() {
        return recipeName;
    }

    public TextView getIngerdients() {
        return ingerdients;
    }

    public TextView getDirections() {
        return directions;
    }

    public TextView getDifficulty() {
        return difficulty;
    }

    public TextView getPrep() {
        return prep;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = (TextView)itemView.findViewById(R.id.rec_name);
        ingerdients = (TextView)itemView.findViewById(R.id.rec_ingred);
        directions = (TextView)itemView.findViewById(R.id.rec_dire);
        difficulty = (TextView)itemView.findViewById(R.id.rec_diffi);
        prep = (TextView)itemView.findViewById(R.id.rec_prep);
        imgView = (ImageView)itemView.findViewById(R.id.rec_img);
    }
}
