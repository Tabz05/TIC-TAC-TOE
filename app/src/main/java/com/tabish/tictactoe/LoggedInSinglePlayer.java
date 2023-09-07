package com.tabish.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class LoggedInSinglePlayer extends AppCompatActivity {

    private String playerName;

    private String winner;
    private String choice;
    private int ch;
    private int firstpicloc;

    private TextView symbolNameText;
    private ImageView image0;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;
    private ImageView image6;
    private ImageView image7;
    private ImageView image8;

    private int status=0;
    private int taplocations[]={2,2,2,2,2,2,2,2,2};

    private int a,b,c,d,e,z;
    private int gameover;

    private int win=0;

    private String profilePicUri;
    private Boolean hasProfilePic;

    private FirebaseAuth frbAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private DocumentReference userStats;

    private CountDownTimer countDownTimer;

    private void gameIsOver()
    {
        userStats.update("no_of_games", FieldValue.increment(1));
        userStats.update("total_no_of_games", FieldValue.increment(1));
        if(ch==1)
        {
            userStats.update("no_of_first_turns", FieldValue.increment(1));
            userStats.update("total_no_of_first_turns", FieldValue.increment(1));
        }

        if(ch==2)
        {
            userStats.update("no_of_second_turns", FieldValue.increment(1));
            userStats.update("total_no_of_second_turns", FieldValue.increment(1));
        }

        if(e==1)
        {
            winner="Game draw";
            userStats.update("no_of_games_draw", FieldValue.increment(1));
            userStats.update("total_no_of_games_draw", FieldValue.increment(1));

        }
        else
        {
            if(a==0 || b==0 || c==0 || d==0)
            {
                winner="You win";
                userStats.update("no_of_games_won", FieldValue.increment(1));
                userStats.update("total_no_of_games_won", FieldValue.increment(1));
            }
            else
            {
                winner="You lose";
                userStats.update("no_of_games_lost", FieldValue.increment(1));
                userStats.update("total_no_of_games_lost", FieldValue.increment(1));
            }
        }

        countDownTimer= new CountDownTimer(1500,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                Intent goToSingleResult = new Intent (getApplicationContext(),LoggedInSingleResult.class);
                goToSingleResult.putExtra("winner",winner.toString());
                goToSingleResult.putExtra("choice",choice.toString());

                startActivity(goToSingleResult);
            }
        }.start();
    }

    private void getUserDetails()
    {
        db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        playerName = (String) document.get("username");
                        hasProfilePic= (Boolean) document.get("hasProfilePic");

                        if(hasProfilePic)
                        {
                            profilePicUri=(String) document.get("profilePicUri");
                        }

                        if(choice.toString().toUpperCase().equals("FIRST"))
                        {
                            symbolNameText.setText(playerName+",You begin");
                            symbolNameText.setTextColor(getResources().getColor(R.color.blue));
                        }
                        if(choice.toString().toUpperCase().equals("SECOND"))
                        {
                            symbolNameText.setText(playerName+",You go second");
                            symbolNameText.setTextColor(getResources().getColor(R.color.blue));
                        }

                    } else {
                    }
                } else {

                }
            }
        });
    }

    private int getRandomNumber(int min, int max)
    {
        return (int) (Math.random()*((max-min)+1))+min;
    }


    private int checkRowBotConsecutive()
    {
        int p=10;
        int q=10;
        int r=10;
        int s=10;
        int t=10;
        int u=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==1 && taplocations[1]==1 && taplocations[2]==2)
        {
            p=2;
        }
        if(taplocations[1]==1 && taplocations[2]==1 && taplocations[0]==2)
        {
            q=0;
        }
        if(taplocations[3]==1 && taplocations[4]==1 && taplocations[5]==2)
        {
            r=5;
        }
        if(taplocations[4]==1 && taplocations[5]==1 && taplocations[3]==2)
        {
            s=3;
        }
        if(taplocations[6]==1 && taplocations[7]==1 && taplocations[8]==2)
        {
            t=8;
        }
        if(taplocations[7]==1 && taplocations[8]==1 && taplocations[6]==2)
        {
            u=6;
        }

        if(p!=10 || q!=10 || r!=10 || s!=10 || t!=10 || u!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }
            if(r!=10)
            {
                v.add(r);
            }

            if(s!=10)
            {
                v.add(s);
            }

            if(t!=10)
            {
                v.add(t);
            }

            if(u!=10)
            {
                v.add(u);
            }
            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }

        else
        {
            m=10;
        }

        return m;
    }

    private int checkColBotConsecutive()
    {
        int p=10;
        int q=10;
        int r=10;
        int s=10;
        int t=10;
        int u=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==1 && taplocations[3]==1 && taplocations[6]==2)
        {
            p=6;
        }
        if(taplocations[3]==1 && taplocations[6]==1 && taplocations[0]==2)
        {
            q=0;
        }
        if(taplocations[1]==1 && taplocations[4]==1 && taplocations[7]==2)
        {
            r=7;
        }
        if(taplocations[4]==1 && taplocations[7]==1 && taplocations[1]==2)
        {
            s=1;
        }
        if(taplocations[2]==1 && taplocations[5]==1 && taplocations[8]==2)
        {
            t=8;
        }
        if(taplocations[5]==1 && taplocations[8]==1 && taplocations[2]==2)
        {
            u=2;
        }

        if(p!=10 || q!=10 || r!=10 || s!=10 || t!=10 || u!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }
            if(r!=10)
            {
                v.add(r);
            }

            if(s!=10)
            {
                v.add(s);
            }

            if(t!=10)
            {
                v.add(t);
            }

            if(u!=10)
            {
                v.add(u);
            }
            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }

        else
        {
            m=10;
        }

        return m;
    }

    private int checkDiagBotConsecutive()
    {
        int p=10;
        int q=10;
        int r=10;
        int s=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==1 && taplocations[4]==1 && taplocations[8]==2)
        {
            p=8;
        }
        if(taplocations[4]==1 && taplocations[8]==1 && taplocations[0]==2)
        {
            q=0;
        }
        if(taplocations[2]==1 && taplocations[4]==1 && taplocations[6]==2)
        {
            r=6;
        }
        if(taplocations[4]==1 && taplocations[6]==1 && taplocations[2]==2)
        {
            s=2;
        }

        if(p!=10 || q!=10 || r!=10 || s!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }
            if(r!=10)
            {
                v.add(r);
            }

            if(s!=10)
            {
                v.add(s);
            }

            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }

        else
        {
            m=10;
        }

        return m;
    }

    private int checkRowPlayerConsecutive()
    {
        int p=10;
        int q=10;
        int r=10;
        int s=10;
        int t=10;
        int u=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==0 && taplocations[1]==0 && taplocations[2]==2)
        {
            p=2;
        }
        if(taplocations[1]==0 && taplocations[2]==0 && taplocations[0]==2)
        {
            q=0;
        }
        if(taplocations[3]==0 && taplocations[4]==0 && taplocations[5]==2)
        {
            r=5;
        }
        if(taplocations[4]==0 && taplocations[5]==0 && taplocations[3]==2)
        {
            s=3;
        }
        if(taplocations[6]==0 && taplocations[7]==0 && taplocations[8]==2)
        {
            t=8;
        }
        if(taplocations[7]==0 && taplocations[8]==0 && taplocations[6]==2)
        {
            u=6;
        }

        if(p!=10 || q!=10 || r!=10 || s!=10 || t!=10 || u!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }
            if(r!=10)
            {
                v.add(r);
            }

            if(s!=10)
            {
                v.add(s);
            }

            if(t!=10)
            {
                v.add(t);
            }

            if(u!=10)
            {
                v.add(u);
            }
            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }

        else
        {
            m=10;
        }

        return m;
    }

    private int checkColPlayerConsecutive()
    {
        int p=10;
        int q=10;
        int r=10;
        int s=10;
        int t=10;
        int u=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==0 && taplocations[3]==0 && taplocations[6]==2)
        {
            p=6;
        }
        if(taplocations[3]==0 && taplocations[6]==0 && taplocations[0]==2)
        {
            q=0;
        }
        if(taplocations[1]==0 && taplocations[4]==0 && taplocations[7]==2)
        {
            r=7;
        }
        if(taplocations[4]==0 && taplocations[7]==0 && taplocations[1]==2)
        {
            s=1;
        }
        if(taplocations[2]==0 && taplocations[5]==0 && taplocations[8]==2)
        {
            t=8;
        }
        if(taplocations[5]==0 && taplocations[8]==0 && taplocations[2]==2)
        {
            u=2;
        }

        if(p!=10 || q!=10 || r!=10 || s!=10 || t!=10 || u!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }
            if(r!=10)
            {
                v.add(r);
            }

            if(s!=10)
            {
                v.add(s);
            }

            if(t!=10)
            {
                v.add(t);
            }

            if(u!=10)
            {
                v.add(u);
            }
            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }

        else
        {
            m=10;
        }

        return m;
    }

    private int checkDiagPlayerConsecutive()
    {
        int p=10;
        int q=10;
        int r=10;
        int s=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==0 && taplocations[4]==0 && taplocations[8]==2)
        {
            p=8;
        }
        if(taplocations[4]==0 && taplocations[8]==0 && taplocations[0]==2)
        {
            q=0;
        }
        if(taplocations[2]==0 && taplocations[4]==0 && taplocations[6]==2)
        {
            r=6;
        }
        if(taplocations[4]==0 && taplocations[6]==0 && taplocations[2]==2)
        {
            s=2;
        }

        if(p!=10 || q!=10 || r!=10 || s!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }
            if(r!=10)
            {
                v.add(r);
            }

            if(s!=10)
            {
                v.add(s);
            }

            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }

        else
        {
            m=10;
        }

        return m;
    }

    private int checkRowBot()
    {
        int p=10;
        int q=10;
        int r=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==1 && taplocations[2]==1 && taplocations[1]==2)
        {
            p=1;
        }
        if(taplocations[3]==1 && taplocations[5]==1 && taplocations[4]==2)
        {
            q=4;
        }
        if(taplocations[6]==1 && taplocations[8]==1 && taplocations[7]==2)
        {
            r=7;
        }

        if(p!=10 || q!=10 || r!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }
            if(r!=10)
            {
                v.add(r);
            }

            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }

        else
        {
            m=10;
        }

        return m;
    }

    private int checkColBot()
    {
        int p=10;
        int q=10;
        int r=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==1 && taplocations[6]==1 && taplocations[3]==2)
        {
            p=3;
        }
        if(taplocations[1]==1 && taplocations[7]==1 && taplocations[4]==2)
        {
            q=4;
        }
        if(taplocations[2]==1 && taplocations[8]==1 && taplocations[5]==2)
        {
            r=5;
        }

        if(p!=10 || q!=10 || r!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }
            if(r!=10)
            {
                v.add(r);
            }


            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }

        else
        {
            m=10;
        }

        return m;


    }

    private int checkDiagBot()
    {
        int p=10;
        int q=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==1 && taplocations[8]==1 && taplocations[4]==2)
        {
            p=4;
        }
        if(taplocations[2]==1 && taplocations[6]==1 && taplocations[4]==2)
        {
            q=4;
        }

        if(p!=10 || q!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }

            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }
        else
        {
            m=10;
        }

        return m;
    }

    private int checkRowPlayer()
    {
        int p=10;
        int q=10;
        int r=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==0 && taplocations[2]==0 && taplocations[1]==2)
        {
            p=1;
        }
        if(taplocations[3]==0 && taplocations[5]==0 && taplocations[4]==2)
        {
            q=4;
        }
        if(taplocations[6]==0 && taplocations[8]==0 && taplocations[7]==2)
        {
            r=7;
        }

        if(p!=10 || q!=10 || r!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }
            if(r!=10)
            {
                v.add(r);
            }

            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }

        else
        {
            m=10;
        }

        return m;
    }

    private int checkColPlayer()
    {
        int p=10;
        int q=10;
        int r=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==0 && taplocations[6]==0 && taplocations[3]==2)
        {
            p=3;
        }
        if(taplocations[1]==0 && taplocations[7]==0 && taplocations[4]==2)
        {
            q=4;
        }
        if(taplocations[2]==0 && taplocations[8]==0 && taplocations[5]==2)
        {
            r=5;
        }

        if(p!=10 || q!=10 || r!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }
            if(r!=10)
            {
                v.add(r);
            }

            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }

        else
        {
            m=10;
        }

        return m;
    }

    private int checkDiagPlayer()
    {
        int p=10;
        int q=10;

        int m=10;
        int n;

        List<Integer> v = new ArrayList<Integer>();

        if(taplocations[0]==0 && taplocations[8]==0 && taplocations[4]==2)
        {
            p=4;
        }
        if(taplocations[2]==0 && taplocations[6]==0 && taplocations[4]==2)
        {
            q=4;
        }

        if(p!=10 || q!=10)
        {
            if(p!=10)
            {
                v.add(p);
            }
            if(q!=10)
            {
                v.add(q);
            }

            n=getRandomNumber(0,v.size()-1);
            m=v.get(n);
        }

        else
        {
            m=10;
        }

        return m;
    }

    private int checkrow()
    {
        int found=2;
        int i=0;
        int j=0;
        while(j<3)
        {
            if(taplocations[i]==taplocations[i+1] && taplocations[i]==taplocations[i+2] && taplocations[i]!=2)
            {
                gameover=1;
                win=1;
                if(taplocations[i]==0)
                {
                    found=0;
                    break;
                }
                else
                {
                    found=1;
                    break;
                }
            }
            i+=3;
            j+=1;
        }
        return found;
    }

    private int checkcol()
    {
        int found=2;
        int i=0;
        int j=0;
        while(j<3)
        {
            if(taplocations[i]==taplocations[i+3] && taplocations[i]==taplocations[i+6] && taplocations[i]!=2)
            {
                gameover=1;
                win=1;
                if(taplocations[i]==0)
                {
                    found=0;
                    break;
                }
                else
                {
                    found=1;
                    break;
                }
            }
            i+=1;
            j+=1;
        }
        return found;
    }

    private int checkmaindiag()
    {
        int found=2;
        int i=0;
        if ((taplocations[i]==taplocations[i+4]) && (taplocations[i]==taplocations[i+8]) && taplocations[i]!=2)
        {
            gameover=1;
            win=1;
            if(taplocations[i]==0)
            {
                found=0;
            }
            else
            {
                found=1;
            }
        }
        return found;
    }

    private int checkoffdiag()
    {
        int found=2;
        int i=2;
        if ((taplocations[i]==taplocations[i+2]) && (taplocations[i+2]==taplocations[i+4]) && taplocations[i]!=2)
        {
            gameover=1;
            win=1;
            if(taplocations[i]==0)
            {
                found=0;
            }
            else
            {
                found=1;
            }
        }
        return found;
    }

    private int checkdraw()
    {
        int i, count=0;
        int draw=0;
        for(i=0;i<9;i+=1)
        {
            if(taplocations[i]!=2)
            {
                count+=1;
            }
        }
        if(count==9 && win==0)
        {
            draw=1;
            gameover=1;
        }
        return draw;
    }

    private void putanother()
    {
        if(gameover!=1 && status==1)
        {
            z=getRandomNumber(0,8);
            if(taplocations[z]==2)
            {
                taplocations[z]=status;
                status=0;

                if(z==0)
                {
                    image0.setImageResource(R.drawable.x);
                }
                if(z==1)
                {
                    image1.setImageResource(R.drawable.x);
                }
                if(z==2)
                {
                    image2.setImageResource(R.drawable.x);
                }
                if(z==3)
                {
                    image3.setImageResource(R.drawable.x);
                }
                if(z==4)
                {
                    image4.setImageResource(R.drawable.x);
                }
                if(z==5)
                {
                    image5.setImageResource(R.drawable.x);
                }
                if(z==6)
                {
                    image6.setImageResource(R.drawable.x);
                }
                if(z==7)
                {
                    image7.setImageResource(R.drawable.x);
                }
                if(z==8)
                {
                    image8.setImageResource(R.drawable.x);
                }

            }
            else
            {
                putanother();
            }

            a=checkrow();
            b=checkcol();
            c=checkmaindiag();
            d=checkoffdiag();
            e=checkdraw();

            if(gameover == 1)
            {
                gameIsOver();
            }
        }
    }

    private void putspecific(int m)
    {
        if(gameover!=1 && status==1) {
            status = 0;

            if (m == 0)
            {
                taplocations[0] = 1;

                image0.setImageResource(R.drawable.x);

            }

            if (m == 1)
            {
                taplocations[1] = 1;

                image1.setImageResource(R.drawable.x);

            }

            if (m == 2)
            {
                taplocations[2] = 1;

                image2.setImageResource(R.drawable.x);

            }

            if (m == 3)
            {
                taplocations[3] = 1;

                image3.setImageResource(R.drawable.x);

            }

            if (m == 4)
            {
                taplocations[4] = 1;

                image4.setImageResource(R.drawable.x);
            }

            if (m == 5)
            {
                taplocations[5] = 1;

                image5.setImageResource(R.drawable.x);

            }

            if (m == 6)
            {
                taplocations[6] = 1;

                image6.setImageResource(R.drawable.x);

            }

            if (m == 7)
            {
                taplocations[7] = 1;

                image7.setImageResource(R.drawable.x);

            }

            if (m == 8)
            {
                taplocations[8] = 1;

                image8.setImageResource(R.drawable.x);

            }

            a = checkrow();
            b = checkcol();
            c = checkmaindiag();
            d = checkoffdiag();
            e = checkdraw();

            if(gameover == 1)//game is over
            {
                gameIsOver();
            }
        }
    }

    public void drop (View view)
    {
        if(gameover != 1)
        {
            symbolNameText.setVisibility(View.INVISIBLE);

            ImageView counter = (ImageView) view;

            int tappedcounter = Integer.parseInt(counter.getTag().toString());

            if (taplocations[tappedcounter] == 2)
            {
                if (status == 0) {
                    taplocations[tappedcounter] = status;
                    status = 1;

                    if (hasProfilePic)
                    {
                     Glide.with(getApplicationContext()).load(profilePicUri.toString()).into(counter);
                    }
                    else
                    {
                        counter.setImageResource(R.drawable.profilepic);
                    }
                }

            }
            a = checkrow();
            b = checkcol();
            c = checkmaindiag();
            d = checkoffdiag();
            e = checkdraw();

            if(gameover == 1)
            {
                gameIsOver();
            } 
            else {
                int a1 = checkRowBotConsecutive();
                int b1 = checkColBotConsecutive();
                int c1 = checkDiagBotConsecutive();
                int d1 = checkRowPlayerConsecutive();
                int e1 = checkColPlayerConsecutive();
                int f1 = checkDiagPlayerConsecutive();
                int g1 = checkRowBot();
                int h1 = checkColBot();
                int i1 = checkDiagBot();
                int j1 = checkRowPlayer();
                int k1 = checkColPlayer();
                int l1 = checkDiagPlayer();

                List<Integer> singleLoc = new ArrayList<Integer>();

                if (a1 != 10 || b1 != 10 || c1 != 10 || g1 != 10 || h1 != 10 || i1 != 10) {
                    if (a1 != 10) {
                        singleLoc.add(a1);
                    }
                    if (b1 != 10) {
                        singleLoc.add(b1);
                    }
                    if (c1 != 10) {
                        singleLoc.add(c1);
                    }
                    if (g1 != 10) {
                        singleLoc.add(g1);
                    }
                    if (h1 != 10) {
                        singleLoc.add(h1);
                    }
                    if (i1 != 10) {
                        singleLoc.add(i1);
                    }
                }

                if ((d1 != 10 || e1 != 10 || f1 != 10 || j1 != 10 || k1 != 10 || l1 != 10) && (a1 == 10 && b1 == 10 && c1 == 10 && g1 == 10 && h1 == 10 && i1 == 10)) {
                    if (d1 != 10) {
                        singleLoc.add(d1);
                    }
                    if (e1 != 10) {
                        singleLoc.add(e1);
                    }
                    if (f1 != 10) {
                        singleLoc.add(f1);
                    }
                    if (j1 != 10) {
                        singleLoc.add(j1);
                    }
                    if (k1 != 10) {
                        singleLoc.add(k1);
                    }
                    if (l1 != 10) {
                        singleLoc.add(l1);
                    }
                }

                if (singleLoc.size() > 0) {
                    new CountDownTimer(1500, 1000) {
                        public void onTick(long millisecondsUntilDone) {
                        }

                        public void onFinish() {
                            int n = getRandomNumber(0, singleLoc.size() - 1);
                            int m = singleLoc.get(n);
                            putspecific(m);
                        }
                    }.start();

                } else {
                    new CountDownTimer(1500, 1000) {
                        public void onTick(long millisecondsUntilDone) {
                        }

                        public void onFinish() { //when timer finishes
                            putanother(); //calling putanother function
                        }
                    }.start();

                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_single_player);

        Intent fromLoggedInSinglePlayerInfo = getIntent();
        choice=fromLoggedInSinglePlayerInfo.getStringExtra("choice");

        frbAuth = FirebaseAuth.getInstance();
        currentUser = frbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        userStats = db.collection("users").document(currentUser.getUid());

        getUserDetails();

        symbolNameText=findViewById(R.id.symbolNameTextLoggedIn);

        image0=findViewById(R.id.image0);
        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        image4=findViewById(R.id.image4);
        image5=findViewById(R.id.image5);
        image6=findViewById(R.id.image6);
        image7=findViewById(R.id.image7);
        image8=findViewById(R.id.image8);

        if(choice.toString().toUpperCase().equals("FIRST"))
        {
            ch=1;
        }
        else
        {
            ch=2;
        }

        if(ch==2)
        {
            firstpicloc=getRandomNumber(0,8);

            if(firstpicloc==0)
            {
                image0.setImageResource(R.drawable.x);
            }
            if(firstpicloc==1)
            {
                image1.setImageResource(R.drawable.x);
            }
            if(firstpicloc==2)
            {
                image2.setImageResource(R.drawable.x);
            }
            if(firstpicloc==3)
            {
                image3.setImageResource(R.drawable.x);
            }
            if(firstpicloc==4)
            {
                image4.setImageResource(R.drawable.x);
            }
            if(firstpicloc==5)
            {
                image5.setImageResource(R.drawable.x);
            }
            if(firstpicloc==6)
            {
                image6.setImageResource(R.drawable.x);
            }
            if(firstpicloc==7)
            {
                image7.setImageResource(R.drawable.x);
            }
            if(firstpicloc==8)
            {
                image8.setImageResource(R.drawable.x);
            }
            taplocations[firstpicloc]=1;
        }


    }
}