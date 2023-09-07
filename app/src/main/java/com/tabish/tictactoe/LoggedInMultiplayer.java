package com.tabish.tictactoe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import java.util.ArrayList;
import java.util.Map;

public class LoggedInMultiplayer extends AppCompatActivity {

    private String sender;
    private String senderName;
    private String receiver;
    private String receiverName;
    private String game_id;
    private String senderUri;
    private String receiverUri;

    private String gamePlayedFor;

    private FirebaseAuth frbAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private TextView playerNameSymbol;

    private ArrayList<String> positionList;

    private boolean gameStart=false;
    private boolean bothJoined=false;

    private int gameover;
    private int win=0;

    private String tappedPosition;
    private String playerTurn;
    private String gameStatus;

    private String senderJoined;
    private String receiverJoined;

    private DocumentReference multiplayerPositions;
    private DocumentReference multiplayerTappedPosition;
    private DocumentReference multiplayerTurn;
    private DocumentReference updateTap;

    private DocumentReference senderHasJoined;
    private DocumentReference receiverHasJoined;

    private int tappedcounter=-1;

    private String tap;

    private ImageView image0;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;
    private ImageView image6;
    private ImageView image7;
    private ImageView image8;

    private ListenerRegistration registration1;
    private ListenerRegistration registration2;
    private ListenerRegistration registration3;
    private ListenerRegistration registration4;

    private void checkrow()
    {
        String found="";
        int i=0;
        int j=0;
        while(j<3)
        {
            if(Long.parseLong(positionList.get(i))==Long.parseLong(positionList.get(i+1)) && Long.parseLong(positionList.get(i))==Long.parseLong(positionList.get(i+2)) && Long.parseLong(positionList.get(i))!=2)
            {
                gameover=1;
                win=1;
                if(Long.parseLong(positionList.get(i))==0)
                {
                    found=senderName;
                    break;
                }
                else
                {
                    found=receiverName;
                    break;
                }
            }
            i+=3;
            j+=1;
        }
        if(!found.equals(""))
        {
            registration4.remove();
            registration1.remove();
            registration3.remove();
            registration2.remove();

            Intent goToLoggedInMultiplayerGameResult = new Intent(getApplicationContext(), LoggedInMultiPlayerResult.class);
            goToLoggedInMultiplayerGameResult.putExtra("sender", sender);
            goToLoggedInMultiplayerGameResult.putExtra("senderName", senderName);
            goToLoggedInMultiplayerGameResult.putExtra("receiver", receiver);
            goToLoggedInMultiplayerGameResult.putExtra("receiverName", receiverName);
            goToLoggedInMultiplayerGameResult.putExtra("game_id", game_id);
            goToLoggedInMultiplayerGameResult.putExtra("senderUri", senderUri);
            goToLoggedInMultiplayerGameResult.putExtra("receiverUri", receiverUri);
            goToLoggedInMultiplayerGameResult.putExtra("winner", found);
            goToLoggedInMultiplayerGameResult.putExtra("gamePlayedFor", gamePlayedFor);
            startActivity(goToLoggedInMultiplayerGameResult);
        }
    }

    private void checkcol()
    {
        String found="";
        int i=0;
        int j=0;
        while(j<3)
        {
            if(Long.parseLong(positionList.get(i))==Long.parseLong(positionList.get(i+3)) && Long.parseLong(positionList.get(i))==Long.parseLong(positionList.get(i+6)) && Long.parseLong(positionList.get(i))!=2)
            {
                gameover=1;
                win=1;
                if(Long.parseLong(positionList.get(i))==0)
                {
                    found=senderName;
                    break;
                }
                else
                {
                    found=receiverName;
                    break;
                }
            }
            i+=1;
            j+=1;
        }
        if(!found.equals(""))
        {
            registration4.remove();
            registration1.remove();
            registration3.remove();
            registration2.remove();

            Intent goToLoggedInMultiplayerGameResult = new Intent(getApplicationContext(), LoggedInMultiPlayerResult.class);
            goToLoggedInMultiplayerGameResult.putExtra("sender", sender);
            goToLoggedInMultiplayerGameResult.putExtra("senderName", senderName);
            goToLoggedInMultiplayerGameResult.putExtra("receiver", receiver);
            goToLoggedInMultiplayerGameResult.putExtra("receiverName", receiverName);
            goToLoggedInMultiplayerGameResult.putExtra("game_id", game_id);
            goToLoggedInMultiplayerGameResult.putExtra("senderUri", senderUri);
            goToLoggedInMultiplayerGameResult.putExtra("receiverUri", receiverUri);
            goToLoggedInMultiplayerGameResult.putExtra("winner", found);
            goToLoggedInMultiplayerGameResult.putExtra("gamePlayedFor", gamePlayedFor);
            startActivity(goToLoggedInMultiplayerGameResult);
        }
    }

