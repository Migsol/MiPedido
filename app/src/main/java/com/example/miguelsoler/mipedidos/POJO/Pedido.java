package com.example.miguelsoler.mipedidos.POJO;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.List;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  2/8/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/
@DatabaseTable
public class Pedido {
    public static final String ID = "_id";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = "codigo")
    private String Codigo;
    @DatabaseField(columnName = "fecha")
    private String Fecha;
    @DatabaseField(columnName = "hora")
    private String Hora;
    @DatabaseField(columnName = "costoTotal")
    private String costoTotal;
    @DatabaseField(columnName = "numItems")
    private int numItems;

    public Pedido() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(String costoTotal) {
        this.costoTotal = costoTotal;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

}
