package com.example.dvueltaapp;

import java.util.ArrayList;

public class Cliente {
    private String nombre;
    private String apiKey;
    private ArrayList<Expedientes> expediente;

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

    public ArrayList<Expedientes> getExpediente() {
        return expediente;
    }

    public void setExpediente(ArrayList<Expedientes> expediente) {
        this.expediente = expediente;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", expediente=" + expediente +
                '}';
    }
}