    private void checkmaindiag()
    {
        String found="";
        int i=0;
        if ((Long.parseLong(positionList.get(i))==Long.parseLong(positionList.get(i+4)) && Long.parseLong(positionList.get(i))==Long.parseLong(positionList.get(i+8)) && Long.parseLong(positionList.get(i))!=2))
        {
            gameover=1;
            win=1;
            if(Long.parseLong(positionList.get(i))==0)
            {
                found=senderName;
            }
            else
            {
                found=receiverName;
            }
        }
        if(!found.equals(""))
        {
            registration4.remove();
            registration1.remove();
            registration3.remove();
            registration2.remove();

            Intent goToLoggedInMultiplayerGameResult = new Intent(getApplicationContext(), LoggedInMultiPlayerResult.class);
            goToLoggedInMultiplayerGameResult.putExtra("sender", sender);
            goToLoggedInMultiplayerGameResult.putExtra("senderName", senderName);
            goToLoggedInMultiplayerGameResult.putExtra("receiver", receiver);
            goToLoggedInMultiplayerGameResult.putExtra("receiverName", receiverName);
            goToLoggedInMultiplayerGameResult.putExtra("game_id", game_id);
            goToLoggedInMultiplayerGameResult.putExtra("senderUri", senderUri);
            goToLoggedInMultiplayerGameResult.putExtra("receiverUri", receiverUri);
            goToLoggedInMultiplayerGameResult.putExtra("winner", found);
            goToLoggedInMultiplayerGameResult.putExtra("gamePlayedFor", gamePlayedFor);
            startActivity(goToLoggedInMultiplayerGameResult);
        }
    }

    private void checkoffdiag()
    {
        String found="";
        int i=2;
        if ((Long.parseLong(positionList.get(i))==Long.parseLong(positionList.get(i+2)) && (Long.parseLong(positionList.get(i+2))==Long.parseLong(positionList.get(i+4))) && Long.parseLong(positionList.get(i))!=2))
        {
            gameover=1;
            win=1;
            if(Long.parseLong(positionList.get(i))==0)
            {
                found=senderName;
            }
            else
            {
                found=receiverName;
            }
        }

        if(!found.equals(""))
        {
            registration4.remove();
            registration1.remove();
            registration3.remove();
            registration2.remove();

            Intent goToLoggedInMultiplayerGameResult = new Intent(getApplicationContext(), LoggedInMultiPlayerResult.class);
            goToLoggedInMultiplayerGameResult.putExtra("sender", sender);
            goToLoggedInMultiplayerGameResult.putExtra("senderName", senderName);
            goToLoggedInMultiplayerGameResult.putExtra("receiver", receiver);
            goToLoggedInMultiplayerGameResult.putExtra("receiverName", receiverName);
            goToLoggedInMultiplayerGameResult.putExtra("game_id", game_id);
            goToLoggedInMultiplayerGameResult.putExtra("senderUri", senderUri);
            goToLoggedInMultiplayerGameResult.putExtra("receiverUri", receiverUri);
            goToLoggedInMultiplayerGameResult.putExtra("winner", found);
            goToLoggedInMultiplayerGameResult.putExtra("gamePlayedFor", gamePlayedFor);
            startActivity(goToLoggedInMultiplayerGameResult);
        }

    }

