package com.example.coockit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ProfileRecipes extends AppCompatActivity {
    private DatabaseReference databaseRef;
    private Query query ;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Recipe,RecipesViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_recipes);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseRef = FirebaseDatabase.getInstance().getReference("Recipes");
        databaseRef.keepSynced(true);
        query = databaseRef.orderByChild("user").equalTo(FirebaseUtils.getCurrentUserEmail());
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class).build();

        adapter = new FirebaseRecyclerAdapter<Recipe,RecipesViewHolder>(options) {
            @NonNull
            @Override
            public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item,parent,false);
                return new RecipesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RecipesViewHolder holder, int position, @NonNull Recipe model) {
                holder.recipeName.setText(model.getName());
                holder.ingerdients.setText(model.getIngredients());
                holder.directions.setText(model.getDirections());
                holder.difficulty.setText(model.getDifficulty());
                holder.prep.setText(model.getPreparationTime());
                Picasso.get().load(model.getPicUrl()).into(holder.imgView);

            }
        };
        recyclerView.setAdapter(adapter);
    }

    private class RecipesViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeName;
        private TextView ingerdients;
        private TextView directions;
        private TextView difficulty;
        private TextView prep;
        private ImageView imgView;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeName = (TextView)itemView.findViewById(R.id.rec_name);
            ingerdients = (TextView)itemView.findViewById(R.id.rec_ingred);
            directions = (TextView)itemView.findViewById(R.id.rec_dire);
            difficulty = (TextView)itemView.findViewById(R.id.rec_diffi);
            prep = (TextView)itemView.findViewById(R.id.rec_prep);
            imgView = (ImageView)itemView.findViewById(R.id.rec_img);
        }
    }

   @Override
   protected  void onStop(){
        super.onStop();
        adapter.stopListening();
   }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

}