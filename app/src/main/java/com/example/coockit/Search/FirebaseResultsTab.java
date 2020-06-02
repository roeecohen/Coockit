package com.example.coockit.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coockit.Classes.Recipe;
import com.example.coockit.R;
import com.example.coockit.Utils.FirebaseUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class FirebaseResultsTab extends Fragment {
    private DatabaseReference databaseRef;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Recipe, FirebaseResultsTab.ViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_firebase_results_tab, container, false);

        recyclerView = view.findViewById(R.id.firebase_results_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseRef = FirebaseDatabase.getInstance().getReference("Recipes");
        databaseRef.keepSynced(true);
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(databaseRef, Recipe.class).build();

        adapter = new FirebaseRecyclerAdapter<Recipe, FirebaseResultsTab.ViewHolder>(options) {

            @NonNull
            @Override
            public FirebaseResultsTab.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.firebase_search_result_recipe_item,parent,false);
                return new FirebaseResultsTab.ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FirebaseResultsTab.ViewHolder holder, final int position, @NonNull final Recipe model) {

                if(matchIngredients(model.getIngredients()))
                {
                    displayElements(holder.rootConLayout,holder.imgView,holder.cardView,holder.recipeName,holder.linear,holder.leftBtnSpace,holder.rightBtnSpace);
                    holder.recipeName.setText(model.getName());
                    Picasso.get().load(model.getPicUrl()).into(holder.imgView);
                    FirebaseUtils.clickImgOpenRecipe(holder.imgView,getActivity(),getActivity().getApplicationContext(),model.getId());
                }
            }
        };
        recyclerView.setAdapter(adapter);
        return view;
    }

    private boolean matchIngredients(String ingredients) {

        ArrayList<String> arrayInput = new ArrayList<>(Arrays.asList(SearchFragment.getmSearchInput().toLowerCase().trim().split(",")));
        int matchCount=0;

        for (int i =0; i<arrayInput.size();i++)
            if (ingredients.toLowerCase().indexOf(arrayInput.get(i)) != -1)
                matchCount++;

        if(matchCount>=arrayInput.size()-1 &&matchCount>0)
            return true;

        return false;
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

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeName;
        private ImageView imgView;
        private Button leftBtnSpace;
        private Button rightBtnSpace;
        private CardView cardView;
        private ConstraintLayout rootConLayout;
        private ConstraintLayout conLayout;
        private LinearLayout linear;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = (ImageView)itemView.findViewById(R.id.fire_result_rec_img);
            recipeName = (TextView)itemView.findViewById(R.id.fire_result_rec_name);
            cardView = (CardView) itemView.findViewById(R.id.fire_result_cardView);
            rootConLayout = (ConstraintLayout)itemView.findViewById(R.id.fire_result_constraint_root);
            linear = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            leftBtnSpace = (Button) itemView.findViewById(R.id.button2);
            rightBtnSpace = (Button) itemView.findViewById(R.id.button3);

            hideElements(rootConLayout,imgView,recipeName,cardView,linear,leftBtnSpace,rightBtnSpace);
        }
    }

    private void hideElements(ConstraintLayout rootConLayout, ImageView imgView, TextView recipeName, CardView cardView, LinearLayout linear, Button leftBtnSpace, Button rightBtnSpace) {
        rootConLayout.setVisibility(View.GONE);
        imgView.setVisibility(View.GONE);
        recipeName.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        linear.setVisibility(View.GONE);
        leftBtnSpace.setVisibility(View.GONE);
        rightBtnSpace.setVisibility(View.GONE);
    }

    private void displayElements(ConstraintLayout rootConLayout, ImageView imgView, CardView cardView, TextView recipeName, LinearLayout linear, Button leftBtnSpace, Button rightBtnSpace)
    {
        rootConLayout.setVisibility(View.VISIBLE);
        imgView.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);
        recipeName.setVisibility(View.VISIBLE);
        linear.setVisibility(View.VISIBLE);
        leftBtnSpace.setVisibility(View.VISIBLE);
        rightBtnSpace.setVisibility(View.VISIBLE);
    }


}
