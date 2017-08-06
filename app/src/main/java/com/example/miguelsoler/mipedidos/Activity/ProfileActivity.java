package com.example.miguelsoler.mipedidos.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.miguelsoler.mipedidos.Configs.DBHelper;
import com.example.miguelsoler.mipedidos.Configs.SessionManager;
import com.example.miguelsoler.mipedidos.POJO.Vendedor;
import com.example.miguelsoler.mipedidos.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  31/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/
public class ProfileActivity extends AppCompatActivity {

    private DBHelper databaseHelper = null;
    private TextView Code, Device, IMEI, Nombre, Apellido, Email, Celular;
    private String CodeVndor, Name, Surname, emailname, Cel, Imeicode, devicename;

    Button pedido;


    List<Vendedor> vendedor;
    private SessionManager session;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private Dao<Vendedor, Integer> VendedorDao;
    String nombre;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activty);

        session = new SessionManager(this);
        databaseHelper = OpenHelperManager.getHelper(this, DBHelper.class);

        session.checkLogin();

        if (session.isLoggedIn()){
            HashMap<String, String> user = session.getUserDetails();

            nombre = user.get(SessionManager.KEY_NAME);

            Code = (TextView) findViewById(R.id.campo_codigovendedor);
            Nombre = (TextView) findViewById(R.id.campo_nombrevendedor);
            Apellido = (TextView) findViewById(R.id.campo_apellidovendedor);
            Email = (TextView) findViewById(R.id.campo_correovendedor);
            Celular = (TextView) findViewById(R.id.campo_celularvendedor);
            Device = (TextView) findViewById(R.id.campo_celulardevice);
            IMEI = (TextView) findViewById(R.id.campo_celularimei);

            fillData();
        } else {
            Intent in = new Intent(this, LoginActivity.class);
            finish();
            startActivity(in);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Perfíl");





    }

    public void fillData(){
        try {
            VendedorDao = getHelper().getVendedorDao();
            for (Vendedor venta: VendedorDao){
                if (venta.getNombres().equals(nombre)){
                    Code.setText(venta.getCodigoVendedor());
                    Nombre.setText(venta.getNombres());
                    Apellido.setText(venta.getApellidos());
                    Email.setText(venta.getCorreo());
                    Celular.setText(venta.getCelular());
                    IMEI.setText(venta.getImei());
                    Device.setText(venta.getDeviceName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DBHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_pedido:
                Intent in = new Intent(this, CarritoActivity.class);
                startActivity(in);
                return true;

            case R.id.action_records:
                Intent iz = new Intent(this, RecordActivity.class);
                startActivity(iz);
                this.finish();
                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, RecordActivity.class);
        startActivity(i);
        this.finish();
    }
}