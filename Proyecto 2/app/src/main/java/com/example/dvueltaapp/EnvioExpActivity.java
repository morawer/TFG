package com.example.dvueltaapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public class EnvioExpActivity extends AppCompatActivity {

    Button camaraButton, galeriaButton;
    ImageView imgView;
    private static final int PICK_IMAGE = 100, PICTURE_RESULT = 122;
    private static final String TAG = "MainActivity";
    private ContentValues values;
    private Uri imageUri;
    private Bitmap thumbnail;
    private String imageurl, encodedImage, apiKeyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio_exp);

        Bundle extras = getIntent().getExtras();
        apiKeyUser = extras.getString("apiKeyUser");

        imgView = findViewById(R.id.imagenCamara);
        camaraButton = findViewById(R.id.botonCamara);
        camaraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara();
            }
        });

        galeriaButton = findViewById(R.id.botonGaleria);
        galeriaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleriaImagenes();
            }
        });

    }

    private void abrirGaleriaImagenes() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void abrirCamara() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Check permissions for Android 6.0+
            if (!checkExternalStoragePermission()) {
                return;
            }
        }
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "MyPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        String imagenOriginal = MediaStore.ACTION_IMAGE_CAPTURE;

        Intent intent = new Intent(imagenOriginal);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PICTURE_RESULT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICTURE_RESULT:
                if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK)
                    try {
                        thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        imgView.setImageBitmap(thumbnail);
                        imageurl = getRealPathFromURI(imageUri);
                        Bitmap bmp = BitmapFactory.decodeFile(imageurl);
                        Bitmap imageScaled = Bitmap.createScaledBitmap(bmp, 1500, 2000, false);

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        imageScaled.compress(Bitmap.CompressFormat.JPEG, 40, bos);
                        byte[] byteArray = bos.toByteArray();

                        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        Toast.makeText(this, encodedImage.substring(0, 15), Toast.LENGTH_SHORT).show();
                        System.out.print(encodedImage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            case PICK_IMAGE:
                if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
                    try {
                        imageUri = data.getData();
                        imgView.setImageURI(imageUri);
                        imageurl = getRealPathFromURI(imageUri);
                        Bitmap bmp = BitmapFactory.decodeFile(imageurl);
                        Bitmap imageScaled = Bitmap.createScaledBitmap(bmp, 1500, 2000, false);

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        imageScaled.compress(Bitmap.CompressFormat.JPEG, 40, bos);
                        byte[] byteArray = bos.toByteArray();

                        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        Toast.makeText(this, encodedImage.substring(0, 15), Toast.LENGTH_SHORT).show();
                        System.out.print(encodedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private boolean checkExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission not granted.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i(TAG, "You already have permission!");
            return true;
        }
        return false;
    }
}