package com.smeagtechnology.integral;

public class SolucionPP {
    private int idpasoejerciciointegralpasos;
    private String  mensajeerrorexplicacionpasos;
    private String imagenejerciciointegralpasos;
    private int idresultadopasos;
    private int numeropaso;

    public SolucionPP(int idpasoejerciciointegralpasos, String mensajeerrorexplicacionpasos, String imagenejerciciointegralpasos, int idresultadopasos, int numeropaso) {
        this.idpasoejerciciointegralpasos = idpasoejerciciointegralpasos;
        this.mensajeerrorexplicacionpasos = mensajeerrorexplicacionpasos;
        this.imagenejerciciointegralpasos = imagenejerciciointegralpasos;
        this.idresultadopasos = idresultadopasos;
        this.numeropaso = numeropaso;
    }

    public int getIdpasoejerciciointegralpasos() {
        return idpasoejerciciointegralpasos;
    }

    public void setIdpasoejerciciointegralpasos(int idpasoejerciciointegralpasos) {
        this.idpasoejerciciointegralpasos = idpasoejerciciointegralpasos;
    }

    public String getMensajeerrorexplicacionpasos() {
        return mensajeerrorexplicacionpasos;
    }

    public void setMensajeerrorexplicacionpasos(String mensajeerrorexplicacionpasos) {
        this.mensajeerrorexplicacionpasos = mensajeerrorexplicacionpasos;
    }

    public String getImagenejerciciointegralpasos() {
        return imagenejerciciointegralpasos;
    }

    public void setImagenejerciciointegralpasos(String imagenejerciciointegralpasos) {
        this.imagenejerciciointegralpasos = imagenejerciciointegralpasos;
    }

    public int getIdresultadopasos() {
        return idresultadopasos;
    }

    public void setIdresultadopasos(int idresultadopasos) {
        this.idresultadopasos = idresultadopasos;
    }

    public int getNumeropaso() {
        return numeropaso;
    }

    public void setNumeropaso(int numeropaso) {
        this.numeropaso = numeropaso;
    }
}
