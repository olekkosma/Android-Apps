package com.example.ukleja.higherorlower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int guessNumber=0;
    public void onClick(View view){
        Random random = new Random();
        EditText numberText= (EditText) findViewById(R.id.numberField);
        int typedNumber = Integer.parseInt(numberText.getText().toString());


    if(typedNumber>guessNumber){
            Toast.makeText(MainActivity.this,"Lower!",Toast.LENGTH_SHORT).show();

    }else{
        if(typedNumber<guessNumber){
            Toast.makeText(MainActivity.this,"Higher!",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(MainActivity.this,"Guessed corectly, try again!",Toast.LENGTH_SHORT).show();
        guessNumber= random.nextInt(50)+1;
        }
    }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Random random = new Random();
        guessNumber = random.nextInt(49)+1;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
