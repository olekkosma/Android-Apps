package com.example.ukleja.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    Button startButton;
    TextView resultTextView;
    ArrayList<Integer> answers = new ArrayList<>();
    TextView pointsTextView;
    int locationOfCorrectAnswer;
    int score = 0 ;
    int numberOfQuestions = 0;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    TextView sumTextView;
    TextView timerTextView;
    Button playAgainButton;
    RelativeLayout gameRelativeLayout;


    public void playAgain(View view){
        score = 0;
        numberOfQuestions= 0;
        timerTextView.setText("30s");
        pointsTextView.setText("0/0");
        resultTextView.setText("");
        playAgainButton.setVisibility(View.INVISIBLE);
        generateQuestion();
        button0.setEnabled(true);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);

        new CountDownTimer(30000+200, 1000) {

            @Override
            public void onTick(long millisUnitFinished){
                timerTextView.setText(String.valueOf(millisUnitFinished/1000)+"s");

            }
            @Override
            public void onFinish(){

                resultTextView.setText("Gratulations:"+Integer.toString(score)+"/"+Integer.toString(numberOfQuestions));
                timerTextView.setText("0s");
                playAgainButton.setVisibility(View.VISIBLE);

                button0.setEnabled(false);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
            }
        }.start();



    }
    public void generateQuestion(){

        Random rand = new Random();
        int a = rand.nextInt(25);
        int b = rand.nextInt(25);
        sumTextView.setText(Integer.toString(a)+" + "+Integer.toString(b));

        locationOfCorrectAnswer = rand.nextInt(4);

        int incorrectAnswer;

        for(int i=0; i<4;i++){
            if(i==locationOfCorrectAnswer){
                answers.add(a+b);

            }else{
                incorrectAnswer = rand.nextInt(49);
                while(incorrectAnswer==a+b || answers.contains(incorrectAnswer)){
                    incorrectAnswer=rand.nextInt(49);
                }
                answers.add(incorrectAnswer);
            }
        }
        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
        answers.clear();


    }
    public void chooseAnswer(View view) {
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {

            score++;
            resultTextView.setText("Correct!");

        } else {
            resultTextView.setText("Wrong!");
        }
        numberOfQuestions++;
        pointsTextView.setText(Integer.toString(score)+"/"+Integer.toString(numberOfQuestions));
        generateQuestion();
    }


    public void start(View view){
        startButton.setVisibility(View.INVISIBLE);
        gameRelativeLayout.setVisibility(RelativeLayout.VISIBLE);
        playAgain(findViewById(R.id.playAgainButton));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startButton = (Button) findViewById(R.id.startButton);
        sumTextView = (TextView)findViewById(R.id.sumTextView);
         button0 = (Button)findViewById(R.id.button);
         button1 = (Button)findViewById(R.id.button2);
         button2 = (Button)findViewById(R.id.button3);
         button3 = (Button)findViewById(R.id.button4);
        resultTextView = (TextView) findViewById(R.id.textView4);
        pointsTextView = (TextView) findViewById(R.id.pointsTextView);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        gameRelativeLayout = (RelativeLayout)findViewById(R.id.gameRelativeLayout);


    }
}
