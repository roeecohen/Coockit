package com.example.coockit.Profile;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.coockit.Classes.Member;
import com.example.coockit.R;
import com.example.coockit.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

/**
 * profile page
 * each user has its own profile page with its own details
 * in this page the user can upload a recipe that is saved under his username
 */
public class ProfileFragment extends Fragment {
    private ImageView imgView;
    private TextView name;
    private TextView email;
    private TextView phone;
    private Button saveBtn;
    private ProgressBar progressBar;
    private Uri imgUri;

    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private StorageTask uploadTask;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profile, container, false);

        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        databaseRef = FirebaseDatabase.getInstance().getReference("Members");

        name = (TextView)view.findViewById(R.id.prof_name);
        email = (TextView)view.findViewById(R.id.prof_email);
        phone = (TextView)view.findViewById(R.id.prof_phone);
        saveBtn = (Button) view.findViewById(R.id.save_btn);
        imgView = (ImageView)view.findViewById(R.id.profile_img);
        progressBar = (ProgressBar) view.findViewById(R.id.prof_progress_bar);

        progressBar.setProgressTintList(ColorStateList.valueOf(Color.WHITE));

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });
        setMemberData();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK
                &&data!=null&&data.getData()!=null){
            imgUri = data.getData();
            imgView.setImageURI(imgUri);

            saveBtn.setVisibility(View.VISIBLE);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    memberImageUpload();
                }
            });
        }
    }
    private String getFileExtension(Uri uri)
    {
        ContentResolver cR= getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void memberImageUpload(){

        final StorageReference fileRef = storageRef.child(System.currentTimeMillis()+"."+getFileExtension(imgUri));
        uploadTask = fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        Query query = databaseRef.orderByChild("email").equalTo(FirebaseUtils.getCurrentUserEmail());

                        query.addChildEventListener(new ChildEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                databaseRef.child(dataSnapshot.getKey()).child("img").setValue(uri.toString().trim());
                                progressBar.setVisibility(View.GONE);
                                saveBtn.setVisibility(View.GONE);

                                if(getActivity()!=null)
                                    Toast.makeText(getActivity().getApplicationContext(),"Image upload succeeded",Toast.LENGTH_SHORT).show();
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
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMemberData() {
        Query query = databaseRef.orderByChild("email").equalTo(FirebaseUtils.getCurrentUserEmail());

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Member member = (Member)dataSnapshot.getValue(Member.class);
                name.setText(member.getFullName());
                email.setText(member.getEmail());
                phone.setText(member.getPhone());
                if(!member.getImg().isEmpty())
                    Picasso.get().load(member.getImg()).into(imgView);
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
}