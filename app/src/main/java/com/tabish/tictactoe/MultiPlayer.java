package com.tabish.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MultiPlayer extends AppCompatActivity {

    private String playerName1;
    private String playerName2;
    private String playerSymbol1;
    private String playerSymbol2;
    private String Psymbol1;
    private String Psymbol2;
    private String winner;

    private TextView playerNameSymbol;
    private CountDownTimer countDownTimer;

    private int status=0;

    private int taplocations[]={2,2,2,2,2,2,2,2,2};

    private int a,b,c,d,e;
    private int gameover;
    private int win=0;
    private int gameStart=0;

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

    public void drop (View view)
    {
        if(gameStart==1) {
            playerNameSymbol.setVisibility(View.INVISIBLE);
            ImageView counter = (ImageView) view;

            int tappedcounter = Integer.parseInt(counter.getTag().toString());

            if (taplocations[tappedcounter] == 2) {
                if (status == 0) {
                    taplocations[tappedcounter] = status;
                    status = 1;
                    if (Psymbol1.toString().equals("X")) {
                        counter.setImageResource(R.drawable.x);
                    } else {
                        counter.setImageResource(R.drawable.o);
                    }

                } else {
                    taplocations[tappedcounter] = status;
                    status = 0;
                    if (Psymbol2.toString().equals("X")) {
                        counter.setImageResource(R.drawable.x);
                    } else {
                        counter.setImageResource(R.drawable.o);
                    }

                }
            }
            a = checkrow();
            b = checkcol();
            c = checkmaindiag();
            d = checkoffdiag();
            e = checkdraw();

            if (gameover == 1 && e != 1) {
                if (a == 0 || b == 0 || c == 0 || d == 0)
                {
                    winner = playerName1.toString();
                }
                else
                {
                    winner = playerName2.toString();
                }

                Intent goToMultiResult = new Intent(getApplicationContext(), MultiResult.class);
                goToMultiResult.putExtra("winner", winner.toString() + " has won");
                goToMultiResult.putExtra("playerName1", playerName1.toString());
                goToMultiResult.putExtra("playerName2", playerName2.toString());
                goToMultiResult.putExtra("playerSymbol1", Psymbol1.toString());
                goToMultiResult.putExtra("playerSymbol2", Psymbol2.toString());
                startActivity(goToMultiResult);
            }
            if (e == 1) {

                Intent goToMultiResult = new Intent(getApplicationContext(), MultiResult.class);
                goToMultiResult.putExtra("winner", "game is a draw");
                goToMultiResult.putExtra("playerName1", playerName1.toString());
                goToMultiResult.putExtra("playerName2", playerName2.toString());
                goToMultiResult.putExtra("playerSymbol1", Psymbol1.toString());
                goToMultiResult.putExtra("playerSymbol2", Psymbol2.toString());
                startActivity(goToMultiResult);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);

        Intent fromMultiPlayerInfo = getIntent();

        playerName1 = fromMultiPlayerInfo.getStringExtra("playerName1");
        playerName2 = fromMultiPlayerInfo.getStringExtra("playerName2");
        playerSymbol1=fromMultiPlayerInfo.getStringExtra("playerSymbol1");
        playerSymbol2=fromMultiPlayerInfo.getStringExtra("playerSymbol2");

        if(playerSymbol1.toString().equals("x") || playerSymbol1.toString().equals("X"))
        {
            Psymbol1="X";
            Psymbol2="O";
        }
        else
        {
            Psymbol1="O";
            Psymbol2="X";
        }

        playerNameSymbol=findViewById(R.id.symbolNameText);

        playerNameSymbol.setText("\n"+playerName1.toString() + " has Symbol: "+ Psymbol1.toString() +"\n"+ playerName2.toString()  +" has Symbol: "+ Psymbol2.toString() + "\n" + playerName1.toString() + " begins"+"\n");
        playerNameSymbol.setTextColor(getResources().getColor(R.color.blue));

        countDownTimer = new CountDownTimer(2000,1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                playerNameSymbol.setVisibility(View.INVISIBLE);

                playerNameSymbol.setText(playerName1.toString()+",your symbol is: " +Psymbol1.toString());
                gameStart=1;
            }
        }.start();

    }
}