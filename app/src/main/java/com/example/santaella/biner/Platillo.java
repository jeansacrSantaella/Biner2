package com.example.santaella.biner;

/**
 * Created by Santaella on 29/01/18.
 */

public class Platillo {
    private  String nombre;
    private int drawableImageID;
    private String descripcion;

    public  Platillo(String nombre,int drawableImageID,String descripcion){
        this.nombre=nombre;
        this.drawableImageID=drawableImageID;
        this.descripcion=descripcion;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }

    public int getDrawableImagenID(){
        return drawableImageID;
    }
    public void setDrawable(int drawableImageID){
        this.drawableImageID=drawableImageID;
    }

    public String getDescripcion(){return descripcion;}
    public void setDescripcion(String descripcion){this.descripcion=descripcion;}


}