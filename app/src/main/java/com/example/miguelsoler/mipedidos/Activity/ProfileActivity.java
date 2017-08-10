package com.example.miguelsoler.mipedidos.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.miguelsoler.mipedidos.Configs.DBHelper;
import com.example.miguelsoler.mipedidos.Configs.ScannerActivity;
import com.example.miguelsoler.mipedidos.Configs.SessionManager;
import com.example.miguelsoler.mipedidos.POJO.Pedido;
import com.example.miguelsoler.mipedidos.POJO.Vendedor;
import com.example.miguelsoler.mipedidos.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  31/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/
public class ProfileActivity extends AppCompatActivity {

    private DBHelper databaseHelper = null;
    private TextView Code, Device, IMEI, Nombre, Apellido, Email, Celular, Total;
    private String CodeVndor, Name, Surname, emailname, Cel, Imeicode, devicename, Pass;

    Button modificar, cancel, save;
    LinearLayout llayout;

    int id;


    List<Vendedor> vendedor;
    private SessionManager session;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private Dao<Vendedor, Integer> VendedorDao;
    String nombre;
    int costo;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activty);

        session = new SessionManager(this);
        databaseHelper = OpenHelperManager.getHelper(this, DBHelper.class);

        session.checkLogin();

        if (session.isLoggedIn()) {
            HashMap<String, String> user = session.getUserDetails();

            nombre = user.get(SessionManager.KEY_NAME);

            Code = (TextView) findViewById(R.id.campo_codigovendedor);
            Nombre = (TextView) findViewById(R.id.campo_nombrevendedor);
            Apellido = (TextView) findViewById(R.id.campo_apellidovendedor);
            Email = (TextView) findViewById(R.id.campo_correovendedor);
            Celular = (TextView) findViewById(R.id.campo_celularvendedor);
            Device = (TextView) findViewById(R.id.campo_celulardevice);
            IMEI = (TextView) findViewById(R.id.campo_celularimei);
            modificar = (Button) findViewById(R.id.modificar);
            llayout = (LinearLayout) findViewById(R.id.linearLayout2);
            cancel = (Button) findViewById(R.id.action_cancel);
            save = (Button) findViewById(R.id.action_save);
            Total = (TextView)findViewById(R.id.campo_vendedorsaldo);

            modificar(false);
            modificar.setVisibility(View.VISIBLE);
            llayout.setVisibility(View.GONE);
            fillData();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Perfíl");

            modificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modificar.setVisibility(View.GONE);
                    modificar(true);
                    llayout.setVisibility(View.VISIBLE);
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llayout.setVisibility(View.GONE);
                    modificar(false);
                    modificar.setVisibility(View.VISIBLE);
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Imeicode = IMEI.getText().toString().trim();
                    devicename = Device.getText().toString().trim();
                    CodeVndor = Code.getText().toString().trim();
                    Name = Nombre.getText().toString().trim();
                    Surname = Apellido.getText().toString().trim();
                    emailname = Email.getText().toString().trim();
                    Cel = Celular.getText().toString().trim();

                    try {

                        Dao<Vendedor, Integer> dao;
                        dao = getHelper().getVendedorDao();
                            for (Vendedor car : dao) {
                                if (car.getId() == id) {
                                    Pass = car.getPassword();
                                    Log.e("Data", car.toString());
                                    UpdateBuilder<Vendedor, Integer> updateBuilder = dao.updateBuilder();
                                    updateBuilder.updateColumnValue("nombres", Name);
                                    updateBuilder.updateColumnValue("apellidos", Surname);
                                    updateBuilder.updateColumnValue("correo", emailname);
                                    updateBuilder.updateColumnValue("celular", Cel);
                                    updateBuilder.updateColumnValue("saldo", costo);
                                    updateBuilder.where().eq("_id", car.getId());
                                    updateBuilder.update();
                                }
                            }

                        session.cleanUser();

                        session.createLoginSession
                                (CodeVndor,
                                        Name,
                                        Pass);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    fillData();
                    llayout.setVisibility(View.GONE);
                    modificar(false);
                    modificar.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Intent in = new Intent(this, RegisterActivity.class);
            finish();
            startActivity(in);
        }
    }

    public void fillData() {
        try {

            Dao<Pedido, Integer> PedidoDao;
            PedidoDao = getHelper().getPedidoDao();

            for (Pedido ped : PedidoDao){
                costo += Integer.parseInt(ped.getCostoTotal());
            }

            VendedorDao = getHelper().getVendedorDao();
            for (Vendedor venta : VendedorDao) {
                if (venta.getNombres().equals(nombre)) {
                    Log.e("Nombre", nombre);
                    Code.setText(venta.getCodigoVendedor());
                    Log.e("Nombre", venta.getCodigoVendedor());
                    Nombre.setText(venta.getNombres());
                    Log.e("Nombre", venta.getNombres());
                    Apellido.setText(venta.getApellidos());
                    Email.setText(venta.getCorreo());
                    Celular.setText(venta.getCelular());
                    IMEI.setText(venta.getImei());
                    Device.setText(venta.getDeviceName());
                    String a = "Q " + String.valueOf(venta.getSaldo());
                    Total.setText(a);

                    id= venta.getId();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificar(boolean data) {
        Code.setEnabled(data);
        Nombre.setEnabled(data);
        Apellido.setEnabled(data);
        Email.setEnabled(data);
        Celular.setEnabled(data);
        Device.setEnabled(data);
        IMEI.setEnabled(data);
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

            case R.id.action_records:
                Intent iz = new Intent(this, RecordActivity.class);
                startActivity(iz);
                this.finish();
                return true;

            case R.id.action_pedido:
                Intent in = new Intent(this, PedidosActivity.class);
                startActivity(in);
                this.finish();
                return true;

            case R.id.action_carrito:
                Intent car = new Intent(this, CarritoActivity.class);
                startActivity(car);
                this.finish();
                return true;

            case R.id.action_profile:
                Intent i = new Intent(this, ProfileActivity.class);
                startActivity(i);
                this.finish();
                return true;

            case R.id.action_scan:
                Intent sc = new Intent(this, ScannerActivity.class);
                startActivity(sc);
                this.finish();
                return true;

            case R.id.action_logout:
                session.logoutUser();
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