package com.smeagtechnology.integral;

public class EjerciciosPasos {
    private int idejerciciointegralpasos;
    private String descripcionejerciciointegralpasos;
    private String imagenejerciciointegralpasos;

    public EjerciciosPasos(int idejerciciointegralpasos, String descripcionejerciciointegralpasos, String imagenejerciciointegralpasos) {
        this.idejerciciointegralpasos = idejerciciointegralpasos;
        this.descripcionejerciciointegralpasos = descripcionejerciciointegralpasos;
        this.imagenejerciciointegralpasos = imagenejerciciointegralpasos;
    }

    public int getIdejerciciointegralpasos() {
        return idejerciciointegralpasos;
    }

    public void setIdejerciciointegralpasos(int idejerciciointegralpasos) {
        this.idejerciciointegralpasos = idejerciciointegralpasos;
    }

    public String getDescripcionejerciciointegralpasos() {
        return descripcionejerciciointegralpasos;
    }

    public void setDescripcionejerciciointegralpasos(String descripcionejerciciointegralpasos) {
        this.descripcionejerciciointegralpasos = descripcionejerciciointegralpasos;
    }

    public String getImagenejerciciointegralpasos() {
        return imagenejerciciointegralpasos;
    }

    public void setImagenejerciciointegralpasos(String imagenejerciciointegralpasos) {
        this.imagenejerciciointegralpasos = imagenejerciciointegralpasos;
    }
}
