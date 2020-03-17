package com.example.coockit;

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
}
