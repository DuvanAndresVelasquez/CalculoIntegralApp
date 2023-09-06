package com.smeagtechnology.integral;

public class Ejercicio {

    private int idejercicio;
    private String nombreejercicio;
    private String fotoejercicio;


    public Ejercicio(int idejercicio, String nombreejercicio, String fotoejercicio) {
        this.idejercicio = idejercicio;
        this.nombreejercicio = nombreejercicio;
        this.fotoejercicio = fotoejercicio;
    }

    public int getIdejercicio() {
        return idejercicio;
    }

    public void setIdejercicio(int idejercicio) {
        this.idejercicio = idejercicio;
    }

    public String getNombreejercicio() {
        return nombreejercicio;
    }

    public void setNombreejercicio(String nombreejercicio) {
        this.nombreejercicio = nombreejercicio;
    }

    public String getFotoejercicio() {
        return fotoejercicio;
    }

    public void setFotoejercicio(String fotoejercicio) {
        this.fotoejercicio = fotoejercicio;
    }
}
