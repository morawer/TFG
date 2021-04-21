package com.example.dvueltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class WelcomeActivity extends AppCompatActivity {

    private TextView welcome;
    Cliente cliente = new Cliente();
    private Button expedientes;
    private Button envioMulta;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        Bundle extras = getIntent().getExtras();
        String clienteJson = (String) extras.getString("clienteJson");
        welcome = (TextView) findViewById(R.id.welcomeText);
        expedientes = (Button) findViewById(R.id.botonExpediente);
        envioMulta = (Button) findViewById(R.id.botonEnvio);
        leerJson(clienteJson);

        expedientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(WelcomeActivity.this, Expedientes.class);
                startActivity(intent);
            }
        });

        envioMulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(WelcomeActivity.this, Envios.class);
                startActivity(intent);
            }
        });
    }

    //Método para leer el Json obtenido en extras de MainActivity.
    public void leerJson(String clienteJson) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(clienteJson);
            JSONObject msg = jsonObj.getJSONObject("msg");

            cliente.setNombre(msg.getString("displayName"));
            cliente.setApiKey(msg.getString("apiKey"));

            welcome.setText("Bienvenido\n" + cliente.getNombre());

            Log.d("TAG-leerJson()", "Lectura correcta Json.");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG-leerJson()_ERROR", "Error de lectura Json.");
        }
    }
}
