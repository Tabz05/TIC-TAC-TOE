package com.tabish.tictactoe;

import androidx.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserList extends AppCompatActivity {

    private FirebaseAuth frbAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private ListView listViewUser;

    private String currUsername;

    private String user_id;
    private String username;
    private ArrayList<String> usersList;

    private ArrayAdapter<String> arrayAdapter;

    private Timestamp sentTimeStamp;

    private String invite_id;

    private int user_id_compare;

    private void getUserList()
    {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                username=(String) document.get("username");

                                if(!username.equals(currUsername))
                                {
                                    usersList.add(username);
                                }
                            }
                            arrayAdapter.notifyDataSetChanged();
                        } else {
                            //Log.i("Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        frbAuth = FirebaseAuth.getInstance();
        currentUser = frbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        listViewUser=findViewById(R.id.listViewUser);

        usersList= new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,usersList);

        listViewUser.setAdapter(arrayAdapter);

        DocumentReference DocRef = db.collection("users").document(currentUser.getUid());

        DocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        currUsername = (String) document.get("username");

                        getUserList();

                    } else {
                        //   Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() { //when an item of listview is clicked
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                db.collection("users")
                        .whereEqualTo("username", usersList.get(i).toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        sentTimeStamp = new Timestamp(new Date());
                                        user_id=(String) document.getId();

                                        Map<String, Object> newInvite = new HashMap<>();
                                        newInvite.put("sender", currentUser.getUid());
                                        newInvite.put("senderName", currUsername);
                                        newInvite.put("receiver",user_id);
                                        newInvite.put("receiverName",usersList.get(i).toString());
                                        newInvite.put("sentOn",sentTimeStamp);
                                        newInvite.put("status","unaccepted");

                                        user_id_compare = currentUser.getUid().toString().compareTo(user_id); //comparing id of current user with id of user with whom chat is happening

                                        if(user_id_compare<0)
                                        {
                                            invite_id = currentUser.getUid().toString()+user_id+sentTimeStamp;
                                        }
                                        else
                                        {
                                            invite_id = user_id+currentUser.getUid().toString()+sentTimeStamp;
                                        }

                                        newInvite.put("inviteId",invite_id);

                                        db.collection("invites").document(invite_id)
                                                .set(newInvite)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(UserList.this, "Invite sent", Toast.LENGTH_LONG).show();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });


                                    }
                                } else {

                                }
                            }
                        });
            }
        });

    }
}