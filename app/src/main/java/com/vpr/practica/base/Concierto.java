package com.vpr.practica.base;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public class Concierto implements Serializable {
    //Atributos
    private long id;
    private String grupos;
    private Date fecha;
    private String hora;
    private float latitud;
    private float longitud;
    private float precio;
    //private transient Bitmap cartel;
    private boolean asistido;
    private boolean cancelado;


    //Metodos
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGrupos() {
        return grupos;
    }

    public void setGrupos(String grupos) {
        this.grupos = grupos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    /*public Bitmap getCartel() {
        return cartel;
    }*/

   /*public void setCartel(Bitmap cartel) {
        this.cartel = cartel;
    }*/

    public boolean isAsistido() {
        return asistido;
    }

    public void setAsistido(boolean asistido) {
        this.asistido = asistido;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String toString(){
        return grupos;
    }
}
