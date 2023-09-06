package com.smeagtechnology.integral;

public class Temas {
    private int idtema;
    private String nombretema;
    private String descripciontema;
    private String fototema;
    private String suscripcion;
    private String nombrevideo;

    public Temas(int idtema, String nombretema, String descripciontema, String fototema, String suscripcion, String nombrevideo) {
        this.idtema = idtema;
        this.nombretema = nombretema;
        this.descripciontema = descripciontema;
        this.fototema = fototema;
        this.suscripcion = suscripcion;
        this.nombrevideo = nombrevideo;
    }

    public int getIdtema() {
        return idtema;
    }

    public void setIdtema(int idtema) {
        this.idtema = idtema;
    }

    public String getNombretema() {
        return nombretema;
    }

    public void setNombretema(String nombretema) {
        this.nombretema = nombretema;
    }

    public String getDescripciontema() {
        return descripciontema;
    }

    public void setDescripciontema(String descripciontema) {
        this.descripciontema = descripciontema;
    }

    public String getFototema() {
        return fototema;
    }

    public void setFototema(String fototema) {
        this.fototema = fototema;
    }

    public String getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(String suscripcion) {
        this.suscripcion = suscripcion;
    }

    public String getNombrevideo() {
        return nombrevideo;
    }

    public void setNombrevideo(String nombrevideo) {
        this.nombrevideo = nombrevideo;
    }
}
