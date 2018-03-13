package com.example.santaella.biner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Santaella on 03/02/18.
 */

public class Pedido {
    private  String nombre;
    private  int cantidad;
    private  double total;
    public Pedido(String nombre,int cantidad,double total){
        this.nombre=nombre;
        this.cantidad=cantidad;
        this.total=total;
    }
    public void setNombre(String nombre){this.nombre=nombre;}

    public void setCantidad(int cantidad){this.cantidad=cantidad;}

    public void setTotal(double total){this.total=total;}

    public String getNombre(){return nombre;}
    public int getCantidad(){return cantidad;}
    public double getTotal(){return  total;}
}
