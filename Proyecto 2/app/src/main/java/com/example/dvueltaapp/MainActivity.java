package com.example.dvueltaapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;


public class MainActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        name = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.userPassword);
        login = (Button) findViewById(R.id.botonEntrar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                //intent.putExtra("name", response.toString());
                startActivity(intent);
            }
        });
    }

   /*private void validate() throws UnirestException {
        //Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://preskynet.dvuelta.es/api10getuserapikey")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Cookie", "sDvuelta=fbg1kd13mk5v1l6fitff56hv64")
                .field("apiKey", "2c94243c0c0dc4452db4efd257d34d2f")
                .field("data", "{\"user\": \"18057187W\", \"password\": \"7Rgh9faR\"}")
                .asString();

        name.setText((CharSequence) response);


    }*/
}