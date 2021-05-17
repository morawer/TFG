package com.example.dvueltaapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
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
    Button descargarPDF;
    File myFile;
    GridView gridViewImagenes;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_imagen_exp, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen_exp);

        Toolbar toolBarImagenExp = findViewById(R.id.toolBarImagenExp);
        setSupportActionBar(toolBarImagenExp);

        imagenes = new Imagenes();

        Bundle extras = getIntent().getExtras();
        apiKeyUser = extras.getString("apiKeyUser");
        idImagen = extras.getString("idImagen");
        nomOrg = extras.getString("nombreOrg");
        hechoDenunciado = extras.getString("hechoDenunciado");
        matricula = extras.getString("matricula");
        puntos = extras.getString("puntos");
        fechaExp = extras.getString("fecha");
        estadoExp = extras.getString("estado");
        numExp = extras.getString("numExp");

        imagenes.setApiKeyUser(apiKeyUser);
        imagenes.setIdImagen(idImagen);

        nomOrgExpText = findViewById(R.id.nomOrgExp);
        hechoExpText = findViewById(R.id.hechoExp);
        matriculaExpText = findViewById(R.id.matriculaExp);
        puntosExpText = findViewById(R.id.puntosExp);
        fechaExpText = findViewById(R.id.fechaExp);
        estadoExpText = findViewById(R.id.expedienteEstadoExp);
        numExpText = findViewById(R.id.numExpImg);

        nomOrgExpText.setText("Lugar: " + nomOrg);
        hechoExpText.setText("Denuncia: " + hechoDenunciado);
        matriculaExpText.setText("Matrícula: " + matricula);
        puntosExpText.setText("Puntos: " + puntos);
        fechaExpText.setText("Fecha: " + fechaExp.substring(0, 10));
        estadoExpText.setText("Estado de trámite: " + estadoExp);

        Typeface typeface = getResources().getFont(R.font.hindmedium);
        numExpText.setTypeface(typeface);
        numExpText.setText("Expediente: " + numExp);

        descargarPDF = findViewById(R.id.botonPDF);
        descargarPDF.setEnabled(false);

        postMethod();

        descargarPDF.setOnClickListener(v -> crearPDF());
    }

    //Metodo para enviar los datos en una peticion Http post.
    public void postMethod() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL, response -> {
            System.out.println(response);
            Log.d("LOG_onResponse: ", String.valueOf(statusCode));

            if (statusCode == 200) {
                if (errorImagenExp(response)) {
                    Toast.makeText(ImagenExpActivity.this, "Problema al obtener las imagenes", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ImagenExpActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                } else {
                    leerJson(response);
                    descargarPDF.setText("Descargar expediente en PDF.");
                    descargarPDF.setBackgroundResource(R.drawable.boton_redondo);
                    descargarPDF.setEnabled(true);
                }
            }
        },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(ImagenExpActivity.this, "Fallo en servidor", Toast.LENGTH_SHORT).show();
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
                params.put("expedientID", idImagen);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
    //Metodo que comprueba que el login con el servidor es correcto.
    public boolean errorImagenExp(String response) {
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
            gridViewImagenes = findViewById(R.id.gridImg);
            gridViewImagenes.setAdapter(itemsImagenesBase64);

            System.out.println("El tamaño del array es de: " + imagenesBase64ArrayList.size());
            Log.d("TAG-leerJson()", "Lectura correcta Json.");

            gridViewImagenes.setOnItemClickListener((parent, view, position, id) -> {

                Bitmap imagen = decodeImage(imagenesBase64ArrayList.get(position).getBase64());
                String nombreImagen = imagenesBase64ArrayList.get(position).getNombre();

                //guardar imagen
                Save savefile = new Save();
                String filepath = savefile.SaveImage(this, imagen , nombreImagen);

                Intent intent = new Intent().setAction(Intent.ACTION_GET_CONTENT);
                Uri path = Uri.parse(filepath);
                Toast.makeText(this, filepath, Toast.LENGTH_SHORT).show();
                intent.setDataAndType(path, "image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                Log.d("pathIMAGE", filepath);
                try {
                    startActivityForResult(Intent.createChooser(intent, ""), 1);
                } catch (ActivityNotFoundException e) {
                    Log.d("ERROR", "NO TIENES APP PARA VER LA IMAGEN");
                    Toast.makeText(this, "NO TIENES APP PARA VER LA IMAGEN", Toast.LENGTH_SHORT).show();
                }

            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG-leerJson()_ERROR", "Error de lectura Json.");
        }
    }
    //Metodo para recorrer el ArrayList de imagenes en base64, decodificarlas y guardarlas en un archivo PDF.
    public void crearPDF() {

        ActivityCompat.requestPermissions(ImagenExpActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        PdfDocument myPdfDocument = new PdfDocument();
        Log.d("CREARPDF()", "Voy a entrar al for TAMAÑO: " + imagenesBase64ArrayList.size());
        for (int i = 0; i < imagenesBase64ArrayList.size(); i++) {

            Log.d("CREARPDF()", "entre!!!");

            PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(800, 1200, i + 1).create();
            PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

            Paint myPaint = new Paint();

            String base64 = imagenesBase64ArrayList.get(i).getBase64();

            myPage.getCanvas().drawBitmap(decodeImage(base64), 10, 25, myPaint);
            myPdfDocument.finishPage(myPage);
        }

        try {
            myFile = new File(Environment.getExternalStorageDirectory(), "/Download/" + numExp + ".pdf");
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            myPdfDocument.writeTo(new FileOutputStream(myFile));
            Toast.makeText(this, "PDF creado." + myFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "ERROR!!", Toast.LENGTH_SHORT).show();
        }
        myPdfDocument.close();
        viewPdf(myFile);
    }

    public Bitmap decodeImage(String base64) {
        byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return Bitmap.createScaledBitmap(decodedImage, 750, 1150, true);
    }

    public void viewPdf(File myFile) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Uri path = Uri.parse(myFile.toString());
        intent.setDataAndType(path, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent intentPdf = Intent.createChooser(intent, "Abrir PDF con...");
        Log.d("pathPDF", myFile.getAbsolutePath());
        try {
            startActivity(intentPdf);
        } catch (ActivityNotFoundException e) {
            Log.d("ERROR", "NO TIENES APP PARA VER PDF");
            Toast.makeText(this, "NO TIENES APP PARA VER PDF", Toast.LENGTH_SHORT).show();
        }
    }
}