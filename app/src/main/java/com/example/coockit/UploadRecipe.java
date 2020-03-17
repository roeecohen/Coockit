package com.example.coockit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class UploadRecipe extends AppCompatActivity {

    private EditText recipeName;
    private EditText ingerdients;
    private EditText directions;
    private EditText difficulty;
    private EditText prep;
    private Button uploadRecipe;
    private ImageView imgView;
    private ProgressBar progressBar;
    private Uri imgUri;

    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        recipeName =findViewById(R.id.name);
        ingerdients =findViewById(R.id.ingredients);
        directions =findViewById(R.id.directions);
        difficulty =findViewById(R.id.difficulty);
        prep =findViewById(R.id.prep);
        imgView = findViewById(R.id.chooseImageView);
        uploadRecipe = findViewById(R.id.buttonUploadRec);
        progressBar = findViewById(R.id.uploadProgress);

        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        databaseRef = FirebaseDatabase.getInstance().getReference("Recipes");

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

        uploadRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask!=null && uploadTask.isInProgress())
                    Toast.makeText(UploadRecipe.this,"Upload in progress",Toast.LENGTH_SHORT).show();
                else
                    recipeUpload();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK
            &&data!=null&&data.getData()!=null){
            imgUri = data.getData();
            imgView.setImageURI(imgUri);
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR= getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void recipeUpload(){
        if(imgUri==null)
            Toast.makeText(this,"No image selected",Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(recipeName.getText().toString().trim())||TextUtils.isEmpty(ingerdients.getText().toString().trim())||TextUtils.isEmpty(directions.getText().toString().trim())||TextUtils.isEmpty(difficulty.getText().toString().trim())||TextUtils.isEmpty(prep.getText().toString().trim()))
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();

        else
        {
            final StorageReference fileRef = storageRef.child(System.currentTimeMillis()+"."+getFileExtension(imgUri));
            uploadTask = fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },500);
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String userEmail = FirebaseUtils.getCurrentUserEmail();
                            if (userEmail!="") {
                                Recipe recipe = new Recipe(recipeName.getText().toString().trim(), uri.toString().trim(), directions.getText().toString().trim(), ingerdients.getText().toString().trim(), difficulty.getText().toString().trim(), prep.getText().toString().trim(), userEmail);
                                databaseRef.child(recipeName.getText().toString().trim()).setValue(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UploadRecipe.this,"Upload successful",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), ProfileRecipes.class));
                                    }
                                });
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadRecipe.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress= (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            });
        }
    }

}
