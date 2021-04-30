package com.example.dvueltaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private Button login;
    private int statusCode = 0;
    private final String URL = "http://preskynet.dvuelta.es/api10getuserapikey";
    private final String APIKEY_ACCESS = "2c94243c0c0dc4452db4efd257d34d2f";
    Intent intent;
    String user;
    String passwordUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//Metodo para enviar los datos en una peticion Http post.
    public void postMethod() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Log.d("LOG_onResponse: ", String.valueOf(statusCode));

                if (statusCode == 200) {
                    if (errorLogin(response)) {
                        resetEditText();
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrecto.", Toast.LENGTH_SHORT).show();

                    } else {
                        //resetEditText();  Linea desactivada para no escribir de nuevo user y password al retroceder.
                        intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        intent.putExtra("clienteJson", response);
                        startActivity(intent);
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Fallo en servidor", Toast.LENGTH_SHORT).show();
                        Log.d("LOG_onErrorResponse: ", "Fallo en servidor: " + String.valueOf(statusCode));
                        resetEditText();
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
                params.put("data", "{\"user\":\"" + user + "\", \"password\":\"" + passwordUser + "\"}");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
//Metodo que comprueba que el login con el servidor es correcto.
    public boolean errorLogin(String response) {
        try {
            JSONObject jsonObj = null;
            String error = "";
            String errorMsg = "";
            jsonObj = new JSONObject(response);
            error = jsonObj.getString("status");
            errorMsg = jsonObj.getString("msg");
            if (error.equals("ERR") || error.equals("KO")) {
                Log.d("TAG-errorLogin(): ", errorMsg);
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG-errorLogin()", "Error en método errorLogin().");
        }
        return false;
    }
//Metodo para poner en blanco los EditText de "usuario" y "contraseña" y focus en "usuario".
    private void resetEditText() {
        name.setText("");
        password.setText("");
        name.requestFocus();
    }
}