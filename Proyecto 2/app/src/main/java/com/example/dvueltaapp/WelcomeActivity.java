package com.example.dvueltaapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity {

    private static final String APIKEY_ACCESS = "2c94243c0c0dc4452db4efd257d34d2f";
    private final String URL = "http://preskynet.dvuelta.es/api10getexpedients";
    private final String URL_CONTACTO = "https://www.dvuelta.es/index.php/contacto";
    private int statusCode = 0;
    private TextView welcome;
    Cliente cliente = new Cliente();
    Intent intent;
    String apiKeyUser;


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



        Toolbar mibarra= findViewById(R.id.toolBar);
        setSupportActionBar(mibarra);

        mibarra.setNavigationOnClickListener(v -> Toast.makeText(WelcomeActivity.this, "Sin servicio", Toast.LENGTH_SHORT).show());

        Bundle extras = getIntent().getExtras();
        String clienteJson = extras.getString("clienteJson");
        welcome = findViewById(R.id.welcomeText);

        Typeface typeface = getResources().getFont(R.font.hindmedium);
        welcome.setTypeface(typeface);

        leerJson(clienteJson);
        postMethod();
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
            apiKeyUser = cliente.getApiKey();

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

    //Metodo para enviar los datos en una peticion Http post.
    public void postMethod() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL, response -> {
            System.out.println(response);
            Log.d("LOG_onResponse: ", String.valueOf(statusCode));

            if (statusCode == 200) {
                if (errorExpedientes(response)) {
                    Toast.makeText(WelcomeActivity.this, "Problema al obtener los expedientes", Toast.LENGTH_SHORT).show();
                    intent = new Intent(WelcomeActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                } else {
                    leerJsonGrafico(response);
                }
            }
        },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(WelcomeActivity.this, "Fallo en servidor", Toast.LENGTH_SHORT).show();
                    Log.d("LOG_onErrorResponse: ", "Fallo en servidor: " + statusCode);
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("apiKey", APIKEY_ACCESS);
                params.put("apiKeyUser", apiKeyUser);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void leerJsonGrafico(String clienteJson) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<String> listaExp = new ArrayList<>();
        boolean coincide = false;

        try {
            JSONObject jsonObject = new JSONObject(clienteJson);
            JSONArray jsonArray = jsonObject.getJSONArray("msg");

            for (int i = 0; i < jsonArray.length(); i++) {
                coincide = false;
                String hechoDenunciado = "";
                int numHechoDenunciado = 0;
                String hecho = "";

                JSONObject json = jsonArray.getJSONObject(i);
                hechoDenunciado = json.getString("HechoDenunciado");

                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject json2 = jsonArray.getJSONObject(x);
                    hecho = json2.getString("HechoDenunciado");
                    if(hecho.equalsIgnoreCase(hechoDenunciado)){
                        numHechoDenunciado++;
                        System.out.println("Coincide" + numHechoDenunciado + hecho);
                    }
                }
                System.out.println("NUMERO: " + numHechoDenunciado + " HECHO: " + hechoDenunciado);

                for (int y = 0; y < listaExp.size(); y++) {
                    if (listaExp.get(y).equals(hechoDenunciado)){
                        coincide = true;
                        System.out.println("Entroooo!!    " + coincide + " >>>>> " + listaExp.get(y) );
                    }
                }
                listaExp.add(hechoDenunciado);
                if(coincide == false){
                    pieEntries.add(new PieEntry(numHechoDenunciado, hechoDenunciado));
                }
            }
            PieChart pieChart;
            pieChart = findViewById(R.id.graficoPastel);

            Description description = new Description();
            description.setText(jsonArray.length() + " Expedientes    ");
            description.setTextSize(16);

            pieChart.setDescription(description);
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.setVisibility(View.VISIBLE);
            pieChart.animateXY(1000, 1000);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Metodo que comprueba que el login con el servidor es correcto.
    public boolean errorExpedientes(String response) {
        try {
            JSONObject jsonObj;
            String error;
            String errorMsg;
            jsonObj = new JSONObject(response);
            error = jsonObj.getString("status");
            errorMsg = jsonObj.getString("msg");
            if (error.equals("ERR") || error.equals("KO")) {
                Log.d("TAG-errorExpedientes(): ", errorMsg);
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG-errorLogin()", "Error en método errorExpedientes().");
        }
        return false;
    }
}