package com.example.miguelsoler.mipedidos.POJO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  28/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/
@DatabaseTable
public class Vendedor {
    public static final String ID = "_id";


    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = "codigoVendedor")
    private String CodigoVendedor;
    @DatabaseField(columnName = "nombres")
    private String Nombres;
    @DatabaseField(columnName = "password")
    private String Password;
    @DatabaseField(columnName = "apellidos")
    private String Apellidos;
    @DatabaseField(columnName = "correo")
    private String Correo;
    @DatabaseField(columnName = "imei")
    private String Imei;
    @DatabaseField(columnName = "deviceName")
    private String DeviceName;
    @DatabaseField(columnName = "celular")
    private String Celular;
    @DatabaseField(columnName = "saldo")
    private int Saldo;


    public Vendedor() {
    }

    public Vendedor(int id, String nombres, String apellidos, String correo, String imei, String deviceName, String celular) {
        this.id = id;
        Nombres = nombres;
        Apellidos = apellidos;
        Correo = correo;
        Imei = imei;
        DeviceName = deviceName;
        Celular = celular;
        Saldo = 0;
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

    public String getCodigoVendedor() {
        return CodigoVendedor;
    }

    public void setCodigoVendedor(String codigoVendedor) {
        CodigoVendedor = codigoVendedor;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getImei() {
        return Imei;
    }

    public void setImei(String imei) {
        Imei = imei;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }

    public int getSaldo() {
        return Saldo;
    }

    public void setSaldo(int saldo) {
        Saldo = saldo;
    }
}
