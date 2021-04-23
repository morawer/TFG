package com.example.dvueltaapp;

public class Expedientes {

    private String fechaExp;
    private String nroExp;
    private String estadoExp;
    private String NomOrg;
    private String hechoDenunciado;
    private String importe;
    private String puntos;
    private String matricula;
    private String idImagen;

    public Expedientes() {
    }

    public String getFechaExp() {
        return fechaExp;
    }

    public void setFechaExp(String fechaExp) {
        this.fechaExp = fechaExp;
    }

    public String getNroExp() {
        return nroExp;
    }

    public void setNroExp(String nroExp) {
        this.nroExp = nroExp;
    }

    public String getEstadoExp() {
        return estadoExp;
    }

    public void setEstadoExp(String estadoExp) {
        this.estadoExp = estadoExp;
    }

    public String getNomOrg() {
        return NomOrg;
    }

    public void setNomOrg(String nomOrg) {
        NomOrg = nomOrg;
    }

    public String getHechoDenunciado() {
        return hechoDenunciado;
    }

    public void setHechoDenunciado(String hechoDenunciado) {
        this.hechoDenunciado = hechoDenunciado;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(String idImagen) {
        this.idImagen = idImagen;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String toString() {
        return "Expedientes{" +
                "fechaExp='" + fechaExp + '\'' +
                ", nroExp='" + nroExp + '\'' +
                ", estadoExp='" + estadoExp + '\'' +
                ", NomOrg='" + NomOrg + '\'' +
                ", hechoDenunciado='" + hechoDenunciado + '\'' +
                ", importe='" + importe + '\'' +
                ", puntos='" + puntos + '\'' +
                ", matricula='" + matricula + '\'' +
                ", idImagen=" + idImagen +
                '}';
    }
}
