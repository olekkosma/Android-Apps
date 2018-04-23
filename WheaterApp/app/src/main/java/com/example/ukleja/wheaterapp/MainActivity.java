package com.example.ukleja.wheaterapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {


    EditText cityName;
    TextView resultTextView;
    public void findWeather(View view){

        if(cityName.getText().toString()!="" && cityName.getText().toString().isEmpty()==false) {
            Log.i("cityName", cityName.getText().toString());

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

            try {
                String encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
                String urlWeather = "http://api.openweathermap.org/data/2.5/weather?q=";
                urlWeather = urlWeather + encodedCityName + "&appid=87bb98ad1ddb488c1320d101cf50352c";

                DownloadTask task = new DownloadTask();
                task.execute(urlWeather);

            } catch (UnsupportedEncodingException e) {

                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();

            }
        }else{

            Toast.makeText(MainActivity.this,"Empty text field ",Toast.LENGTH_SHORT).show();

        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = (EditText) findViewById(R.id.cityName);
        resultTextView = (TextView)findViewById(R.id.resultView);


    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {

            String result ="";
            URL url;

            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data !=-1){

                    char current = (char) data;

                    result +=current;

                    data = reader.read();
                }
                return  result;

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();

            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                String message = "";
                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather content",weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                for(int i=0;i<arr.length();i++){

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = "";
                    String desc = "";

                    main = jsonPart.getString("main");
                    desc = jsonPart.getString("description");

                    Log.i("main:",jsonPart.getString("main"));
                    Log.i("desc:",jsonPart.getString("description"));
                    if(main!="" && desc != ""){
                        message +=main + ":" + desc+"\r\n";
                    }

                }
                if(message !=""){
                    resultTextView.setText(message);

                }else{
                    Toast.makeText(MainActivity.this,"test",Toast.LENGTH_LONG).show();
                   // resultTextView.setText("");
                }


            } catch (JSONException e) {
                //resultTextView.setText("");

                Toast.makeText(MainActivity.this,"test",Toast.LENGTH_LONG).show();

            }


        }
    }
}