    private void checkdraw()
    {
        int i, count=0;
        String draw="";
        for(i=0;i<9;i+=1)
        {
            if(Long.parseLong(positionList.get(i))!=2)
            {
                count+=1;
            }
        }
        if(count==9 && win==0)
        {
            draw="draw";
            gameover=1;
        }

        if(!draw.equals(""))
        {
            registration4.remove();
            registration1.remove();
            registration3.remove();
            registration2.remove();

            Intent goToLoggedInMultiplayerGameResult = new Intent(getApplicationContext(), LoggedInMultiPlayerResult.class);
            goToLoggedInMultiplayerGameResult.putExtra("sender", sender);
            goToLoggedInMultiplayerGameResult.putExtra("senderName", senderName);
            goToLoggedInMultiplayerGameResult.putExtra("receiver", receiver);
            goToLoggedInMultiplayerGameResult.putExtra("receiverName", receiverName);
            goToLoggedInMultiplayerGameResult.putExtra("game_id", game_id);
            goToLoggedInMultiplayerGameResult.putExtra("senderUri", senderUri);
            goToLoggedInMultiplayerGameResult.putExtra("receiverUri", receiverUri);
            goToLoggedInMultiplayerGameResult.putExtra("winner", "draw");
            goToLoggedInMultiplayerGameResult.putExtra("gamePlayedFor", gamePlayedFor);
            startActivity(goToLoggedInMultiplayerGameResult);
        }

    }

