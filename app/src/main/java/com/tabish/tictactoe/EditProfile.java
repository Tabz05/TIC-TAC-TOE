package com.tabish.tictactoe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth frbAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private Uri selectedImage;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private EditText newUsername;
    private Button removePic;
    private Button submitEditProfile;

    private ImageView imageView3;

    private String currUsername;
    private String existing_username;
    private String new_username;
    private String senderGameId;
    private String receiverGameId;
    private String senderInviteId;
    private String receiverInviteId;

    private boolean foundUsername=false;

    private void getPhoto()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        someActivityResultLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }

    public void chooseProfilePicEdit (View view)
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //checking if permission for gallery has been granted or not
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            getPhoto();
        }
    }

    private void updateImage()
    {
        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Updating profile picture...");
        progressDialog.show();

        // Defining the child of storageReference
        StorageReference ref = storageReference.child("users").child(currentUser.getUid()).child("profilePic");

        // adding listeners on upload
        // or failure of image
        ref.putFile(selectedImage).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(
                            UploadTask.TaskSnapshot taskSnapshot)
                    {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Map<String, Object> userProfilePic = new HashMap<>();
                                userProfilePic.put("hasProfilePic", true);
                                userProfilePic.put("profilePicUri", uri.toString());

                                db.collection("users").document(currentUser.getUid())
                                        .update(userProfilePic)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                // Image uploaded successfully
                                                // Dismiss dialog
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProfile.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                                                Intent goToMainActivity = new Intent(getApplicationContext(),MainActivity.class);

                                                startActivity(goToMainActivity);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                            }
                                        });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {

                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(
                                    UploadTask.TaskSnapshot taskSnapshot)
                            {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int)progress + "%");
                            }
                        });
    }

    private void updateUsername()
    {
        db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //   Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        currUsername = (String) document.get("username");

                        db.collection("usernames").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                existing_username = (String) document.getId();

                                                if (existing_username.equals(newUsername.getText().toString())) {
                                                    foundUsername = true;
                                                }
                                            }

                                            if (!foundUsername) {

                                                ProgressDialog progressDialog
                                                        = new ProgressDialog(EditProfile.this);
                                                progressDialog.setTitle("Updating username...");
                                                progressDialog.show();

                                                new_username = newUsername.getText().toString();
                                                Map<String, Object> newUsername = new HashMap<>();
                                                newUsername.put("username", new_username);

                                                db.collection("users").document(currentUser.getUid())
                                                        .update(newUsername)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                Map<String, Object> nameUpdate = new HashMap<>();
                                                                nameUpdate.put(new_username, currentUser.getUid());

                                                                db.collection("usernames").document(new_username)
                                                                        .set(nameUpdate)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                                db.collection("usernames").document(currUsername).delete();

                                                                                db.collection("MultiplayerGames")
                                                                                        .whereEqualTo("sender", currentUser.getUid())
                                                                                        .get()
                                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                if (task.isSuccessful()) {

                                                                                                    Map<String, Object> senderNameUpdate = new HashMap<>();
                                                                                                    senderNameUpdate.put("senderName", new_username);

                                                                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                                        senderGameId=  (String) document.getId();

                                                                                                        db.collection("MultiplayerGames").document(senderGameId)
                                                                                                                .update(senderNameUpdate)
                                                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void aVoid) {

                                                                                                                    }

                                                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                                            @Override
                                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                                            }
                                                                                                        });

                                                                                                    }

                                                                                                    Map<String, Object> receiverNameUpdate = new HashMap<>();
                                                                                                    receiverNameUpdate.put("receiverName", new_username);

                                                                                                    db.collection("MultiplayerGames")
                                                                                                            .whereEqualTo("receiver", currentUser.getUid())
                                                                                                            .get()
                                                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                    if (task.isSuccessful()) {

                                                                                                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                                                            receiverGameId=  (String) document.getId();

                                                                                                                            db.collection("MultiplayerGames").document(receiverGameId)
                                                                                                                                    .update(receiverNameUpdate)
                                                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                        @Override
                                                                                                                                        public void onSuccess(Void aVoid) {

                                                                                                                                        }

                                                                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                                                                @Override
                                                                                                                                public void onFailure(@NonNull Exception e) {

                                                                                                                                }
                                                                                                                            });


                                                                                                                        }

                                                                                                                        Map<String, Object> senderInviteUpdate = new HashMap<>();
                                                                                                                        senderInviteUpdate.put("senderName", new_username);

                                                                                                                        db.collection("invites")
                                                                                                                                .whereEqualTo("sender", currentUser.getUid())
                                                                                                                                .get()
                                                                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                                    @Override
                                                                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                                        if (task.isSuccessful()) {

                                                                                                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                                                                                senderInviteId=  (String) document.getId();

                                                                                                                                                db.collection("invites").document(senderInviteId)
                                                                                                                                                        .update(senderInviteUpdate)
                                                                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                            @Override
                                                                                                                                                            public void onSuccess(Void aVoid) {

                                                                                                                                                            }

                                                                                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onFailure(@NonNull Exception e) {

                                                                                                                                                    }
                                                                                                                                                });


                                                                                                                                            }

                                                                                                                                            Map<String, Object> receiverInviteUpdate = new HashMap<>();
                                                                                                                                            receiverInviteUpdate.put("receiverName", new_username);


                                                                                                                                            db.collection("invites")
                                                                                                                                                    .whereEqualTo("receiver", currentUser.getUid())
                                                                                                                                                    .get()
                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                                                            if (task.isSuccessful()) {

                                                                                                                                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                                                                                                    receiverInviteId=  (String) document.getId();

                                                                                                                                                                    db.collection("invites").document(receiverInviteId)
                                                                                                                                                                            .update(receiverInviteUpdate)
                                                                                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                                @Override
                                                                                                                                                                                public void onSuccess(Void aVoid) {

                                                                                                                                                                                }

                                                                                                                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onFailure(@NonNull Exception e) {

                                                                                                                                                                        }
                                                                                                                                                                    });


                                                                                                                                                                }

                                                                                                                                                                Toast.makeText(EditProfile.this, "Username changed!!", Toast.LENGTH_SHORT).show();

                                                                                                                                                                progressDialog.dismiss();

                                                                                                                                                                if (selectedImage != null)
                                                                                                                                                                {
                                                                                                                                                                    updateImage();
                                                                                                                                                                }
                                                                                                                                                                else
                                                                                                                                                                 {
                                                                                                                                                                    Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);

                                                                                                                                                                    startActivity(goToMainActivity);
                                                                                                                                                                }


                                                                                                                                                            } else {

                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    });




                                                                                                                                        } else {

                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });




                                                                                                                    } else {

                                                                                                                    }
                                                                                                                }
                                                                                                            });

                                                                                                } else {

                                                                                                }
                                                                                            }
                                                                                        });

                                                                            }

                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                    }
                                                                });
                                                            }
                                                        });

                                            } else {
                                                  Toast.makeText(EditProfile.this,"Username is already in use",Toast.LENGTH_LONG).show();
                                            }

                                        } else {

                                        }
                                    }
                                });
                    } else {

                    }

                } else {

                }

            }

        });
    }

    public void submitEditProfile(View view)
    {
        if (selectedImage != null && newUsername.getText().toString().isEmpty())
        {
            updateImage();
        }

        if(!(newUsername.getText().toString().isEmpty()))
        {
            updateUsername();
        }
    }

    public void removeProfilePic(View view)
    {
        imageView3.setImageResource(R.drawable.profilepic);
        removePic.setVisibility(View.INVISIBLE);
        removePic.setEnabled(false);

        Map<String, Object> userProfilePic = new HashMap<>();
        userProfilePic.put("hasProfilePic", false);
        userProfilePic.put("profilePicUri", "");

        db.collection("users").document(currentUser.getUid())
                .update(userProfilePic)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(EditProfile.this,"Profile pic removed",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        frbAuth = FirebaseAuth.getInstance();
        currentUser = frbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        newUsername=findViewById(R.id.newUsername);
        imageView3=findViewById(R.id.imageView3);
        removePic=findViewById(R.id.removeProfilePicEdit);
        submitEditProfile=findViewById(R.id.submitEditProfile);

        DocumentReference docRef = db.collection("users").document(currentUser.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String currUsername = (String) document.get("username");
                        boolean hasProfilePic = (boolean) document.get("hasProfilePic");
                        String profilePicUri = (String) document.get("profilePicUri");

                        if(hasProfilePic)
                        {
                            Glide.with(getApplicationContext()).load(profilePicUri.toString()).apply(RequestOptions.circleCropTransform()).into(imageView3);

                            removePic.setVisibility(View.VISIBLE);
                            removePic.setEnabled(true);

                        }
                        else
                        {
                            imageView3.setImageResource(R.drawable.profilepic);
                        }

                    } else {
                        //   Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes

                        Intent data = result.getData();

                        selectedImage = data.getData();

                        Glide.with(getApplicationContext()).asBitmap().load(selectedImage.toString()).apply(RequestOptions.circleCropTransform()).into(imageView3);

                        removePic.setVisibility(View.VISIBLE);
                        removePic.setEnabled(true);
                    }
                }
            });
}