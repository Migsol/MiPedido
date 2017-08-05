package com.example.miguelsoler.mipedidos.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.miguelsoler.mipedidos.Configs.Config;
import com.example.miguelsoler.mipedidos.Configs.DBHelper;
import com.example.miguelsoler.mipedidos.Configs.SessionManager;
import com.example.miguelsoler.mipedidos.POJO.Vendedor;
import com.example.miguelsoler.mipedidos.R;
import com.example.miguelsoler.mipedidos.Splash_Activity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  29/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private DBHelper databaseHelper = null;
    private EditText Code, Device, IMEI, Nombre, Apellido, Email, Celular;
    private String CodeVndor, Name, Surname, emailname, Cel, Imeicode, devicename;

    Button pedido;
    boolean flag = false;

    private SessionManager session;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);
        databaseHelper = OpenHelperManager.getHelper(this, DBHelper.class);

        requestPermissions2();

        Code = (EditText) findViewById(R.id.campo_codigovendedor);
        Nombre = (EditText) findViewById(R.id.campo_nombrevendedor);
        Apellido = (EditText) findViewById(R.id.campo_apellidovendedor);
        Email = (EditText) findViewById(R.id.campo_correovendedor);
        Celular = (EditText) findViewById(R.id.campo_celularvendedor);
        Device = (EditText) findViewById(R.id.campo_celulardevice);
        IMEI = (EditText) findViewById(R.id.campo_celularimei);

        IMEI.setVisibility(View.GONE);
        Device.setVisibility(View.GONE);

        pedido = (Button) findViewById(R.id.pedido);
        pedido.setOnClickListener(this);
    }

    private void requestPermissions2() {

        final List<String> requiredSDKPermissions = new ArrayList<String>();
        requiredSDKPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        requiredSDKPermissions.add(Manifest.permission.READ_PHONE_STATE);

        ActivityCompat.requestPermissions(this,
                requiredSDKPermissions.toArray(new String[requiredSDKPermissions.size()]),
                REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pedido:
                CodeVndor = Code.getText().toString();
                Name = Nombre.getText().toString();
                Surname= Apellido.getText().toString();
                emailname = Email.getText().toString();
                Cel = Celular.getText().toString();

                TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                String myIMEI = manager.getDeviceId();
                Device.setText(Config.getDeviceName());
                IMEI.setText(myIMEI);

                Imeicode = IMEI.getText().toString();
                devicename = Device.getText().toString();

                session.createLoginSession
                        (CodeVndor,
                        Name,
                        Surname,
                        emailname,
                        Cel,
                        Imeicode,
                        devicename);

                Insert(CodeVndor,
                        Name,
                        Surname,
                        emailname,
                        Cel,
                        Imeicode,
                        devicename);

                Intent i = new Intent(this, Splash_Activity.class);
                startActivity(i);
                this.finish();
                break;
        }
    }

    public void Insert(String code,String name, String surname, String email, String cel, String imei, String device){

        Dao<Vendedor, Integer> dao;
        try {
            dao = getHelper().getVendedorDao();
            Vendedor syncdata = new Vendedor();
            syncdata.setCodigoVendedor(code);
            syncdata.setNombres(name);
            syncdata.setApellidos(surname);
            syncdata.setCorreo(email);
            syncdata.setCelular(cel);
            syncdata.setImei(imei);
            syncdata.setDeviceName(device);
            dao.create(syncdata);
        }
        catch (SQLException e)
        {
            Log.e("THis", "Error creando calls");
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
}
