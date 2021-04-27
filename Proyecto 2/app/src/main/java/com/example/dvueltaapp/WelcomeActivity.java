package com.example.dvueltaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class WelcomeActivity extends AppCompatActivity {

    private TextView welcome;
    Cliente cliente = new Cliente();
    Expedientes expediente = new Expedientes();
    private Button expedientes;
    private Button envioMulta;
    Intent intentApiUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Toolbar mibarra= (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mibarra);

        mibarra.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WelcomeActivity.this, "Sin servicio", Toast.LENGTH_SHORT).show();
            }
        });

        Bundle extras = getIntent().getExtras();
        String clienteJson = (String) extras.getString("clienteJson");
        welcome = (TextView) findViewById(R.id.welcomeText);
        expedientes = (Button) findViewById(R.id.botonExpediente);
        envioMulta = (Button) findViewById(R.id.botonEnvio);
        leerJson(clienteJson);

        expedientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentApiUser = new Intent(WelcomeActivity.this, ExpedienteActivity.class);
                String apiKeyUser = cliente.getApiKey();
                intentApiUser.putExtra("apiKeyUser", apiKeyUser);
                startActivity(intentApiUser);
            }
        });

        envioMulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent = new Intent(WelcomeActivity.this, Imagen.class);
                //startActivity(intent);
                Toast.makeText(WelcomeActivity.this, "Sin servicio.", Toast.LENGTH_SHORT).show();
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
