package com.tabish.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SingleResult extends AppCompatActivity {

    private String winner,Psymbol,Bsymbol,playerName,choice;

    private TextView singleResult;

    public void singlePlayAgain(View view)
    {
        Intent goToSinglePlayer = new Intent (getApplicationContext(),SinglePlayer.class);

        goToSinglePlayer.putExtra("playerName", playerName.toString());
        goToSinglePlayer.putExtra("playerSymbol", Psymbol.toString());
        goToSinglePlayer.putExtra("choice", choice.toString());
        startActivity(goToSinglePlayer);
    }

    public void singleHome (View view)
    {
        Intent goToMain = new Intent (getApplicationContext(),MainActivity.class);
        startActivity(goToMain);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_result);

        Intent fromSinglePlayer = getIntent();

        winner = fromSinglePlayer.getStringExtra("winner");
        Psymbol=fromSinglePlayer.getStringExtra("Psymbol");
        Bsymbol=fromSinglePlayer.getStringExtra("Bsymbol");
        playerName=fromSinglePlayer.getStringExtra("playerName");
        choice=fromSinglePlayer.getStringExtra("choice");

        singleResult=findViewById(R.id.singleResult);
        singleResult.setText(winner);
        singleResult.setTextColor(getResources().getColor(R.color.blue));

    }
}