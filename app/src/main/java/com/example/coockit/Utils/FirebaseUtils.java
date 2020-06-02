package com.example.coockit.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.coockit.Home.SingleRecipeItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUtils {

    public static String getCurrentUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail ="";
        if (user != null)
            userEmail = user.getEmail();

        return userEmail;
    }

    public static void clickImgOpenRecipe(ImageView imgView, final Activity activity, final Context context, final String id){
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleRecipeItem.class);
                intent.putExtra("rec_id", id);
                activity.startActivity(intent);
            }
        });
    }
}
