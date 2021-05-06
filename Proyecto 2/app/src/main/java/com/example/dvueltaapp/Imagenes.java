package com.example.dvueltaapp;

import java.util.ArrayList;

public class Imagenes {

    private String apiKeyUser;
    private String idImagen;
    private ArrayList<ImagenesBase64> imagenesBase64;

    public Imagenes() {
    }

    public String getApiKeyUser() {
        return apiKeyUser;
    }

    public void setApiKeyUser(String apiKeyUser) {
        this.apiKeyUser = apiKeyUser;
    }

    public String getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(String idImagen) {
        this.idImagen = idImagen;
    }

    public ArrayList<ImagenesBase64> getImagenesBase64() {
        return imagenesBase64;
    }

    public void setImagenesBase64(ArrayList<ImagenesBase64> imagenesBase64) {
        this.imagenesBase64 = imagenesBase64;
    }
}
