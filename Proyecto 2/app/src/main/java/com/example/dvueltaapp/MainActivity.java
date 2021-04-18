package com.example.dvueltaapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private Button login;
    private int mStatusCode = 0;
    private final String url = "http://preskynet.dvuelta.es/api10getuserapikey";
    private final String apiKeyAcess = "2c94243c0c0dc4452db4efd257d34d2f";
    Intent intent;

    String user;
    String passwordUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        name = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.userPassword);
        login = (Button) findViewById(R.id.botonEntrar);
        user = name.getText().toString();
        passwordUser = password.getText().toString();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = name.getText().toString();
                passwordUser = password.getText().toString();
                postMethod();
            }
        });
    }

    public void postMethod() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Log.e("LOG_VOLLEY: ", String.valueOf(mStatusCode));

                if (mStatusCode == 200){
                    intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    intent.putExtra("name", response);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("LOG_VOLLEY_DANI: ", error.toString());
                    }
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("apiKey", apiKeyAcess);
                params.put("data", "{\"user\":\"" + user + "\", \"password\":\"" + passwordUser + "\"}");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
}