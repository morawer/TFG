package com.example.dvueltaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    Intent intentApiUser;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_expediente:
                intentApiUser = new Intent(WelcomeActivity.this, ExpedienteActivity.class);
                String apiKeyUser = cliente.getApiKey();
                intentApiUser.putExtra("apiKeyUser", apiKeyUser);
                startActivity(intentApiUser);
                break;

            case R.id.menu_envio:
                Toast.makeText(WelcomeActivity.this, "Sin servicio", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_ayuda:
                Toast.makeText(WelcomeActivity.this, "Sin servicio", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_salir:
                Intent salida = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(salida);
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        leerJson(clienteJson);
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
