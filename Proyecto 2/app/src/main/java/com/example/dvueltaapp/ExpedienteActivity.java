package com.example.dvueltaapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ExpedienteActivity extends AppCompatActivity {

    private final String APIKEY_ACCESS = "2c94243c0c0dc4452db4efd257d34d2f";
    private final String URL = "http://preskynet.dvuelta.es/api10getexpedients";
    private int statusCode = 0;
    Intent intent;
    Cliente cliente = new Cliente();
    Expedientes expediente;
    ArrayList<Expedientes> expedienteList;
    ListView listViewExpedientes;
    String apiKeyUser;
    TextView fecha;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_expedientes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_inicio:
                Intent inicio = new Intent(ExpedienteActivity.this, WelcomeActivity.class);
                startActivity(inicio);
                break;
            case R.id.menu_envio:
                Toast.makeText(ExpedienteActivity.this, "Sin servicio", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_ayuda:
                Toast.makeText(ExpedienteActivity.this, "Sin servicio", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_salir:
                Intent salida = new Intent(ExpedienteActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_expediente);

        Toolbar toolBarExp= (Toolbar) findViewById(R.id.toolBarExp);
        setSupportActionBar(toolBarExp);

        Bundle extras = getIntent().getExtras();
        apiKeyUser = (String) extras.getString("apiKeyUser");
        cliente.setApiKey(apiKeyUser);

        postMethod();
    }

    public void leerJson(String clienteJson) {

        try {
            JSONObject jsonObject = new JSONObject(clienteJson);
            JSONArray jsonArray = jsonObject.getJSONArray("msg");
            expedienteList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++){

                JSONObject json = jsonArray.getJSONObject(i);
                expediente = new Expedientes();

                expediente.setFechaExp(json.getString("FechaInfraccion"));
                expediente.setNroExp(json.getString("NumExp"));
                expediente.setEstadoExp(json.getString("EstadoExpediente"));
                expediente.setNomOrg(json.getString("Organismo"));
                expediente.setHechoDenunciado(json.getString("HechoDenunciado"));
                expediente.setImporte(json.getString("importe"));
                expediente.setPuntos(json.getString("Puntos"));
                expediente.setMatricula(json.getString("Matricula"));
                expediente.setIdImagen(json.getString("IdExpediente"));

                expedienteList.add(expediente);
            }
            cliente.setExpediente(expedienteList);

            ExpedienteAdapter itemsExpediente = new ExpedienteAdapter(ExpedienteActivity.this, expedienteList);
            listViewExpedientes = (ListView) findViewById(R.id.listaExpediente);
            listViewExpedientes.setAdapter(itemsExpediente);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            fecha = (TextView)findViewById(R.id.actualizacion);
            String formattedDate = df.format(c.getTime());
            fecha.setText("Ultima actualización: " + formattedDate);

            listViewExpedientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String idImagen = cliente.getExpediente().get(position).getIdImagen();
                    String apiKeyUser = cliente.getApiKey();
                    Intent intent = new Intent (ExpedienteActivity.this, ImagenExpActivity.class);
                    intent.putExtra("apiKeyUser", apiKeyUser);
                    intent.putExtra("idImagen", idImagen);
                    startActivity(intent);
                }
            });

            System.out.println(cliente);
            Log.d("TAG-leerJson-Expedientes()", "Lectura correcta Json.");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG-leerJson()_ERROR_expedientes", "Error de lectura Json.");
        }
    }

    public void postMethod() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Log.d("LOG_onResponse: ", String.valueOf(statusCode));

                if (statusCode == 200) {
                    if (errorExpedientes(response)) {
                        Toast.makeText(ExpedienteActivity.this, "Problema al obtener los expedientes", Toast.LENGTH_SHORT).show();
                        intent = new Intent(ExpedienteActivity.this, WelcomeActivity.class);
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
                        Toast.makeText(ExpedienteActivity.this, "Fallo en servidor", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    //Metodo que comprueba que el login con el servidor es correcto.
    public boolean errorExpedientes(String response) {
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
            Log.d("TAG-errorLogin()", "Error en método errorExpedientes().");
        }
        return false;
    }

}