package com.example.coockit.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coockit.Classes.Recipe;
import com.example.coockit.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


public class HomeSingleRecipe extends AppCompatActivity {
    private DatabaseReference database;
    private Query query ;

    private TextView recipeName;
    private TextView ingerdients;
    private TextView directions;
    private TextView difficulty;
    private TextView prep;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_single_recipe);

        recipeName = (TextView) findViewById(R.id.rec_name);
        ingerdients =(TextView)findViewById(R.id.rec_ingred);
        ingerdients.setMovementMethod(new ScrollingMovementMethod());
        directions =(TextView)findViewById(R.id.rec_dire);
        directions.setMovementMethod(new ScrollingMovementMethod());
        difficulty =(TextView)findViewById(R.id.rec_diffi);
        prep =(TextView)findViewById(R.id.rec_prep);
        imgView = (ImageView)findViewById(R.id.rec_img);

        database= FirebaseDatabase.getInstance().getReference("Recipes");

        query = database.orderByChild("id").equalTo(getIntent().getStringExtra("rec_id"));
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                recipeName.setText(recipe.getName());
                ingerdients.setText(recipe.getIngredients());
                directions.setText(recipe.getDirections());
                difficulty.setText(recipe.getDifficulty());
                prep.setText(recipe.getPreparationTime());
                Picasso.get().load(recipe.getPicUrl()).into(imgView);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
