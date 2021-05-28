package com.example.dvueltaapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import harmony.java.awt.Color;

public class WelcomeActivity extends AppCompatActivity {

    private final String URL_CONTACTO = "https://www.dvuelta.es/index.php/contacto";
    private TextView welcome;
    Cliente cliente = new Cliente();
    Intent intent;
    String apiKeyUser;
    PieChart pieChart;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //Opciones de menu desplegable
        switch (item.getItemId()) {
            case R.id.menu_expediente:
                intent = new Intent(WelcomeActivity.this, ExpedienteActivity.class);
                apiKeyUser = cliente.getApiKey();
                String nombreUser = cliente.getNombre();
                intent.putExtra("apiKeyUser", apiKeyUser);
                intent.putExtra("nombreUser", nombreUser);
                startActivity(intent);
                break;

            case R.id.menu_envio:
                Intent envio = new Intent(WelcomeActivity.this, EnvioExpActivity.class);
                apiKeyUser = cliente.getApiKey();
                envio.putExtra("apiKeyUser", apiKeyUser);
                startActivity(envio);
                break;
            case R.id.menu_ayuda:
                Uri contacto = Uri.parse(URL_CONTACTO);
                Intent i = new Intent(Intent.ACTION_VIEW, contacto);
                startActivity(i);
                break;
            case R.id.menu_salir:
                Intent salida = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(salida);
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        pieChart = findViewById(R.id.graficoPastel);

        Toolbar mibarra= findViewById(R.id.toolBar);
        setSupportActionBar(mibarra);

        mibarra.setNavigationOnClickListener(v -> Toast.makeText(WelcomeActivity.this, "Sin servicio", Toast.LENGTH_SHORT).show());

        Bundle extras = getIntent().getExtras();
        String clienteJson = extras.getString("clienteJson");
        welcome = findViewById(R.id.welcomeText);

        Typeface typeface = getResources().getFont(R.font.hindmedium);
        welcome.setTypeface(typeface);

        leerJson(clienteJson);
        crearGrafico();
    }

    private void crearGrafico() {

        Description description = new Description();
        description.setText("Gráfico de expedientes.");

        pieChart.setDescription(description);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        pieEntries.add(new PieEntry(2, "Alcoholemia"));
        pieEntries.add(new PieEntry(3, "Velocidad"));
        pieEntries.add(new PieEntry(6, "Aparcamiento"));
        pieEntries.add(new PieEntry(1, "Seguro"));




        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
    }


    //Método para leer el Json obtenido en extras de MainActivity.
    @SuppressLint("SetTextI18n")
    public void leerJson(String clienteJson) {
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(clienteJson);
            JSONObject msg = jsonObj.getJSONObject("msg");

            cliente.setNombre(msg.getString("displayName"));
            cliente.setApiKey(msg.getString("apiKey"));

            welcomeUser();

            Log.d("TAG-leerJson()", "Lectura correcta Json.");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG-leerJson()_ERROR", "Error de lectura Json.");
        }
    }
//Metodo que según la hora del dia de da un saludo personalizado.
    private void welcomeUser() {
        Date dt = new Date();
        int horas = dt.getHours();

        if (horas >= 6 && horas < 12){
            welcome.setText("Buenos días " + cliente.getNombre());
        }
        if (horas >= 12 && horas < 20){
            welcome.setText("Buenas tardes " + cliente.getNombre());
        }
        if (horas >= 20 && horas <= 23){
            welcome.setText("Buenas noches " + cliente.getNombre());
        }
        if (horas >= 0 && horas < 6){
            welcome.setText("Buenas noches " + cliente.getNombre());
        }
    }
}