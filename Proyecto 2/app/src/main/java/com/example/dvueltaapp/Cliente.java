package com.example.dvueltaapp;

import java.util.ArrayList;

public class Cliente {
    private String nombre;
    private String apiKey;
    private ArrayList<Expedientes> expedientes;

    public Cliente() {
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Expedientes> getExpedientes() {
        return expedientes;
    }

    public void setExpedientes(ArrayList<Expedientes> expedientes) {
        this.expedientes = expedientes;
    }
}