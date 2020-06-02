package com.example.coockit.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coockit.Classes.Recipe;
import com.example.coockit.Utils.FirebaseUtils;
import com.example.coockit.R;
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
    private FirebaseRecyclerAdapter<Recipe, RecipeViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_recipes);

        Toolbar toolbar =findViewById(R.id.orange_top_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Recipes");

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseRef = FirebaseDatabase.getInstance().getReference("Recipes");
        databaseRef.keepSynced(true);
        query = databaseRef.orderByChild("user").equalTo(FirebaseUtils.getCurrentUserEmail());
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class).build();

        adapter = new FirebaseRecyclerAdapter<Recipe,RecipeViewHolder>(options) {
            @NonNull
            @Override
            public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.firebase_search_result_recipe_item,parent,false);
                return new RecipeViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull Recipe model) {
                holder.recipeName.setText(model.getName());
                Picasso.get().load(model.getPicUrl()).into(holder.imgView);
                FirebaseUtils.clickImgOpenRecipe(holder.imgView,ProfileRecipes.this,getApplicationContext(),model.getId());
            }
        };
        recyclerView.setAdapter(adapter);
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

    private class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeName;
        private ImageView imgView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeName = (TextView)itemView.findViewById(R.id.fire_result_rec_name);
            imgView = (ImageView)itemView.findViewById(R.id.fire_result_rec_img);
        }
    }

}