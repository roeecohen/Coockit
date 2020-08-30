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
import com.example.coockit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * login page
 * the user needs to insert his user name and password
 */
public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    FirebaseAuth fAuth;
    TextView mRegisterBtn;
    ProgressBar mProgressBar;
    Drawable progressDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.emailLog);
        mPassword = findViewById(R.id.passLog);
        mLoginBtn = findViewById(R.id.loginBtn);
        mRegisterBtn = findViewById(R.id.registerBtn);
        fAuth= FirebaseAuth.getInstance();
        mProgressBar=findViewById(R.id.progressBar);

        progressDrawable = mProgressBar.getIndeterminateDrawable().mutate();
        progressDrawable.setColorFilter(Color.rgb(255,159,51), android.graphics.PorterDuff.Mode.SRC_IN);
        mProgressBar.setProgressDrawable(progressDrawable);

        if(fAuth.getCurrentUser()!=null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mEmail.getText().toString().trim())){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(mPassword.getText().toString().trim())){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(mPassword.getText().toString().trim().length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }


                mProgressBar.setVisibility(View.VISIBLE);


                fAuth.signInWithEmailAndPassword(mEmail.getText().toString().trim(),mPassword.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                                else {
                                    Toast.makeText(Login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    mProgressBar.setVisibility(View.GONE);
                                }

                            }
                        });
            }
        });


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }
}
