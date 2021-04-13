package com.example.dvueltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        Bundle extras = getIntent().getExtras();
        String welcomeName = (String) extras.getString("name");

        welcome = (TextView)findViewById(R.id.welcomeText);
        welcome.setText("Bienvenido " + welcomeName);
    }
}