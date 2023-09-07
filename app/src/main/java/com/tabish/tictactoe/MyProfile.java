package com.tabish.tictactoe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Map;

public class MyProfile extends AppCompatActivity {

    private FirebaseAuth frbAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private TextView myProfileText;
    private ImageView myProfilePicture;

    private LinearLayout linearLayout;

    private DocumentReference noOfPosts;

    private String username;
    private String email;
    private String hasProfilePic;
    private String joinedOn;

    private String profileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        frbAuth = FirebaseAuth.getInstance();
        currentUser = frbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        noOfPosts = db.collection("users").document(currentUser.getUid());

        myProfileText = findViewById(R.id.MyProfileText);
        myProfilePicture = findViewById(R.id.imageView2);

        linearLayout = findViewById(R.id.feedLinearLayout);

        db.collection("users").document(currentUser.getUid()).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Map<String, Object> userDetails = snapshot.getData();
                            username = userDetails.get("username").toString();
                            email = userDetails.get("email").toString();
                            hasProfilePic = userDetails.get("hasProfilePic").toString();
                            joinedOn = userDetails.get("joinedOn").toString();

                            String info = "\nUsername: " + username + "\n\nEmail: " + email+"\n\nJoined On: "+joinedOn;
                            myProfileText.setText(info);

                            if (hasProfilePic.equals("true"))
                            {
                                profileUri = userDetails.get("profilePicUri").toString();
                                Glide.with(getApplicationContext()).load(profileUri.toString()).apply(RequestOptions.circleCropTransform()).into(myProfilePicture);
                            }
                            else
                                {
                                myProfilePicture.setImageResource(R.drawable.profilepic);
                            }

                        } else {

                        }
                    }
                });

    }

}