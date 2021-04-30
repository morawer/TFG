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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImagenExpActivity extends AppCompatActivity {

    private final String APIKEY_ACCESS = "2c94243c0c0dc4452db4efd257d34d2f";
    private final String URL = "http://preskynet.dvuelta.es/api10getexpedientsimage";

    String apiKeyUser;
    String idImagen;
    Imagen imagen;
    int statusCode = 0;
    TextView idFotoText;
    TextView nombreFotoText;
    TextView baseImagen;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_imagen_exp, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_inicio:
            case R.id.menu_envio:
            case R.id.menu_ayuda:
                Toast.makeText(ImagenExpActivity.this, "Sin servicio", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_salir:
                Intent salida = new Intent(ImagenExpActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_imagen_exp);

        Toolbar toolBarImagenExp= (Toolbar) findViewById(R.id.toolBarImagenExp);
        setSupportActionBar(toolBarImagenExp);

        imagen = new Imagen();

        Bundle extras = getIntent().getExtras();
        apiKeyUser = (String) extras.getString("apiKeyUser");
        idImagen = (String) extras.getString("idImagen");

        imagen.setApiKeyUser(apiKeyUser);
        imagen.setIdImagen(idImagen);

        idFotoText = (TextView) findViewById(R.id.idfoto);
        nombreFotoText = (TextView) findViewById(R.id.nombrefoto);
        baseImagen = (TextView) findViewById(R.id.imagenBase64);

        postMethod();
    }

    public void postMethod() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Log.d("LOG_onResponse: ", String.valueOf(statusCode));

                if (statusCode == 200) {
                    if (errorImagenExp(response)) {
                        Toast.makeText(ImagenExpActivity.this, "Problema al obtener las imagenes", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ImagenExpActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    } else {
                        leerJson(response);
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ImagenExpActivity.this, "Fallo en servidor", Toast.LENGTH_SHORT).show();
                        Log.d("LOG_onErrorResponse: ", "Fallo en servidor: " + String.valueOf(statusCode));
                    }
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
                params.put("expedientID", idImagen);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    //Metodo que comprueba que el login con el servidor es correcto.
    public boolean errorImagenExp(String response) {
        try {
            JSONObject jsonObj = null;
            String error = "";
            String errorMsg = "";
            jsonObj = new JSONObject(response);
            error = jsonObj.getString("status");
            errorMsg = jsonObj.getString("msg");
            if (error.equals("ERR") || error.equals("KO")) {
                Log.d("TAG-errorExpedientes(): ", errorMsg);
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG-errorLogin()", "Error en método errorImagenExp().");
        }
        return false;
    }

    public void leerJson(String clienteJson) {

        try {
            JSONObject jsonObject = new JSONObject(clienteJson);
            JSONArray jsonArray = jsonObject.getJSONArray("msg");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);

                imagen.setNombre(json.getString("Name"));
                imagen.setBase64(json.getString("Image"));
            }
            Log.d("TAG-leerJson()", "Lectura correcta Json.");

            idFotoText.setText(imagen.getIdImagen());
            nombreFotoText.setText(imagen.getNombre());
            String baseImg = imagen.getBase64();
            baseImagen.setText(baseImg.substring(0,15) + "...");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG-leerJson()_ERROR", "Error de lectura Json.");
        }
    }
}