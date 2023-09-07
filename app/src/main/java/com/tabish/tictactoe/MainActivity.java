package com.tabish.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth frbAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private ImageView headerImage;
    private TextView headerName;
    private TextView headerEmail;

    private TextView welcomeUser;

    private String currUsername;
    private String email;

    private String profileUri;

    private String hasProfilePic;

    private DocumentReference updateStats;

    private DrawerLayout sidebarDrawerLayout;

    private NavigationView navigationBar;

    private ActionBarDrawerToggle toggle;

    private boolean isNetworkAvailable() {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            connected = true;
        }
        else
        {
            connected = false;
        }

        return connected;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1)
        {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent goToMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goToMain);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.aboutus:

            {
                Toast.makeText(this, "About Us", Toast.LENGTH_LONG).show();
                Intent goToAboutUs = new Intent(getApplicationContext(), AboutUs.class);
                startActivity(goToAboutUs);

                break;
            }

            case R.id.help:
            {
                Toast.makeText(this, "Help", Toast.LENGTH_LONG).show();
                Intent goToHelp = new Intent(getApplicationContext(), Help.class);
                startActivity(goToHelp);

                break;
            }


            case R.id.signUp:
            {
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 1);
                } else
                {
                    if(isNetworkAvailable())
                    {
                        Toast.makeText(this, "Sign Up", Toast.LENGTH_LONG).show();
                        Intent goToSignUp = new Intent(getApplicationContext(), SignUp.class);
                        startActivity(goToSignUp);
                    }
                    else
                    {
                        Toast.makeText(this, "No internet access", Toast.LENGTH_LONG).show();
                    }

                }

                break;
            }

            case R.id.signIn:
            {
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 1);
                } else
                {
                    if(isNetworkAvailable())
                    {
                        Toast.makeText(this, "Sign In", Toast.LENGTH_LONG).show();
                        Intent goToSignIn = new Intent(getApplicationContext(), SignIn.class);
                        startActivity(goToSignIn);
                    }
                    else
                    {
                        Toast.makeText(this, "No internet access", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            }

            case R.id.signOut:
            {
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 1);
                } else {
                    if(isNetworkAvailable())
                    {
                        frbAuth.signOut();

                        Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(goToMainActivity);
                    }
                    else
                    {
                        Toast.makeText(this, "No internet access", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            }

            case R.id.forgotPassword:
            {
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 1);
                } else {
                    if(isNetworkAvailable())
                    {
                        Intent goToPasswordReset = new Intent(getApplicationContext(), PasswordReset.class);
                        startActivity(goToPasswordReset);

                    }
                    else
                    {
                        Toast.makeText(this, "No internet access", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            }

            case R.id.myStat:
            {
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 1);
                } else {
                    if(isNetworkAvailable())
                    {
                        Toast.makeText(this, "Your Stats", Toast.LENGTH_LONG).show();

                        Intent goToMyStat = new Intent(getApplicationContext(), myStats.class);
                        startActivity(goToMyStat);
                    }
                    else
                    {
                        Toast.makeText(this, "No internet access", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            }

            case R.id.myProfile:
            {
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 1);
                } else {
                    if(isNetworkAvailable())
                    {
                        Toast.makeText(this, "Your Profile", Toast.LENGTH_LONG).show();

                        Intent goToMyProfile = new Intent(getApplicationContext(), MyProfile.class);
                        startActivity(goToMyProfile);
                    }
                    else
                    {
                        Toast.makeText(this, "No internet access", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            }


            case R.id.editProfile:
            {
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 1);
                } else {
                    if(isNetworkAvailable())
                    {
                        Toast.makeText(this, "Your Profile", Toast.LENGTH_LONG).show();
                        Intent goToEditProfile = new Intent(getApplicationContext(), EditProfile.class);
                        startActivity(goToEditProfile);
                    }
                    else
                    {
                        Toast.makeText(this, "No internet access", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            }


            case R.id.invites:
            {
                //if internet permission is not granted
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                {
                    //asking for internet permission
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 1);
                } else { //internet permission is granted
                    if(isNetworkAvailable()) //connected to the internet
                    {
                        Toast.makeText(this, "Invitations", Toast.LENGTH_LONG).show();
                        //going to editProfile activity
                        //goToEditProfile is a variable of intent type
                        Intent goToInvites = new Intent(getApplicationContext(), Invites.class);
                        startActivity(goToInvites ); //going to editProfile activity
                    }
                    else //not connected to the internet
                    {
                        //toast message
                        Toast.makeText(this, "No internet access", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            }

            case R.id.findPeople:
            {
                //if internet permission is not granted
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                {
                    //asking for internet permission
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 1);
                } else { //internet permission is granted
                    if(isNetworkAvailable()) //connected to the internet
                    {
                        Toast.makeText(this, "Find People", Toast.LENGTH_LONG).show();

                        Intent goToUserList = new Intent(getApplicationContext(), UserList.class);
                        startActivity(goToUserList);
                    }
                    else //not connected to the internet
                    {
                        //toast message
                        Toast.makeText(this, "No internet access", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            }

            case R.id.resetStats:
            {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
                } else {
                    if (isNetworkAvailable()) {

                        new AlertDialog.Builder(this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Are you sure!?")
                                .setMessage("Do you want to reset your stats?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        updateStats.update("no_of_games",0);
                                        updateStats.update("no_of_games_won",0);
                                        updateStats.update("no_of_games_lost",0);
                                        updateStats.update("no_of_games_draw",0);
                                        updateStats.update("no_of_first_turns",0);
                                        updateStats.update("no_of_second_turns",0);
                                        updateStats.update("no_of_second_turns",0);
                                        updateStats.update("multiplayer_no_of_games",0);
                                        updateStats.update("multiplayer_no_of_games_won",0);
                                        updateStats.update("multiplayer_no_of_games_lost",0);
                                        updateStats.update("multiplayer_no_of_games_draw",0);
                                        updateStats.update("multiplayer_no_of_first_turns",0);
                                        updateStats.update("multiplayer_no_of_second_turns",0);

                                        Intent goToStat = new Intent(getApplicationContext(), myStats.class);
                                        startActivity(goToStat);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();

                    } else {
                        Toast.makeText(this, "No internet access", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            }
        }

        sidebarDrawerLayout.closeDrawers();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void singlePlayer(View view)
    {
        if(currentUser!=null)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
            }
            else
                {
                if (isNetworkAvailable()) {

                    Intent goToLoggedSinglePlayerInfo = new Intent(getApplicationContext(), LoggedInSinglePlayerInfo.class);
                    startActivity(goToLoggedSinglePlayerInfo);
                } else {
                    Toast.makeText(this, "Since you are logged in, You need to be connected to internet to play the game", Toast.LENGTH_LONG).show();
                }
            }
        }
        else
        {
            Intent goToSinglePlayerInfo = new Intent (getApplicationContext(),SinglePlayerInfo.class);

            startActivity(goToSinglePlayerInfo);
        }

    }

    public void multiPlayer(View view)
    {
        if(currentUser!=null)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
            } else {
                if (isNetworkAvailable())
                {
                    Toast.makeText(this, "Your multiplayer games", Toast.LENGTH_LONG).show();

                    Intent goToInvites = new Intent(getApplicationContext(), MyMultiplayerGames.class);
                    startActivity(goToInvites);
                } else
                {
                    Toast.makeText(this, "No internet access", Toast.LENGTH_LONG).show();
                }
            }
        }
        else
        {
            Intent goToInvites = new Intent(getApplicationContext(), MultiPlayerInfo.class);
            startActivity(goToInvites);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frbAuth = FirebaseAuth.getInstance();
        currentUser = frbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        sidebarDrawerLayout=findViewById(R.id.sidebar_drawer_layout);

        navigationBar=findViewById(R.id.navigationBar);

        if(currentUser!=null)
        {
            navigationBar.getMenu().clear();
            navigationBar.inflateMenu(R.menu.menu_bar_logged_in);
        }
        else
        {
            navigationBar.getMenu().clear();
            navigationBar.inflateMenu(R.menu.menu_bar);
        }

        navigationBar.setNavigationItemSelectedListener(this);

        toggle= new ActionBarDrawerToggle(MainActivity.this,sidebarDrawerLayout,R.string.open,R.string.close);

        sidebarDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        welcomeUser=findViewById(R.id.welcomeUser);

        welcomeUser=findViewById(R.id.welcomeUser);

        welcomeUser.setVisibility(View.VISIBLE);

        View hView =  navigationBar.getHeaderView(0);

        headerImage = (ImageView) hView.findViewById(R.id.headerImage);

        if(currentUser!=null)
        {
            updateStats=db.collection("users").document(currentUser.getUid());
            db.collection("users").document(currentUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            currUsername = (String) document.get("username");
                            email = (String) document.get("email");
                            welcomeUser.setText("Welcome "+currUsername);

                            hasProfilePic = (String) document.get("hasProfilePic").toString();

                            headerName=(TextView)hView.findViewById(R.id.headerTextName);
                            headerEmail=(TextView)hView.findViewById(R.id.headerTextEmail);

                            headerName.setText(currUsername);
                            headerEmail.setText(email);

                            if(hasProfilePic.equals("true"))
                            {
                                profileUri = (String) document.get("profilePicUri").toString();

                                Glide.with(getApplicationContext()).load(profileUri.toString()).into(new CustomTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        headerImage.setBackground(resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }

                                });
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
        else
        {
            ViewGroup.LayoutParams layoutParams = headerImage.getLayoutParams();
            layoutParams.height = 0;
            headerImage.setLayoutParams(layoutParams);
        }
    }
}