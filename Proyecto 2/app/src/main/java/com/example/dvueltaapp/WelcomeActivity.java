package com.example.dvueltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;


public class WelcomeActivity extends AppCompatActivity {

    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        //Bundle extras = getIntent().getExtras();
        //String welcomeName = (String) extras.getString("name");
        welcome = (TextView) findViewById(R.id.welcomeText);
        // welcome.setText(welcomeName);
        try {

            HttpResponse<String> response = Unirest.post("http://preskynet.dvuelta.es/api10getuserapikey")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Cookie", "sDvuelta=fbg1kd13mk5v1l6fitff56hv64")
                    .field("apiKey", "2c94243c0c0dc4452db4efd257d34d2f")
                    .field("data", "{\"user\": \"18057187W\", \"password\": \"7Rgh9faR\"}")
                    .asString();
            welcome.setText(response.getStatus());
        } catch (Exception e) {
            welcome.setText("Problema");
        }
    }


}

