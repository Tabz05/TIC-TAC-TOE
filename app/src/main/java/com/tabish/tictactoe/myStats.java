package com.tabish.tictactoe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class myStats extends AppCompatActivity {

    private FirebaseAuth frbAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private PieChartView overallChart;
    private PieChartView singleChart;
    private PieChartView multiChart;

    private TextView overallGamesWon;
    private TextView overallGamesLost;
    private TextView overallGamesDraw;

    private TextView overallFirstTurnsBar;
    private TextView overallSecondTurnsBar;

    private TextView overallFirstTurnsText;
    private TextView overallSecondTurnsText;

    private TextView singleGamesWon;
    private TextView singleGamesLost;
    private TextView singleGamesDraw;

    private TextView singleFirstTurnsBar;
    private TextView singleSecondTurnsBar;

    private TextView singleFirstTurnsText;
    private TextView singleSecondTurnsText;

    private TextView multiGamesWon;
    private TextView multiGamesLost;
    private TextView multiGamesDraw;

    private TextView multiFirstTurnsBar;
    private TextView multiSecondTurnsBar;

    private TextView multiFirstTurnsText;
    private TextView multiSecondTurnsText;

    private String total_no_of_games;
    private String total_no_of_games_draw;
    private String total_no_of_games_lost;
    private String total_no_of_games_won;
    private String total_no_of_first_turns;
    private String total_no_of_second_turns;

    private String no_of_games;
    private String no_of_games_draw;
    private String no_of_games_lost;
    private String no_of_games_won;
    private String no_of_first_turns;
    private String no_of_second_turns;
    private String multiplayer_no_of_games;
    private String multiplayer_no_of_games_draw;
    private String multiplayer_no_of_games_lost;
    private String multiplayer_no_of_games_won;
    private String multiplayer_no_of_first_turns;
    private String multiplayer_no_of_second_turns;

    private int width1;
    private int width2;
    private int width3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stats);

        frbAuth = FirebaseAuth.getInstance();
        currentUser = frbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        overallChart = findViewById(R.id.overallChart);
        singleChart = findViewById(R.id.singleChart);
        multiChart = findViewById(R.id.multiChart);

        overallGamesWon=findViewById(R.id.overallGamesWon);
        overallGamesLost=findViewById(R.id.overallGamesLost);
        overallGamesDraw=findViewById(R.id.overallGamesDraw);

        overallFirstTurnsBar=findViewById(R.id.overallFirstTurnsBar);
        overallSecondTurnsBar=findViewById(R.id.overallSecondTurnsBar);

        overallFirstTurnsText=findViewById(R.id.overallFirstTurnsText);
        overallSecondTurnsText=findViewById(R.id.overallSecondTurnsText);


        singleGamesWon=findViewById(R.id.singleGamesWon);
        singleGamesLost=findViewById(R.id.singleGamesLost);
        singleGamesDraw=findViewById(R.id.singleGamesDraw);

        singleFirstTurnsBar=findViewById(R.id.singleFirstTurnsBar);
        singleSecondTurnsBar=findViewById(R.id.singleSecondTurnsBar);

        singleFirstTurnsText=findViewById(R.id.singleFirstTurnsText);
        singleSecondTurnsText=findViewById(R.id.singleSecondTurnsText);

        multiGamesWon=findViewById(R.id.multiGamesWon);
        multiGamesLost=findViewById(R.id.multiGamesLost);
        multiGamesDraw=findViewById(R.id.multiGamesDraw);

        multiFirstTurnsBar=findViewById(R.id.multiFirstTurnsBar);
        multiSecondTurnsBar=findViewById(R.id.multiSecondTurnsBar);

        multiFirstTurnsText=findViewById(R.id.multiFirstTurnsText);
        multiSecondTurnsText=findViewById(R.id.multiSecondTurnsText);

        overallChart.post(new Runnable() {
            @Override
            public void run() {

                width1 = (int) overallChart.getWidth();

                overallChart.getLayoutParams().height = width1;

                overallChart.requestLayout();

                overallChart.setVisibility(View.VISIBLE);

            }
        });

        singleChart.post(new Runnable() {
            @Override
            public void run() {

                width2 = (int) singleChart.getWidth();

                singleChart.getLayoutParams().height = width2;

                singleChart.requestLayout();

                singleChart.setVisibility(View.VISIBLE);

            }
        });

        multiChart.post(new Runnable() {
            @Override
            public void run() {

                width3 = (int) multiChart.getWidth();

                multiChart.getLayoutParams().height = width3;

                multiChart.requestLayout();

                multiChart.setVisibility(View.VISIBLE);

            }
        });

        db.collection("users").document(currentUser.getUid()).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {

                            if(currentUser!=null) //if user is logged in
                            {
                                Map<String, Object> userStats = snapshot.getData();

                                total_no_of_games = userStats.get("total_no_of_games").toString();
                                total_no_of_games_draw =userStats.get("total_no_of_games_draw").toString();
                                total_no_of_games_lost =userStats.get("total_no_of_games_lost").toString();
                                total_no_of_games_won = userStats.get("total_no_of_games_won").toString();
                                total_no_of_first_turns = userStats.get("total_no_of_first_turns").toString();
                                total_no_of_second_turns = userStats.get("total_no_of_second_turns").toString();

                                no_of_games = userStats.get("no_of_games").toString();
                                no_of_games_draw =userStats.get("no_of_games_draw").toString();
                                no_of_games_lost =userStats.get("no_of_games_lost").toString();
                                no_of_games_won = userStats.get("no_of_games_won").toString();
                                no_of_first_turns = userStats.get("no_of_first_turns").toString();
                                no_of_second_turns = userStats.get("no_of_second_turns").toString();
                                multiplayer_no_of_games = userStats.get("multiplayer_no_of_games").toString();
                                multiplayer_no_of_games_draw =userStats.get("multiplayer_no_of_games_draw").toString();
                                multiplayer_no_of_games_lost =userStats.get("multiplayer_no_of_games_lost").toString();
                                multiplayer_no_of_games_won = userStats.get("multiplayer_no_of_games_won").toString();
                                multiplayer_no_of_first_turns =userStats.get("multiplayer_no_of_first_turns").toString();
                                multiplayer_no_of_second_turns =userStats.get("multiplayer_no_of_second_turns").toString();

                                List pieData = new ArrayList<>();

                                pieData.add(new SliceValue(Float.parseFloat(total_no_of_games_won), Color.parseColor("#fcf1e3")));
                                pieData.add(new SliceValue(Float.parseFloat(total_no_of_games_lost), Color.RED));
                                pieData.add(new SliceValue(Float.parseFloat(total_no_of_games_draw), Color.MAGENTA));

                                PieChartData pieChartData1 = new PieChartData(pieData);

                                pieChartData1.setHasCenterCircle(true).setCenterText1("Overall").setCenterText1FontSize(16).setCenterText1Color(Color.parseColor("#0097A7"));
                                overallChart.setPieChartData(pieChartData1);

                                LinearLayout.LayoutParams paramFirstTurnOverall = new LinearLayout.LayoutParams(
                                        0,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        Float.parseFloat(total_no_of_first_turns)
                                );

                                overallFirstTurnsBar.setLayoutParams(paramFirstTurnOverall);

                                int totalFirstTurns=Integer.parseInt(total_no_of_first_turns);
                                overallFirstTurnsText.setText("First Turns: "+totalFirstTurns/*+"("+percentage1+"%)"*/);

                                LinearLayout.LayoutParams paramSecondTurnOverall = new LinearLayout.LayoutParams(
                                        0,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        Float.parseFloat(total_no_of_second_turns)
                                );

                                overallSecondTurnsBar.setLayoutParams(paramSecondTurnOverall);

                                int totalSecondTurns=Integer.parseInt(total_no_of_second_turns);

                                overallSecondTurnsText.setText("Second Turns: "+totalSecondTurns/*+"("+percentage2+"%)"*/);

                                int totalGamesWon = Integer.parseInt(total_no_of_games_won);
                                int totalGamesLost = Integer.parseInt(total_no_of_games_lost);
                                int totalGamesDraw = Integer.parseInt(total_no_of_games_draw);

                                overallGamesWon.setText("Games Won: "+totalGamesWon);
                                overallGamesLost.setText("Games Lost: "+totalGamesLost);
                                overallGamesDraw.setText("Games Draw: "+totalGamesDraw);


                                List pieData2 = new ArrayList<>();

                                pieData2.add(new SliceValue(Float.parseFloat(no_of_games_won), Color.parseColor("#13a849")));
                                pieData2.add(new SliceValue(Float.parseFloat(no_of_games_lost), Color.parseColor("#ffff00")));
                                pieData2.add(new SliceValue(Float.parseFloat(no_of_games_draw), Color.parseColor("#14b7c9")));

                                PieChartData pieChartData2 = new PieChartData(pieData2);

                                pieChartData2.setHasCenterCircle(true).setCenterText1("Single Player").setCenterText1FontSize(16).setCenterText1Color(Color.parseColor("#0097A7"));
                                singleChart.setPieChartData(pieChartData2);


                                LinearLayout.LayoutParams paramFirstTurnSingle = new LinearLayout.LayoutParams(
                                        0,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        Float.parseFloat(no_of_first_turns)
                                );

                                singleFirstTurnsBar.setLayoutParams(paramFirstTurnSingle);

                                int singleFirstTurns=Integer.parseInt(no_of_first_turns);
                                singleFirstTurnsText.setText("First Turns: "+singleFirstTurns/*+"("+percentage3+"%)"*/);

                                LinearLayout.LayoutParams paramSecondTurnSingle = new LinearLayout.LayoutParams(
                                        0,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        Float.parseFloat(no_of_second_turns)
                                );

                                singleSecondTurnsBar.setLayoutParams(paramSecondTurnSingle);

                                int singleSecondTurns=Integer.parseInt(no_of_second_turns);

                                singleSecondTurnsText.setText("Second Turns: "+singleSecondTurns/*+"("+percentage4+"%)"*/);

                                singleGamesWon.setText("Games Won: "+Integer.parseInt(no_of_games_won));
                                singleGamesLost.setText("Games Lost: "+Integer.parseInt(no_of_games_lost));
                                singleGamesDraw.setText("Games Draw: "+Integer.parseInt(no_of_games_draw));


                                List pieData3 = new ArrayList<>();
                                pieData3.add(new SliceValue(Float.parseFloat(multiplayer_no_of_games_won), Color.parseColor("#b8ab3d")));
                                pieData3.add(new SliceValue(Float.parseFloat(multiplayer_no_of_games_lost), Color.parseColor("#ffb13d")));
                                pieData3.add(new SliceValue(Float.parseFloat(multiplayer_no_of_games_draw), Color.parseColor("#9c6332")));

                                PieChartData pieChartData3 = new PieChartData(pieData3);

                                pieChartData3.setHasCenterCircle(true).setCenterText1("Multi Player").setCenterText1FontSize(16).setCenterText1Color(Color.parseColor("#0097A7"));
                                multiChart.setPieChartData(pieChartData3);

                                LinearLayout.LayoutParams paramFirstTurnMulti = new LinearLayout.LayoutParams(
                                        0,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        Float.parseFloat(multiplayer_no_of_first_turns)
                                );

                                multiFirstTurnsBar.setLayoutParams(paramFirstTurnMulti);

                                int multiFirstTurns=Integer.parseInt(multiplayer_no_of_first_turns);
                                multiFirstTurnsText.setText("First Turns: "+multiFirstTurns/*+"("+percentage5+"%)"*/);

                                LinearLayout.LayoutParams paramSecondTurnMulti = new LinearLayout.LayoutParams(
                                        0,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        Float.parseFloat(multiplayer_no_of_second_turns)
                                );

                                multiSecondTurnsBar.setLayoutParams(paramSecondTurnMulti);

                                int MultiSecondTurns=Integer.parseInt(multiplayer_no_of_second_turns);

                                multiSecondTurnsText.setText("Second Turns: "+MultiSecondTurns/*+"("+percentage6+"%)"*/);

                                multiGamesWon.setText("Games Won: "+Integer.parseInt(multiplayer_no_of_games_won));
                                multiGamesLost.setText("Games Lost: "+Integer.parseInt(multiplayer_no_of_games_lost));
                                multiGamesDraw.setText("Games Draw: "+Integer.parseInt(multiplayer_no_of_games_draw));
                            }

                        } else {

                        }
                    }
                });

    }
}