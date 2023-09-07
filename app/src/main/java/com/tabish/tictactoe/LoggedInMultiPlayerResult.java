package com.tabish.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class LoggedInMultiPlayerResult extends AppCompatActivity {

    private String sender;
    private String senderName;
    private String receiver;
    private String receiverName;
    private String game_id;
    private String senderUri;
    private String receiverUri;

    private String winner;

    private String gamePlayedFor;

    private FirebaseAuth frbAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private TextView loggedInMultiResult;

    private Button loggedInMultiHome;
    private Button loggedInMultiPlayAgain;

    private ImageView winnerImageView;

    private long gameStatus;
    private long senderJoined;

    private String newGameId;

    private String receiverGameId;

    public void loggedInMultiPlayAgain(View view)
    {
        if(currentUser.getUid().equals(receiver))
        {
            db.collection("MultiplayerGames").document(newGameId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            gameStatus = (long) document.get("gameStatus");
                            senderJoined = (long) document.get("senderJoined");

                            if(gameStatus==1)
                            {
                                Toast.makeText(LoggedInMultiPlayerResult.this,"host ("+senderName +") cancelled the game",Toast.LENGTH_LONG).show();

                                Intent goToMainAcitivity = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(goToMainAcitivity);
                            }

                        //    Toast.makeText(LoggedInMultiPlayerResult.this,"sender joined "+senderJoined,Toast.LENGTH_LONG).show();

                            if(senderJoined!=1)
                            {
                                Toast.makeText(LoggedInMultiPlayerResult.this,"waiting for host ("+senderName+") to join",Toast.LENGTH_LONG).show();
                            }

                            if(senderJoined==1 && gameStatus==0)
                            {
                                Intent goToLoggedInMultiplayerGame = new Intent(getApplicationContext(), LoggedInMultiplayer.class);
                                goToLoggedInMultiplayerGame.putExtra("sender", sender);
                                goToLoggedInMultiplayerGame.putExtra("senderName", senderName);
                                goToLoggedInMultiplayerGame.putExtra("receiver", receiver);
                                goToLoggedInMultiplayerGame.putExtra("receiverName", receiverName);
                                goToLoggedInMultiplayerGame.putExtra("game_id", newGameId);
                                goToLoggedInMultiplayerGame.putExtra("senderUri", senderUri);
                                goToLoggedInMultiplayerGame.putExtra("receiverUri", receiverUri);

                                startActivity(goToLoggedInMultiplayerGame);
                            }

                        } else {
                            Toast.makeText(LoggedInMultiPlayerResult.this,"host ("+senderName+") cancelled the game",Toast.LENGTH_LONG).show();
                            Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(goToMainActivity);
                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        if(currentUser.getUid().equals(sender))
        {
            Intent goToLoggedInMultiplayerGame = new Intent(getApplicationContext(), LoggedInMultiplayer.class);
            goToLoggedInMultiplayerGame.putExtra("sender", sender);
            goToLoggedInMultiplayerGame.putExtra("senderName", senderName);
            goToLoggedInMultiplayerGame.putExtra("receiver", receiver);
            goToLoggedInMultiplayerGame.putExtra("receiverName", receiverName);
            goToLoggedInMultiplayerGame.putExtra("game_id", newGameId);
            goToLoggedInMultiplayerGame.putExtra("senderUri", senderUri);
            goToLoggedInMultiplayerGame.putExtra("receiverUri", receiverUri);

            startActivity(goToLoggedInMultiplayerGame);
        }
    }

    public void loggedInMultiHome(View view)
    {

        db.collection("MultiplayerGames").document(newGameId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(LoggedInMultiPlayerResult.this,"you cancelled the game",Toast.LENGTH_LONG).show();

                        Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(goToMainActivity);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_multi_player_result);

        Intent fromLoggedInMultiPlayerGame = getIntent();
        sender = fromLoggedInMultiPlayerGame.getStringExtra("sender");
        senderName = fromLoggedInMultiPlayerGame.getStringExtra("senderName");
        receiver = fromLoggedInMultiPlayerGame.getStringExtra("receiver");
        receiverName = fromLoggedInMultiPlayerGame.getStringExtra("receiverName");
        game_id =fromLoggedInMultiPlayerGame.getStringExtra("game_id");
        senderUri = fromLoggedInMultiPlayerGame.getStringExtra("senderUri");
        receiverUri =fromLoggedInMultiPlayerGame.getStringExtra("receiverUri");
        winner=fromLoggedInMultiPlayerGame.getStringExtra("winner");
        gamePlayedFor=fromLoggedInMultiPlayerGame.getStringExtra("gamePlayedFor");

        newGameId= sender+receiver+(Long.parseLong(gamePlayedFor)+1);

        frbAuth = FirebaseAuth.getInstance();
        currentUser = frbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        loggedInMultiResult=findViewById(R.id.loggedInMultiResult);
        loggedInMultiHome = findViewById(R.id.buttonLoggedInMultiHome);
        loggedInMultiPlayAgain=findViewById(R.id.buttonLoggedInMultiPlayAgain);

        winnerImageView=findViewById(R.id.winnerImage);

        if(winner.equals("draw"))
        {
            loggedInMultiResult.setText("Game is draw");
            winnerImageView.setVisibility(View.INVISIBLE);
        }
        else
        {
            loggedInMultiResult.setText(winner +" is the winner");

            if(winner.equals(senderName))
            {
                if(!senderUri.equals("noPic"))
                {
                    Glide.with(getApplicationContext()).load(senderUri.toString()).into(winnerImageView);
                }
                else
                {
                    winnerImageView.setImageResource(R.drawable.x);
                }
            }

            if(winner.equals(receiverName))
            {
                if(!receiverUri.equals("noPic"))
                {
                    Glide.with(getApplicationContext()).load(receiverUri.toString()).into(winnerImageView);
                }
                else
                {
                    winnerImageView.setImageResource(R.drawable.o);
                }
            }
        }

        if(currentUser.getUid().equals(sender))
        {
            db.collection("MultiplayerGames").document(game_id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

            Map<String, Object> newMultiplayerGame = new HashMap<>();
            newMultiplayerGame.put("sender", sender);
            newMultiplayerGame.put("senderName", senderName);
            newMultiplayerGame.put("receiver", receiver);
            newMultiplayerGame.put("receiverName", receiverName);
            newMultiplayerGame.put("game_id", newGameId);
            newMultiplayerGame.put("turn", 0);
            newMultiplayerGame.put("gameStatus", 0);
            newMultiplayerGame.put("tappedPosition", -1);
            newMultiplayerGame.put("senderJoined", 0);
            newMultiplayerGame.put("receiverJoined", 0);
            newMultiplayerGame.put("senderUri", senderUri);
            newMultiplayerGame.put("receiverUri", receiverUri);
            newMultiplayerGame.put("gamePlayedFor", (Long.parseLong(gamePlayedFor)+1));

            Map<String, Object> positions = new HashMap<>();
            for(int i=0;i<9;i+=1)
            {
                positions.put(Integer.toString(i), 2);
            }

            Map<String, Object> tappedPositions = new HashMap<>();

            tappedPositions.put("tappedPosition", -1);

            Map<String, Object> gameTurn = new HashMap<>();

            gameTurn.put("turn", 0);

            db.collection("MultiplayerGames").document(newGameId)
                    .set(newMultiplayerGame)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            db.collection("MultiplayerGames").document(newGameId).collection("Positions").document(newGameId)
                                    .set(positions)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            db.collection("MultiplayerGames").document(newGameId).collection("tappedPosition").document(newGameId)
                                                    .set(tappedPositions)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            db.collection("MultiplayerGames").document(newGameId).collection("turn").document(newGameId)
                                                                    .set(gameTurn)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            if(winner.equals(senderName))
                                                                            {
                                                                                db.collection("users").document(sender)
                                                                                        .update("multiplayer_no_of_games", FieldValue.increment(1),"multiplayer_no_of_first_turns",FieldValue.increment(1),"multiplayer_no_of_games_won",FieldValue.increment(1),"total_no_of_games", FieldValue.increment(1),"total_no_of_first_turns",FieldValue.increment(1),"total_no_of_games_won",FieldValue.increment(1))

                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                                db.collection("users").document(receiver)
                                                                                                        .update("multiplayer_no_of_games", FieldValue.increment(1),"multiplayer_no_of_second_turns",FieldValue.increment(1),"multiplayer_no_of_games_lost",FieldValue.increment(1),"total_no_of_games", FieldValue.increment(1),"total_no_of_second_turns",FieldValue.increment(1),"total_no_of_games_lost",FieldValue.increment(1))

                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {

                                                                                                               loggedInMultiPlayAgain.setEnabled(true);
                                                                                                                loggedInMultiPlayAgain.setVisibility(View.VISIBLE);

                                                                                                                loggedInMultiHome.setVisibility(View.VISIBLE);
                                                                                                                loggedInMultiHome.setEnabled(true);
                                                                                                            }
                                                                                                        })
                                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                                            @Override
                                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                            }
                                                                                        });
                                                                            }

                                                                            if(winner.equals(receiverName))
                                                                            {
                                                                                db.collection("users").document(sender)
                                                                                        .update("multiplayer_no_of_games", FieldValue.increment(1),"multiplayer_no_of_first_turns",FieldValue.increment(1),"multiplayer_no_of_games_lost",FieldValue.increment(1),"total_no_of_games", FieldValue.increment(1),"total_no_of_first_turns",FieldValue.increment(1),"total_no_of_games_lost",FieldValue.increment(1))

                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                                db.collection("users").document(receiver)
                                                                                                        .update("multiplayer_no_of_games", FieldValue.increment(1),"multiplayer_no_of_second_turns",FieldValue.increment(1),"multiplayer_no_of_games_won",FieldValue.increment(1),"total_no_of_games", FieldValue.increment(1),"total_no_of_second_turns",FieldValue.increment(1),"total_no_of_games_won",FieldValue.increment(1))

                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {

                                                                                                            loggedInMultiPlayAgain.setEnabled(true);
                                                                                                                loggedInMultiPlayAgain.setVisibility(View.VISIBLE);

                                                                                                                loggedInMultiHome.setVisibility(View.VISIBLE);
                                                                                                                loggedInMultiHome.setEnabled(true);
                                                                                                            }
                                                                                                        })
                                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                                            @Override
                                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                            }
                                                                                        });
                                                                            }

                                                                            if(winner.equals("draw"))
                                                                            {
                                                                                db.collection("users").document(sender)
                                                                                        .update("multiplayer_no_of_games", FieldValue.increment(1),"multiplayer_no_of_first_turns",FieldValue.increment(1),"multiplayer_no_of_games_draw",FieldValue.increment(1),"total_no_of_games", FieldValue.increment(1),"total_no_of_first_turns",FieldValue.increment(1),"total_no_of_games_draw",FieldValue.increment(1))

                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                                db.collection("users").document(receiver)
                                                                                                        .update("multiplayer_no_of_games", FieldValue.increment(1),"multiplayer_no_of_second_turns",FieldValue.increment(1),"multiplayer_no_of_games_draw",FieldValue.increment(1),"total_no_of_games", FieldValue.increment(1),"total_no_of_second_turns",FieldValue.increment(1),"total_no_of_games_draw",FieldValue.increment(1))

                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {

                                                                                                          loggedInMultiPlayAgain.setEnabled(true);
                                                                                                                loggedInMultiPlayAgain.setVisibility(View.VISIBLE);

                                                                                                                loggedInMultiHome.setVisibility(View.VISIBLE);
                                                                                                                loggedInMultiHome.setEnabled(true);
                                                                                                            }
                                                                                                        })
                                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                                            @Override
                                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                            }
                                                                                        });
                                                                            }
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
                            Toast.makeText(LoggedInMultiPlayerResult.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }

        if(currentUser.getUid().equals(receiver))
        {

            db.collection("MultiplayerGames")
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

                                        receiverGameId=(String) dc.getDocument().getId();

                                        if(receiverGameId.equals(newGameId))
                                        {
                                            loggedInMultiPlayAgain.setEnabled(true);
                                            loggedInMultiPlayAgain.setVisibility(View.VISIBLE);
                                        }

                                        break;
                                    case MODIFIED:

                                        break;
                                    case REMOVED:

                                      /*  receiverGameIdDeleted=(String) dc.getDocument().getId();

                                        if(receiverGameIdDeleted.equals(newGameId))
                                        {
                                            Toast.makeText(LoggedInMultiPlayerResult.this,"host ("+senderName+") cancelled the game",Toast.LENGTH_LONG).show();
                                            Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(goToMainActivity);
                                        }*/

                                        break;
                                }
                            }

                        }
                    });



            /*db.collection("MultiplayerGames").document(game_id).
                    addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {

                                return;
                            }

                            if (snapshot != null && snapshot.exists()) {

                                Map<String, Object> gamePositions = snapshot.getData();

                                gameStatus=gamePositions.get("gameStatus").toString();

                                if(Long.parseLong(gameStatus)==1)
                                {
                                    Toast.makeText(LoggedInMultiPlayerResult.this,senderName +" cancelled the game",Toast.LENGTH_LONG).show();

                                    Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(goToMainActivity );
                                }

                            } else {

                            }
                        }
                    });*/
        }

       /* db.collection("MultiplayerGames").document(game_id).update("tappedPosition",-1);
        db.collection("MultiplayerGames").document(game_id).update("senderJoined",0);
        db.collection("MultiplayerGames").document(game_id).update("receiverJoined",0);
        db.collection("MultiplayerGames").document(game_id).collection("tappedPosition").document(game_id).update("tappedPosition", -1);
        db.collection("MultiplayerGames").document(game_id).collection("turn").document(game_id).update("turn",0);

        db.collection("users").document(sender).update("multiplayer_no_of_games", FieldValue.increment(1));
        db.collection("users").document(receiver).update("multiplayer_no_of_games", FieldValue.increment(1));

        db.collection("users").document(sender).update("multiplayer_no_of_first_turns", FieldValue.increment(1));
        db.collection("users").document(receiver).update("multiplayer_no_of_second_turns", FieldValue.increment(1));

        for(int i=0;i<9;i+=1)
        {
            db.collection("MultiplayerGames").document(game_id).collection("Positions").document(game_id).update(Integer.toString(i), 2);
        }*/

       /* if(winner.equals("draw"))
        {
            db.collection("users").document(sender).update("multiplayer_no_of_games_draw", FieldValue.increment(1));
            db.collection("users").document(receiver).update("multiplayer_no_of_games_draw", FieldValue.increment(1));
            loggedInMultiResult.setText("Game is draw");
        }
        else
        {
            if(winner.equals(senderName))
            {
                db.collection("users").document(sender).update("multiplayer_no_of_games_won", FieldValue.increment(1));
                db.collection("users").document(receiver).update("multiplayer_no_of_games_lost", FieldValue.increment(1));
            }
            if(winner.equals(receiverName))
            {
                db.collection("users").document(sender).update("multiplayer_no_of_games_lost", FieldValue.increment(1));
                db.collection("users").document(receiver).update("multiplayer_no_of_games_won", FieldValue.increment(1));
            }
            loggedInMultiResult.setText(winner +" is the winner");
        }

        if(currentUser.getUid().equals(sender))
        {
            loggedInMultiHome.setVisibility(View.VISIBLE);
            loggedInMultiHome.setEnabled(true);
        }*/

    }

    @Override //to prevent going back when back button is clicked
    public void onBackPressed()
    {
        Toast.makeText(this,"Cannot go back to previous online realtime multiplayer game",Toast.LENGTH_LONG).show();
    }
}