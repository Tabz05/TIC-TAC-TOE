package com.tabish.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class Invites extends AppCompatActivity {

    private FirebaseAuth frbAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private String sender;
    private String senderName;
    private String invite_id;
    private String receiver;
    private String receiverName;

    private LinearLayout inviteLinearLayout;

    private String senderUri;
    private String receiverUri;

    private Boolean senderHasPic;
    private Boolean receiverHasPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);

        frbAuth = FirebaseAuth.getInstance();
        currentUser = frbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        inviteLinearLayout= findViewById(R.id.invitesLinearLayout);

        db.collection("invites")
                .whereEqualTo("receiver", currentUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:

                                    invite_id=(String) dc.getDocument().get("inviteId");
                                    sender= (String) dc.getDocument().get("sender");
                                    senderName=(String) dc.getDocument().get("senderName");
                                    receiver=(String) dc.getDocument().get("receiver");
                                    receiverName=(String) dc.getDocument().get("receiverName");

                                    LinearLayout inviteBlockLinearLayout = new LinearLayout(getApplicationContext());
                                    inviteBlockLinearLayout.setOrientation(LinearLayout.VERTICAL);
                                    inviteBlockLinearLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.rounded_form));

                                    LinearLayout.LayoutParams inviteBlockLinearLayoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    inviteBlockLinearLayoutParams.setMargins(20,20,20,0);

                                    TextView inviteText = new TextView(getApplicationContext());

                                    LinearLayout.LayoutParams inviteTextLayoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    inviteTextLayoutParams.setMargins( 0 , 5, 0 , 0 ) ;
                                    inviteTextLayoutParams.gravity = Gravity.CENTER;

                                    inviteText.setText(senderName+ " sent you a request");
                                    inviteText.setTextSize(20);
                                    inviteText.setTextColor(getResources().getColor(R.color.black));

                                    inviteBlockLinearLayout.addView(inviteText, inviteTextLayoutParams);

                                    LinearLayout AcceptRejectButtons =  new LinearLayout(getApplicationContext());
                                    AcceptRejectButtons.setOrientation(LinearLayout.HORIZONTAL);

                                    LinearLayout.LayoutParams AcceptRejectButtonsLayoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    //AcceptRejectButtons.setGravity(Gravity.CENTER);

                                    inviteBlockLinearLayout.addView(AcceptRejectButtons, AcceptRejectButtonsLayoutParams);

                                    Space space1=new Space(getApplicationContext());

                                    LinearLayout.LayoutParams space1LayoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            1.0f
                                    );

                                    AcceptRejectButtons.addView(space1,space1LayoutParams);

                                    Button acceptButton = new Button(getApplicationContext());

                                    LinearLayout.LayoutParams acceptButtonLayoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            1.0f
                                            );

                                    acceptButton .setText("Accept");
                                    acceptButton.setTextColor(getResources().getColor(R.color.white));
                                    acceptButton .setTextSize(20);
                                    acceptButton .setBackgroundColor(Color.parseColor("#5cb85c"));

                                 //   acceptButtonLayoutParams.setMargins(0,0,20,0);

                                    AcceptRejectButtons.addView(acceptButton, acceptButtonLayoutParams);

                                    Space space2=new Space(getApplicationContext());

                                    LinearLayout.LayoutParams space2LayoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            1.0f
                                    );

                                    AcceptRejectButtons.addView(space2,space2LayoutParams);

                                    Button rejectButton = new Button(getApplicationContext());

                                    LinearLayout.LayoutParams rejectButtonLayoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            1.0f
                                            );


                                    rejectButton.setText("Reject");
                                    rejectButton.setTextColor(getResources().getColor(R.color.white));
                                    rejectButton.setTextSize(20);
                                    rejectButton.setBackgroundColor(getResources().getColor(R.color.red));

                                    AcceptRejectButtons.addView(rejectButton,rejectButtonLayoutParams);

                                    Space space3=new Space(getApplicationContext());

                                    LinearLayout.LayoutParams space3LayoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            1.0f
                                    );

                                    AcceptRejectButtons.addView(space3,space3LayoutParams);

                                    inviteLinearLayout.addView(inviteBlockLinearLayout,inviteBlockLinearLayoutParams);

                                    acceptButton.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {

                                            ProgressDialog progressDialog
                                                    = new ProgressDialog(Invites.this);
                                            progressDialog.setTitle("Creating online multiplayer");
                                            progressDialog.show();

                                            db.collection("users").document(sender).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {

                                                            senderHasPic = (boolean) document.get("hasProfilePic");

                                                            if(senderHasPic)
                                                            {
                                                                senderUri = (String) document.get("profilePicUri");
                                                            }
                                                            else
                                                            {
                                                                senderUri = "noPic";
                                                            }

                                                            db.collection("users").document(receiver).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        DocumentSnapshot document = task.getResult();
                                                                        if (document.exists()) {

                                                                            receiverHasPic = (boolean) document.get("hasProfilePic");

                                                                            if(receiverHasPic)
                                                                            {
                                                                                receiverUri = (String) document.get("profilePicUri");
                                                                            }
                                                                            else
                                                                            {
                                                                                receiverUri = "noPic";
                                                                            }

                                                                                db.collection("invites").document(invite_id)
                                                                                        .delete()
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                                Map<String, Object> newMultiplayerGame = new HashMap<>();
                                                                                                newMultiplayerGame.put("sender", sender);
                                                                                                newMultiplayerGame.put("senderName", senderName);
                                                                                                newMultiplayerGame.put("receiver", receiver);
                                                                                                newMultiplayerGame.put("receiverName", receiverName);
                                                                                                newMultiplayerGame.put("game_id", invite_id);
                                                                                                newMultiplayerGame.put("turn", 0);
                                                                                                newMultiplayerGame.put("gameStatus", 0);
                                                                                                newMultiplayerGame.put("tappedPosition", -1);
                                                                                                newMultiplayerGame.put("senderJoined", 0);
                                                                                                newMultiplayerGame.put("receiverJoined", 0);
                                                                                                newMultiplayerGame.put("senderUri", senderUri);
                                                                                                newMultiplayerGame.put("receiverUri", receiverUri);
                                                                                                newMultiplayerGame.put("gamePlayedFor", 1);


                                                                                                Map<String, Object> positions = new HashMap<>();
                                                                                                for(int i=0;i<9;i+=1)
                                                                                                {
                                                                                                    positions.put(Integer.toString(i), 2);
                                                                                                }

                                                                                                Map<String, Object> tappedPositions = new HashMap<>();

                                                                                                tappedPositions.put("tappedPosition", -1);

                                                                                                Map<String, Object> gameTurn = new HashMap<>();

                                                                                                gameTurn.put("turn", 0);

                                                                                                    db.collection("MultiplayerGames").document(invite_id)
                                                                                                            .set(newMultiplayerGame)
                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Void aVoid) {

                                                                                                                    db.collection("MultiplayerGames").document(invite_id).collection("Positions").document(invite_id)
                                                                                                                            .set(positions)
                                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(Void aVoid) {

                                                                                                                                    db.collection("MultiplayerGames").document(invite_id).collection("tappedPosition").document(invite_id)
                                                                                                                                            .set(tappedPositions)
                                                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                @Override
                                                                                                                                                public void onSuccess(Void aVoid) {

                                                                                                                                                    db.collection("MultiplayerGames").document(invite_id).collection("turn").document(invite_id)
                                                                                                                                                            .set(gameTurn)
                                                                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                @Override
                                                                                                                                                                public void onSuccess(Void aVoid) {


                                                                                                                                                                    progressDialog.dismiss();
                                                                                                                        Intent goToLoggedInMultiplayerGame = new Intent(getApplicationContext(), LoggedInMultiplayer.class);
                                                                                                                        goToLoggedInMultiplayerGame.putExtra("sender", sender);
                                                                                                                        goToLoggedInMultiplayerGame.putExtra("senderName", senderName);
                                                                                                                        goToLoggedInMultiplayerGame.putExtra("receiver", receiver);
                                                                                                                        goToLoggedInMultiplayerGame.putExtra("receiverName", receiverName);
                                                                                                                        goToLoggedInMultiplayerGame.putExtra("game_id", invite_id);
                                                                                                                        goToLoggedInMultiplayerGame.putExtra("senderUri", senderUri);
                                                                                                                        goToLoggedInMultiplayerGame.putExtra("receiverUri", receiverUri);


                                                                                                                        startActivity(goToLoggedInMultiplayerGame);

                                                                                                                                                                }
                                                                                                                                                            })
                                                                                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                                                                                @Override
                                                                                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                                                                                    e.printStackTrace();
                                                                                                                                                                }
                                                                                                                                                            });
                                                                                                                                }
                                                                                                                            })
                                                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                                                @Override
                                                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                }
                                                                                                                            });

                                                                                                                                }
                                                                                                                            })
                                                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                                                @Override
                                                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                }
                                                                                                                            });

                                                                                                                }
                                                                                                            })
                                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                                @Override
                                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                                    Toast.makeText(Invites.this, e.toString(), Toast.LENGTH_LONG).show();
                                                                                                                }
                                                                                                            });

                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                            }
                                                                                        });


                                                                        } else {
                                                                            //   Log.d(TAG, "No such document");
                                                                        }
                                                                    } else {
                                                                        //Log.d(TAG, "get failed with ", task.getException());
                                                                    }
                                                                }
                                                            });

                                                        } else {
                                                            //   Log.d(TAG, "No such document");
                                                        }
                                                    } else {
                                                        //Log.d(TAG, "get failed with ", task.getException());
                                                    }
                                                }
                                            });
                                        }
                                    });

                                    rejectButton.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            db.collection("invites").document(invite_id)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(Invites.this, e.toString(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }
                                    });

                                    break;
                                case MODIFIED:

                                    break;
                                case REMOVED:

                                    break;
                            }
                        }

                    }
                });



    }
}