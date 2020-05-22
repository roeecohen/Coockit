package com.example.coockit.LoginRegister;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.coockit.Main.MainActivity;
import com.example.coockit.Classes.Member;
import com.example.coockit.Profile.ProfileRecipes;
import com.example.coockit.Profile.UploadRecipe;
import com.example.coockit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText mFullName, mEmail, mPhone,mPassword;
    private Button mRegisterBtn;
    private TextView mLoginBtn;
    private FirebaseAuth fAuth;
    private ProgressBar mProgressBar;
    private Drawable progressDrawable;
    private Member member;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fAuth=FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser()!=null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        databaseRef = FirebaseDatabase.getInstance().getReference("Members");

        mFullName = findViewById(R.id.fullname);
        mEmail = findViewById(R.id.email);
        mPhone = findViewById(R.id.phone);
        mPassword = findViewById(R.id.pass);
        mLoginBtn = findViewById(R.id.loginBtn);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mProgressBar=findViewById(R.id.progressBar2);

        progressDrawable = mProgressBar.getIndeterminateDrawable().mutate();
        progressDrawable.setColorFilter(Color.rgb(255,159,51), android.graphics.PorterDuff.Mode.SRC_IN);
        mProgressBar.setProgressDrawable(progressDrawable);
        //reff = FirebaseDatabase.getInstance().getReference().child("member");


        mRegisterBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                member = new Member(mFullName.getText().toString().trim(),mEmail.getText().toString().trim(),mPhone.getText().toString().trim(),mPassword.getText().toString().trim(),"");

                if(TextUtils.isEmpty(member.getEmail())){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(member.getPass())){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(member.getPass().length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(member.getEmail(), member.getPass()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else {
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                        }

                    }
                });
                databaseRef.child(member.getId()).setValue(member).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) { }
                });

            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }

}
