package com.example.dvueltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class WelcomeActivity extends AppCompatActivity {

    private TextView welcome;
    private int mStatusCode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        //Bundle extras = getIntent().getExtras();
        //String welcomeName = (String) extras.getString("name");
        welcome = (TextView) findViewById(R.id.welcomeText);
        postMethod();
    }

    public void postMethod() {
        String url = "http://preskynet.dvuelta.es/api10getuserapikey";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                welcome.setText(response);
                System.out.println(response);
                Log.e("LOG_VOLLEY: ", String.valueOf(mStatusCode));
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("LOG_VOLLEY_DANI: ", error.toString());
                    }
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("apiKey", "2c94243c0c0dc4452db4efd257d34d2f");
                params.put("data", "{\"user\":\"36712897K\", \"password\":\"oDe4z2aQ\"}");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
}