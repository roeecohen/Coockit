package com.example.coockit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    private DatabaseReference databaseRef;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Recipe, HomeFragment.HomeRecipesViewHolder> adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.home_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseRef = FirebaseDatabase.getInstance().getReference("Recipes");
        databaseRef.keepSynced(true);
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(databaseRef, Recipe.class).build();

        adapter = new FirebaseRecyclerAdapter<Recipe, HomeRecipesViewHolder>(options) {

            @NonNull
            @Override
            public HomeRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recipe_item,parent,false);

                // here we override the inflated view's height to be half the recyclerview size
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                layoutParams.height = (parent.getHeight() / 3) - layoutParams.topMargin - layoutParams.bottomMargin;
                view.setLayoutParams(layoutParams);

                return new HomeFragment.HomeRecipesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull HomeRecipesViewHolder holder, final int position, @NonNull Recipe model) {
                holder.recipeName.setText(model.getName());
                Picasso.get().load(model.getPicUrl()).into(holder.imgView);

                holder.imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Recipe recipe= (Recipe)adapter.getItem(position);

                        Toast.makeText(getActivity(), "Recycle Click" , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), HomeSingleRecipe.class);
                        intent.putExtra("rec_id", recipe.getId());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

        return view;
    }

    private class HomeRecipesViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeName;
        private ImageView imgView;

        public HomeRecipesViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeName = (TextView)itemView.findViewById(R.id.home_rec_name);
            imgView = (ImageView)itemView.findViewById(R.id.home_rec_img);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }
}
