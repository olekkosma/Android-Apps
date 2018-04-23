package com.example.ukleja.connect3;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int activePlayer = 0;
    int[] gameState= {2,2,2,2,2,2,2,2,2};
    int[][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};

boolean gameIsActive = true;

    public void playAgainClick(View view){
        gameIsActive=true;
        LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
        layout.animate().alpha(0f).setDuration(100);
        activePlayer=0;
        for(int i=0;i<9;i++){
            gameState[i]=2;
        }
        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);

        for(int i=0;i<gridLayout.getChildCount();i++){
            ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);
        }

    }
    public void dropIn(View view){

        ImageView counter = (ImageView) view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if(gameState[tappedCounter]==2 && gameIsActive) {

            counter.setTranslationY(-1000f);
            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.circlegreen);
                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.circlered);
                activePlayer = 0;

            }
            gameState[tappedCounter]=activePlayer;

        counter.animate().translationYBy(1000f).setDuration(300);
            for(int[] winningPosition : winningPositions){

                if(gameState[winningPosition[0]]==gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]]==gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]]!=2){
                    String winner;
                    gameIsActive=false;
                    if(activePlayer==0){
                        winner = "Red has won!";
                    }else{
                        winner = "Green has won!";

                    }
                    TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);
                    winnerMessage.setText(winner);
                    LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
                    layout.animate().alpha(1f).setDuration(300);

                }else{
                    boolean overGame = true;
                    for(int counterState : gameState) {
                        if (counterState == 2) {
                            overGame = false;
                        }
                    }
                        if(overGame){
                            TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);
                            winnerMessage.setText("draw");
                            LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
                            layout.animate().alpha(1f).setDuration(300);
                        }

                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
