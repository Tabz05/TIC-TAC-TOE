package com.tabish.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoggedInSingleResult extends AppCompatActivity {

    private String winner,choice;

    private TextView singleResult;

    public void LoggedInSinglePlayAgain(View view)
    {
        Intent goToSinglePlayer = new Intent (getApplicationContext(),LoggedInSinglePlayer.class);
        goToSinglePlayer.putExtra("choice", choice.toString());
        startActivity(goToSinglePlayer);
    }

    public void LoggedInSingleHome (View view)
    {
        Intent goToMain = new Intent (getApplicationContext(),MainActivity.class);
        startActivity(goToMain);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_single_result);

        Intent fromLoggedInSinglePlayer = getIntent();

        winner = fromLoggedInSinglePlayer.getStringExtra("winner");
        choice=fromLoggedInSinglePlayer.getStringExtra("choice");

        singleResult=findViewById(R.id.LoggedInSingleResult);
        singleResult.setText(winner);
        singleResult.setTextColor(getResources().getColor(R.color.blue));
    }
}