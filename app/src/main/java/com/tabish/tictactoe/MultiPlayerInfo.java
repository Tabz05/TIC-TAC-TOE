package com.tabish.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MultiPlayerInfo extends AppCompatActivity {

    private EditText playerName1;
    private EditText playerName2;

    private String playerSymbol2;
    private String playerSymbol1;

    private int p1x=0;
    private int p1o=0;
    private int p2x=0;
    private int p2o;

    public void symbolSelect1(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.player1x:
                if (checked)
                    playerSymbol1="X";
                p1x=1;
                break;
            case R.id.player1o:
                if (checked)
                    playerSymbol1="O";
                p1o=1;
                break;
        }
    }

    public void symbolSelect2(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.player2x:
                if (checked)
                    playerSymbol2="X";
                p2x=1;
                break;
            case R.id.player2o:
                if (checked)
                    playerSymbol2="O";
                p2o=1;
                break;
        }
    }


    public void playMultiple (View view)
    {
        if(playerName1.getText().toString().length()>0 && playerName2.getText().toString().length()>0) {

            if(playerName1.getText().toString().equals(playerName2.getText().toString())) {

                Toast.makeText(this, "Players cannot have same name", Toast.LENGTH_SHORT).show();
            }
            else{

                if ((p1x == 1 || p1o == 1) && (p2x == 1 || p2o == 1)) {

                    if (playerSymbol1.toString().toUpperCase().equals(playerSymbol2.toString().toUpperCase())) {
                        Toast.makeText(this, "Players cannot have same symbol", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Intent goToMultiPlayer = new Intent(getApplicationContext(), MultiPlayer.class);

                        goToMultiPlayer.putExtra("playerSymbol1", playerSymbol1.toString());
                        goToMultiPlayer.putExtra("playerName1", playerName1.getText().toString());

                        goToMultiPlayer.putExtra("playerSymbol2", playerSymbol2.toString());
                        goToMultiPlayer.putExtra("playerName2", playerName2.getText().toString());

                        startActivity(goToMultiPlayer);
                    }

                }
                else {
                    Toast.makeText(this, "Please choose symbols for both the players", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else
        {
            Toast.makeText(this, "Please enter both the names", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player_info);

        playerName1= findViewById(R.id.playerName1);
        playerName2= findViewById(R.id.playerName2);
    }
}