    public void drop (View view)
    {
        if(bothJoined)
        {
            if(( (Long.parseLong(playerTurn)==0 && currentUser.getUid().equals(sender) )|| (Long.parseLong(playerTurn)==1 && currentUser.getUid().equals(receiver)) ) )
           {

           ImageView counter = (ImageView) view;
           tappedcounter = Integer.parseInt(counter.getTag().toString());

            if (Long.parseLong(positionList.get(tappedcounter))==2)
            {
                updateTap.update("tappedPosition",tappedcounter);
            }
        }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_multiplayer);

            Intent fromMultiPlayerInfo = getIntent();
            sender = fromMultiPlayerInfo.getStringExtra("sender");
            senderName = fromMultiPlayerInfo.getStringExtra("senderName");
            receiver = fromMultiPlayerInfo.getStringExtra("receiver");
            receiverName = fromMultiPlayerInfo.getStringExtra("receiverName");
            game_id = fromMultiPlayerInfo.getStringExtra("game_id");
            senderUri = fromMultiPlayerInfo.getStringExtra("senderUri");
            receiverUri = fromMultiPlayerInfo.getStringExtra("receiverUri");


        frbAuth = FirebaseAuth.getInstance();
        currentUser = frbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        updateTap=db.collection("MultiplayerGames").document(game_id);
        multiplayerTappedPosition = db.collection("MultiplayerGames").document(game_id).collection("tappedPosition").document(game_id);
        multiplayerPositions=db.collection("MultiplayerGames").document(game_id).collection("Positions").document(game_id);
        multiplayerTurn= db.collection("MultiplayerGames").document(game_id).collection("turn").document(game_id);
        senderHasJoined=db.collection("MultiplayerGames").document(game_id);
        receiverHasJoined=db.collection("MultiplayerGames").document(game_id);

        if(currentUser.getUid().equals(sender))
        {
            senderHasJoined.update("senderJoined",FieldValue.increment(1));
        }
        if(currentUser.getUid().equals(receiver))
        {
            receiverHasJoined.update("receiverJoined",FieldValue.increment(1));
        }


        registration1= db.collection("MultiplayerGames").document(game_id).collection("turn").document(game_id).
                addSnapshotListener(LoggedInMultiplayer.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {

                            Map<String, Object> userDetails = snapshot.getData();

                            playerTurn = userDetails.get("turn").toString();


                        } else {

                        }
                    }
                });

        registration2=db.collection("MultiplayerGames").document(game_id).
                addSnapshotListener(LoggedInMultiplayer.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {

                            Map<String, Object> gamePositions = snapshot.getData();

                            tap=gamePositions.get("tappedPosition").toString();
                            senderJoined=gamePositions.get("senderJoined").toString();
                            receiverJoined=gamePositions.get("receiverJoined").toString();

                            gamePlayedFor=gamePositions.get("gamePlayedFor").toString();

                            if(Long.parseLong(senderJoined)==1 && Long.parseLong(receiverJoined)==1)
                            {
                                bothJoined=true;
                                if(Long.parseLong(tap)!=-1)
                                {
                                    gameStart=true;
                                    if(Long.parseLong(playerTurn)==0 && currentUser.getUid().equals(sender))
                                    {
                                        multiplayerPositions.update(tap.toString(), FieldValue.increment(-2));
                                    }
                                    if(Long.parseLong(playerTurn)==1 && currentUser.getUid().equals(receiver))
                                    {
                                        multiplayerPositions.update(tap.toString(), FieldValue.increment(-1));
                                    }
                                }
                            }

                            gameStatus=gamePositions.get("gameStatus").toString();

                            if(Long.parseLong(gameStatus)==1)
                            {
                                Toast.makeText(LoggedInMultiplayer.this,senderName +" cancelled the game",Toast.LENGTH_LONG).show();

                                Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(goToMainActivity );
                            }

                        } else {

                        }
                    }
                });

        image0=findViewById(R.id.image0);
        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        image4=findViewById(R.id.image4);
        image5=findViewById(R.id.image5);
        image6=findViewById(R.id.image6);
        image7=findViewById(R.id.image7);
        image8=findViewById(R.id.image8);

        playerNameSymbol=findViewById(R.id.symbolNameText);

        playerNameSymbol.setText("\n"+senderName.toString() + " V/S "+receiverName.toString());
        playerNameSymbol.setTextColor(getResources().getColor(R.color.black));

        registration3=db.collection("MultiplayerGames").document(game_id).collection("Positions").document(game_id).
                addSnapshotListener(LoggedInMultiplayer.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {

                            positionList=new ArrayList<String>();

                            Map<String, Object> gamePositions = snapshot.getData();

                            for(int i=0;i<9;i+=1)
                            {
                                positionList.add(gamePositions.get(Integer.toString(i)).toString());
                            }

                            if(Long.parseLong(senderJoined)==1 && Long.parseLong(receiverJoined)==1)
                            {
                                if(gameStart)
                                {
                                    if(Long.parseLong(tap)!=-1)
                                    {
                                        multiplayerTappedPosition.update("tappedPosition", Long.parseLong(tap));
                                    }
                                }
                            }

                        } else {

                        }
                    }
                });

        registration4=  db.collection("MultiplayerGames").document(game_id).collection("tappedPosition").document(game_id).
                addSnapshotListener(LoggedInMultiplayer.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {

                            Map<String, Object> userDetails = snapshot.getData();
                            tappedPosition = userDetails.get("tappedPosition").toString();

                            if(Long.parseLong(tappedPosition)==0)
                            {

                                if(Long.parseLong(playerTurn)==0 )
                                {
                                    if(!senderUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(senderUri.toString()).into(image0);
                                    }
                                    else
                                    {
                                        image0.setImageResource(R.drawable.x);
                                    }
                                }
                                else// if(Long.parseLong(playerTurn)==0 /*&& currentUser.getUid().equals(receiver)*/)
                                {
                                    if(!receiverUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(receiverUri.toString()).into(image0);
                                    }
                                    else
                                    {
                                        image0.setImageResource(R.drawable.o);
                                    }

                                }

                            }
                            if(Long.parseLong(tappedPosition)==1)
                            {
                                if(Long.parseLong(playerTurn)==0 )
                                {
                                    if(!senderUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(senderUri.toString()).into(image1);
                                    }
                                    else
                                    {
                                        image1.setImageResource(R.drawable.x);
                                    }
                                }
                                else
                                {
                                    if(!receiverUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(receiverUri.toString()).into(image1);
                                    }
                                    else
                                    {
                                        image1.setImageResource(R.drawable.o);
                                    }
                                }

                            }
                            if(Long.parseLong(tappedPosition)==2)
                            {
                                if(Long.parseLong(playerTurn)==0)
                                {
                                    if(!senderUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(senderUri.toString()).into(image2);
                                    }
                                    else
                                    {
                                        image2.setImageResource(R.drawable.x);
                                    }
                                }
                                else
                                {
                                    if(!receiverUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(receiverUri.toString()).into(image2);
                                    }
                                    else
                                    {
                                        image2.setImageResource(R.drawable.o);
                                    }
                                }
                            }

                            if(Long.parseLong(tappedPosition)==3)
                            {
                                if(Long.parseLong(playerTurn)==0)
                                {
                                    if(!senderUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(senderUri.toString()).into(image3);
                                    }
                                    else
                                    {
                                        image3.setImageResource(R.drawable.x);
                                    }
                                }
                                else
                                {
                                    if(!receiverUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(receiverUri.toString()).into(image3);
                                    }
                                    else
                                    {
                                        image3.setImageResource(R.drawable.o);
                                    }
                                }
                            }
                            if(Long.parseLong(tappedPosition)==4)
                            {
                                if(Long.parseLong(playerTurn)==0)
                                {
                                    if(!senderUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(senderUri.toString()).into(image4);
                                    }
                                    else
                                    {
                                        image4.setImageResource(R.drawable.x);
                                    }
                                }
                                else
                                {
                                    if(!receiverUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(receiverUri.toString()).into(image4);
                                    }
                                    else
                                    {
                                        image4.setImageResource(R.drawable.o);
                                    }
                                }
                            }
                            if(Long.parseLong(tappedPosition)==5)
                            {
                                if(Long.parseLong(playerTurn)==0)
                                {
                                    if(!senderUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(senderUri.toString()).into(image5);
                                    }
                                    else
                                    {
                                        image5.setImageResource(R.drawable.x);
                                    }
                                }
                                else
                                {
                                    if(!receiverUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(receiverUri.toString()).into(image5);
                                    }
                                    else
                                    {
                                        image5.setImageResource(R.drawable.o);
                                    }
                                }
                            }
                            if(Long.parseLong(tappedPosition)==6)
                            {
                                if(Long.parseLong(playerTurn)==0)
                                {
                                    if(!senderUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(senderUri.toString()).into(image6);
                                    }
                                    else
                                    {
                                        image6.setImageResource(R.drawable.x);
                                    }
                                }
                                else
                                {
                                    if(!receiverUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(receiverUri.toString()).into(image6);
                                    }
                                    else
                                    {
                                        image6.setImageResource(R.drawable.o);
                                    }
                                }
                            }
                            if(Long.parseLong(tappedPosition)==7)
                            {
                                if(Long.parseLong(playerTurn)==0)
                                {
                                    if(!senderUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(senderUri.toString()).into(image7);
                                    }
                                    else
                                    {
                                        image7.setImageResource(R.drawable.x);
                                    }
                                }
                                else
                                {
                                    if(!receiverUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(receiverUri.toString()).into(image7);
                                    }
                                    else
                                    {
                                        image7.setImageResource(R.drawable.o);
                                    }
                                }
                            }
                            if(Long.parseLong(tappedPosition)==8)
                            {
                                if(Long.parseLong(playerTurn)==0)
                                {
                                    if(!senderUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(senderUri.toString()).into(image8);
                                    }
                                    else
                                    {
                                        image8.setImageResource(R.drawable.x);
                                    }
                                }
                                else
                                {
                                    if(!receiverUri.equals("noPic"))
                                    {
                                        Glide.with(getApplicationContext()).load(receiverUri.toString()).into(image8);
                                    }
                                    else
                                    {
                                        image8.setImageResource(R.drawable.o);
                                    }
                                }
                            }

                            if(Long.parseLong(senderJoined)==1 && Long.parseLong(receiverJoined)==1)
                            {
                                if(Long.parseLong(tappedPosition)!=-1)
                                {
                                    if(Long.parseLong(playerTurn)==0 && currentUser.getUid().equals(sender))
                                    {
                                        multiplayerTurn.update("turn",FieldValue.increment(1));

                                    //    Toast.makeText(LoggedInMultiplayer.this,"turn is sender "+playerTurn,Toast.LENGTH_LONG).show();
                                    }
                                    if(Long.parseLong(playerTurn)==1 && currentUser.getUid().equals(receiver))
                                    {
                                        multiplayerTurn.update("turn",FieldValue.increment(-1));
                                    //    Toast.makeText(LoggedInMultiplayer.this,"turn is rec "+playerTurn,Toast.LENGTH_LONG).show();
                                    }
                                }

                                if(Long.parseLong(tappedPosition)!=-1)
                                {
                                    checkrow();
                                    checkcol();
                                    checkmaindiag();
                                    checkoffdiag();
                                    checkdraw();
                                }
                            }

                        } else {

                        }
                    }
                });

    }

    /*@Override
    public void onStop()
    {
        super.onStop();

        registration4.remove();
        registration1.remove();
        registration3.remove();
        registration2.remove();

    }*/

   /* @Override
    public void onDestroy()
    {
        super.onDestroy();

        registration4.remove();
        registration3.remove();
        registration2.remove();
        registration1.remove();
    }*/

    @Override //to prevent going back when back button is clicked
    public void onBackPressed()
    {
        Toast.makeText(this,"Multiplayer game cannot be terminated in between",Toast.LENGTH_LONG).show();
    }
}