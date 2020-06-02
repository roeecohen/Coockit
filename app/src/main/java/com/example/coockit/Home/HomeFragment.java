package com.example.coockit.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coockit.Classes.Member;
import com.example.coockit.Classes.Recipe;
import com.example.coockit.R;
import com.example.coockit.Utils.FirebaseUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

//        Toolbar toolbar =view.findViewById(R.id.orange_top_bar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("CoockIt");
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
                return new HomeFragment.HomeRecipesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull HomeRecipesViewHolder holder, final int position, @NonNull final Recipe model) {
                holder.recipeName.setText(model.getName());
                Picasso.get().load(model.getPicUrl()).into(holder.imgView);
                setMemberInfo(model.getUser(),holder.profileImg,holder.memberName);

                FirebaseUtils.clickImgOpenRecipe(holder.imgView,getActivity(),getActivity().getApplicationContext(),model.getId());
            }
        };
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void setMemberInfo(String email,final ImageView profileImg, final TextView memberName) {
        DatabaseReference databaseMemberRef = FirebaseDatabase.getInstance().getReference("Members");;
        Query query = databaseMemberRef.orderByChild("email").equalTo(email);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Member member = (Member)dataSnapshot.getValue(Member.class);
                if(member!=null)
                {
                    memberName.setText(member.getFullName());
                    Picasso.get().load(member.getImg()).into(profileImg);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private class HomeRecipesViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeName;
        private TextView memberName;
        private ImageView imgView;
        private ImageView profileImg;

        public HomeRecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = (TextView)itemView.findViewById(R.id.home_rec_name);
            memberName = (TextView)itemView.findViewById(R.id.member_name);
            imgView = (ImageView)itemView.findViewById(R.id.home_rec_img);
            profileImg = (ImageView)itemView.findViewById(R.id.profile_img2);
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
