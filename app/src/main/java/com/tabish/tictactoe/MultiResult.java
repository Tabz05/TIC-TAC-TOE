package com.tabish.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MultiResult extends AppCompatActivity {

    private String winner,playerName1,playerName2,playerSymbol1,playerSymbol2;

    private TextView multiResult;

    public void multiPlayAgain (View view)
    {
        Intent goToMultiPlayer = new Intent (getApplicationContext(),MultiPlayer.class);
        goToMultiPlayer.putExtra("playerName1", playerName1.toString());
        goToMultiPlayer.putExtra("playerName2", playerName2.toString());
        goToMultiPlayer.putExtra("playerSymbol1", playerSymbol1.toString());
        goToMultiPlayer.putExtra("playerSymbol2", playerSymbol2.toString());

        startActivity(goToMultiPlayer);
    }

    public void multiHome(View view)
    {
        Intent goToHome = new Intent (getApplicationContext(),MainActivity.class);
        startActivity(goToHome);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_result);

        Intent fromMultiPlayer = getIntent();
        winner = fromMultiPlayer.getStringExtra("winner");
        playerName1=fromMultiPlayer.getStringExtra("playerName1");
        playerName2=fromMultiPlayer.getStringExtra("playerName2");
        playerSymbol1=fromMultiPlayer.getStringExtra("playerSymbol1");
        playerSymbol2=fromMultiPlayer.getStringExtra("playerSymbol2");

        multiResult=findViewById(R.id.multiResult);
        multiResult.setText(winner);
        multiResult.setTextColor(getResources().getColor(R.color.blue));
    }
}