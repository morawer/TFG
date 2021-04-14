package com.example.dvueltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WelcomeActivity extends AppCompatActivity {

    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        Bundle extras = getIntent().getExtras();
        String welcomeName = (String) extras.getString("name");
        welcome = (TextView) findViewById(R.id.welcomeText);
        loginWelcome(welcomeName);
    }

    public void loginWelcome(String id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://jsonplaceholder.typicode.com/users/";
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url + id, null,
                new Response.Listener<JsonObjectRequest>() {
                    @Override
                    public void onResponse(JsonObjectRequest response) {

                        welcome.setText(response.getString("name"));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                welcome.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getName(String id) throws IOException {

        StringBuilder resultado = new StringBuilder();

        URL loginApiUrl = new URL("https://jsonplaceholder.typicode.com/users/" + id);

        HttpURLConnection conexion = (HttpURLConnection) loginApiUrl.openConnection();
        conexion.setRequestMethod("GET");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        String linea = "";

        while ((linea = rd.readLine()) != null) {
            resultado.append(linea);
        }
        welcome.setText(resultado.toString());
        rd.close();
    }
}

