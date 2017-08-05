package com.example.miguelsoler.mipedidos.POJO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  31/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/
@DatabaseTable
public class Carrito {
    public static final String ID = "_id";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = "nombre")
    private String Nombre;
    @DatabaseField(columnName = "costo")
    private int Costo;
    @DatabaseField(columnName = "imagen")
    private int Imagen;
    @DatabaseField(columnName = "descripcion")
    private String Descripcion;
    @DatabaseField(columnName = "codigoQR", canBeNull=true)
    private String CodigoQR;
    @DatabaseField(columnName = "cantidad")
    private int Cantidad;

    public Carrito() {
    }

    public Carrito(String nombre) {
        Nombre = nombre;
    };

    public Carrito(String nombre, int costo, int imagen, String descripcion, int cantidad) {
        Nombre = nombre;
        Costo = costo;
        Imagen = imagen;
        Descripcion = descripcion;
        Cantidad = cantidad;
        CodigoQR = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getCosto() {
        return Costo;
    }

    public void setCosto(int costo) {
        Costo = costo;
    }

    public int getImagen(int position) {
        return Imagen;
    }

    public void setImagen(int imagen) {
        Imagen = imagen;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public String getCodigoQR() {
        return CodigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        CodigoQR = codigoQR;
    }
}
