package com.example.dvueltaapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String DVUELTA_PUSH = "0001";
    private EditText name;
    private EditText password;
    private int statusCode = 0;
    private final String URL = "http://preskynet.dvuelta.es/api10getuserapikey"; //URL acceso a API
    private final String APIKEY_ACCESS = "2c94243c0c0dc4452db4efd257d34d2f"; //Token de acceso ApiMobile
    Intent intent;
    String user;
    String passwordUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.userName);
        password = findViewById(R.id.userPassword);
        Button login = findViewById(R.id.botonEntrar);
        user = name.getText().toString();
        passwordUser = password.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, DVUELTA_PUSH)
                .setSmallIcon(R.drawable.logo_dvuelta_push)
                .setContentTitle("Nueva multa encontrada.")
                .setContentText("Acceda para obtener mas información.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                //.setStyle(new NotificationCompat.BigTextStyle()
                        //.bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0001, builder.build());

        login.setOnClickListener(v -> {
            user = name.getText().toString();
            passwordUser = password.getText().toString();
            postMethod();
        });
    }
//Metodo para enviar los datos en una peticion Http post.
    public void postMethod() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL, response -> {
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
        },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this, "Fallo en servidor", Toast.LENGTH_SHORT).show();
                    Log.d("LOG_onErrorResponse: ", "Fallo en servidor: " + statusCode);
                    resetEditText();
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
            JSONObject jsonObj;
            String error;
            String errorMsg;
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
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(DVUELTA_PUSH, "PUSH DVUELTA", importance);
            channel.setDescription("Notificaciones DVUELTA");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}