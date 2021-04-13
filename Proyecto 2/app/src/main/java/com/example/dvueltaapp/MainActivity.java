package com.example.dvueltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                validate(name.getText().toString(), password.getText().toString());
            }
        });
    }

    private void validate(String userName, String userPassword){
        if(userPassword.equals("1234")){
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.putExtra("name", userName);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Usuario o contraseña invalido.", Toast.LENGTH_SHORT).show();
        }
    }
}