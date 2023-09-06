package com.smeagtechnology.integral;

public class SolucionEjercicio {
    private int idsolucion;
    private String explicacionpaso;
    private String fotopaso;


    public SolucionEjercicio(int idsolucion, String explicacionpaso, String fotopaso) {
        this.idsolucion = idsolucion;
        this.explicacionpaso = explicacionpaso;
        this.fotopaso = fotopaso;
    }

    public int getIdsolucion() {
        return idsolucion;
    }

    public void setIdsolucion(int idsolucion) {
        this.idsolucion = idsolucion;
    }

    public String getExplicacionpaso() {
        return explicacionpaso;
    }

    public void setExplicacionpaso(String explicacionpaso) {
        this.explicacionpaso = explicacionpaso;
    }

    public String getFotopaso() {
        return fotopaso;
    }

    public void setFotopaso(String fotopaso) {
        this.fotopaso = fotopaso;
    }
}
