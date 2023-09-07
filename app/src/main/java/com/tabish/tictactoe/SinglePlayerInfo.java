package com.tabish.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SinglePlayerInfo extends AppCompatActivity {

    private String playerSymbol;
    private String choice;

    private EditText playerName;

    private int px=0;
    private int po=0;
    private int c1=0;
    private int c2=0;

    public void symbolSelect(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.symbolX:
                if (checked)
                    playerSymbol="X";
                px=1;
                break;
            case R.id.symbolO:
                if (checked)
                    playerSymbol="O";
                po=1;
                break;
        }
    }

    public void turnSelect(View view)
    {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.turnFirst:
                if (checked)
                    choice="FIRST";
                c1=1;
                break;
            case R.id.turnSecond:
                if (checked)
                    choice="SECOND";
                c2=1;
                break;
        }
    }

    public void playSingle (View view)
    {
        if(playerName.getText().toString().length()>0) {
            if (px==1 || po==1) {
                if(c1==1 || c2==1)
                {
                    Intent goToSinglePlayer = new Intent(getApplicationContext(), SinglePlayer.class);

                    goToSinglePlayer.putExtra("playerSymbol", playerSymbol.toString());
                    goToSinglePlayer.putExtra("playerName",playerName.getText().toString());
                    goToSinglePlayer.putExtra("choice",choice.toString());

                    startActivity(goToSinglePlayer);
                }
                else
                {
                    Toast.makeText(this, "Please select a turn choice", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Please choose a symbol", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_info);

        playerName= findViewById(R.id.playerName);
    }
}