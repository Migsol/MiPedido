package com.example.miguelsoler.mipedidos.POJO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * | ## |   FECHA           |     PROGRAMADOR            | TWITTER
 * |###|--MES/AÑO---|-------------------------------------------------
 * | 01 |  07/2016          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/
@DatabaseTable
public class Articulo {
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


    public Articulo() {
    }

    public Articulo(String nombre, int costo, String descripcion, int imagen) {
        Nombre = nombre;
        Costo = costo;
        Imagen = imagen;
        Descripcion = descripcion;
        CodigoQR = null;
    }

    public static String getID() {
        return ID;
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

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getImagen() {
        return Imagen;
    }

    public int getImagen(int position) {
        return Imagen;
    }

    public void setImagen(int imagen) {
        Imagen = imagen;
    }

    public String getCodigoQR() {
        return CodigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        CodigoQR = codigoQR;
    }
}
