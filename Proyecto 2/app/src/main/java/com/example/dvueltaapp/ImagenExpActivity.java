package com.example.dvueltaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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

    String apiKeyUser, idImagen, nomOrg, hechoDenunciado, matricula, puntos, fechaExp, estadoExp, numExp;
    Imagenes imagenes;
    ArrayList<ImagenesBase64> imagenesBase64ArrayList;
    ImagenesBase64 imagenesBase64;
    int statusCode = 0;
    TextView nomOrgExpText, hechoExpText, matriculaExpText, puntosExpText, fechaExpText, estadoExpText, numExpText;
    ImageView baseImagen;
    GridView gridViewImagenes;

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

        Toolbar toolBarImagenExp = (Toolbar) findViewById(R.id.toolBarImagenExp);
        setSupportActionBar(toolBarImagenExp);

        imagenes = new Imagenes();

        Bundle extras = getIntent().getExtras();
        apiKeyUser = (String) extras.getString("apiKeyUser");
        idImagen = (String) extras.getString("idImagen");
        nomOrg = (String) extras.getString("nombreOrg");
        hechoDenunciado = (String) extras.getString("hechoDenunciado");
        matricula = (String) extras.getString("matricula");
        puntos = (String) extras.getString("puntos");
        fechaExp = (String) extras.getString("fecha");
        estadoExp  = (String) extras.getString("estado");
        numExp = (String) extras.getString("numExp");

        imagenes.setApiKeyUser(apiKeyUser);
        imagenes.setIdImagen(idImagen);

        nomOrgExpText = (TextView)findViewById(R.id.nomOrgExp);
        hechoExpText = (TextView)findViewById(R.id.hechoExp);
        matriculaExpText = (TextView)findViewById(R.id.matriculaExp);
        puntosExpText = (TextView)findViewById(R.id.puntosExp);
        fechaExpText = (TextView)findViewById(R.id.fechaExp);
        estadoExpText = (TextView)findViewById(R.id.expedienteEstadoExp);
        numExpText = (TextView)findViewById(R.id.numExpImg);

        nomOrgExpText.setText("Lugar: " + nomOrg);
        hechoExpText.setText("Denuncia: " + hechoDenunciado);
        matriculaExpText.setText("Matrícula: " + matricula);
        puntosExpText.setText("Puntos: " + puntos);
        fechaExpText.setText("Fecha: " + fechaExp.substring(0, 10));
        estadoExpText.setText("Estado de trámite: " + estadoExp);

        Typeface typeface = getResources().getFont(R.font.hindmedium);
        numExpText.setTypeface(typeface);
        numExpText.setText("Expediente: " + numExp);

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
            imagenesBase64ArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                imagenesBase64 = new ImagenesBase64();

                imagenesBase64.setNombre(json.getString("Name"));
                imagenesBase64.setBase64(json.getString("Image"));

                imagenesBase64ArrayList.add(imagenesBase64);
            }
            imagenes.setImagenesBase64(imagenesBase64ArrayList);

            ImagenExpAdapter itemsImagenesBase64 = new ImagenExpAdapter(ImagenExpActivity.this, imagenesBase64ArrayList);
            gridViewImagenes = (GridView)findViewById(R.id.gridImg);
            gridViewImagenes.setAdapter(itemsImagenesBase64);

            System.out.println("El tamaño del array es de: " + imagenesBase64ArrayList.size());
            Log.d("TAG-leerJson()", "Lectura correcta Json.");

            //decodeBase64();

            gridViewImagenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(ImagenExpActivity.this, imagenesBase64ArrayList.get(position).getNombre(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG-leerJson()_ERROR", "Error de lectura Json.");
        }
    }

    public void decodeBase64() {
        byte[] imageBytes = Base64.decode(imagenesBase64.getBase64(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        baseImagen.setImageBitmap(decodedImage);
    }
}