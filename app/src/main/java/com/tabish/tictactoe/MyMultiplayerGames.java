package com.tabish.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class MyMultiplayerGames extends AppCompatActivity {

    private FirebaseAuth frbAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private String sender;
    private String senderName;
    private String receiver;
    private String receiverName;
    private String game_id;
    private String senderUri;
    private String receiverUri;

    private ArrayList<String> multiList;

    private ArrayAdapter<String> arrayAdapter;

    private ListView multiplayerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_multiplayer_games);

        frbAuth = FirebaseAuth.getInstance();
        currentUser = frbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        multiplayerList=findViewById(R.id.multiplayerList);

        multiList= new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,multiList);

        multiplayerList.setAdapter(arrayAdapter);

        db.collection("MultiplayerGames")
                .whereEqualTo("sender", currentUser.getUid()).whereEqualTo("gameStatus",0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                game_id= (String) document.get("game_id");

                                multiList.add(game_id);
                            }
                            arrayAdapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });

        multiplayerList.setOnItemClickListener(new AdapterView.OnItemClickListener() { //when an item of listview is clicked
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                db.collection("MultiplayerGames")
                        .whereEqualTo("game_id",multiList.get(i).toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {





                                                sender = (String) document.get("sender");


                                                senderName = (String) document.get("senderName");

                                                receiver = (String) document.get("receiver");


                                                receiverName = (String) document.get("receiverName");



                                                game_id=(String) document.get("game_id");



                                                senderUri= (String) document.get("senderUri");



                                                receiverUri= (String) document.get("receiverUri");

                                            Intent goToLoggedInMultiplayerGame = new Intent(getApplicationContext(), LoggedInMultiplayer.class);
                                            goToLoggedInMultiplayerGame.putExtra("sender", sender);
                                            goToLoggedInMultiplayerGame.putExtra("senderName", senderName);
                                            goToLoggedInMultiplayerGame.putExtra("receiver", receiver);
                                            goToLoggedInMultiplayerGame.putExtra("receiverName", receiverName);
                                            goToLoggedInMultiplayerGame.putExtra("game_id", game_id);
                                            goToLoggedInMultiplayerGame.putExtra("senderUri",senderUri);
                                            goToLoggedInMultiplayerGame.putExtra("receiverUri",receiverUri);


                                            startActivity(goToLoggedInMultiplayerGame);

                                    }
                                } else {

                                }
                            }
                        });
            }
        });
    }
}