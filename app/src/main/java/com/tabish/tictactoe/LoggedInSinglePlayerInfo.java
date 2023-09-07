package com.tabish.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class LoggedInSinglePlayerInfo extends AppCompatActivity {

    private String choice;

    private int c1=0;
    private int c2=0;

    public void turnSelectLoggedIn(View view){//when any button of radio group of id turnSelectLoggedIn is clicked.(to select user's turn)

        boolean checked = ((RadioButton) view).isChecked();//boolean variable to check if any button has been checked.

        // Checking which radio button was clicked by checking the id of radio button clicked.
        switch(view.getId()) {
            case R.id.turnFirstLoggedIn: //if radiobutton of id turnFirstLoggedIn was clicked.
                if (checked)
                    choice="FIRST";
                c1=1;
                break;
            case R.id.turnSecondLoggedIn://if radiobutton of id turnSecondLoggedIn was clicked.
                if (checked)
                    choice="SECOND";
                c2=1;
                break;
        }
    }

    public void playSingleLoggedIn (View view) //when play button is clicked
    {
                if(c1==1 || c2==1)//if turn choice has been specified
                {
                    //going to LoggedInSinglePlayer activity by passing choice, the same tag 'choice' has been used in
                    //LoggedInSinglePlayer to receive the value passed
                    Intent goToLoggedInSinglePlayer = new Intent(getApplicationContext(), LoggedInSinglePlayer.class);

                    goToLoggedInSinglePlayer.putExtra("choice",choice.toString());

                    startActivity(goToLoggedInSinglePlayer);//going to LoggedInSinglePlayer activity
                }
                else //if turn choice has not been specified
                {
                    Toast.makeText(this, "Please select a turn choice", Toast.LENGTH_SHORT).show();
                }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_single_player_info);
    }
}