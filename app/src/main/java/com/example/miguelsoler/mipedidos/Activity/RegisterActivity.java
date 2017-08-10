package com.example.miguelsoler.mipedidos.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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

public class RegisterActivity extends AppCompatActivity {

    private DBHelper databaseHelper = null;
    private EditText Code, Device, IMEI, Nombre, Apellido, Email, Celular, Password;
    private String CodeVndor, Name, Surname, emailname, Cel, Imeicode, devicename, Pass;

    private TextInputLayout TlNombre, TlApellido, TlCorreo, TlNumero, TlCodigo, TlPassw;

    Button pedido;

    private SessionManager session;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        session = new SessionManager(this);
        databaseHelper = OpenHelperManager.getHelper(this, DBHelper.class);


        Code = (EditText) findViewById(R.id.campo_codigovendedor);
        Nombre = (EditText) findViewById(R.id.campo_nombrevendedor);
        Apellido = (EditText) findViewById(R.id.campo_apellidovendedor);
        Email = (EditText) findViewById(R.id.campo_correovendedor);
        Celular = (EditText) findViewById(R.id.campo_celularvendedor);
        Device = (EditText) findViewById(R.id.campo_celulardevice);
        IMEI = (EditText) findViewById(R.id.campo_celularimei);
        Password = (EditText) findViewById(R.id.campo_passwvendedor);


        TlNombre = (TextInputLayout) findViewById(R.id.campo_nombre);
        TlApellido = (TextInputLayout) findViewById(R.id.campo_apellido);
        TlCorreo = (TextInputLayout) findViewById(R.id.campo_correo);
        TlNumero = (TextInputLayout) findViewById(R.id.campo_celular);
        TlCodigo = (TextInputLayout) findViewById(R.id.campo_codigo);
        TlPassw = (TextInputLayout) findViewById(R.id.campo_passw);


        IMEI.setVisibility(View.GONE);
        Device.setVisibility(View.GONE);

        pedido = (Button) findViewById(R.id.pedido);
        pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Nombre.getText().toString().equals("")) {
                    TlNombre.setError("Por favor Ingrese su nombre");
                } else if (Password.getText().toString().equals("")) {
                    TlPassw.setError("Por favor Ingrese su Contraseña");
                } else if (Email.getText().toString().equals("") || (!isValidEmail(Email.getText().toString()))) {
                    TlCorreo.setError("Por favor Ingrese su correo");
                } else if (Code.getText().toString().equals("")) {
                    TlCodigo.setError("Por favor Ingrese su código");
                } else if (Celular.getText().toString().equals("")) {
                    TlNumero.setError("Por favor Ingrese su número");
                } else if (Apellido.getText().toString().equals("")) {
                    TlApellido.setError("Por favor Ingrese su Apellido");
                } else {
                    onSavePressed();
                }
            }
        });
    }


    public void onSavePressed() {

        requestPermissions1();

        CodeVndor = Code.getText().toString();
        Name = Nombre.getText().toString();
        Surname = Apellido.getText().toString();
        emailname = Email.getText().toString();
        Cel = Celular.getText().toString();
        Pass = Password.getText().toString();

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String myIMEI = manager.getDeviceId();
        Device.setText(Config.getDeviceName());
        IMEI.setText(myIMEI);

        Imeicode = IMEI.getText().toString();
        devicename = Device.getText().toString();

        session.createLoginSession
                (CodeVndor,
                        Name,
                        Pass);

        Insert(CodeVndor,
                Pass,
                Name,
                Surname,
                emailname,
                Cel,
                Imeicode,
                devicename);


        Intent i = new Intent(this, Splash_Activity.class);
        startActivity(i);
        this.finish();
    }

    private void requestPermissions1() {

        final List<String> requiredSDKPermissions = new ArrayList<String>();
        requiredSDKPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ActivityCompat.requestPermissions(this,
                requiredSDKPermissions.toArray(new String[requiredSDKPermissions.size()]),
                REQUEST_CODE_ASK_PERMISSIONS);
    }

    public void Insert(String code, String Pass, String name, String surname, String email, String cel, String imei, String device) {

        Dao<Vendedor, Integer> dao;
        try {
            dao = getHelper().getVendedorDao();
            Vendedor syncdata = new Vendedor();
            syncdata.setCodigoVendedor(code);
            syncdata.setNombres(name);
            syncdata.setPassword(Pass);
            syncdata.setApellidos(surname);
            syncdata.setCorreo(email);
            syncdata.setCelular(cel);
            syncdata.setImei(imei);
            syncdata.setDeviceName(device);
            dao.create(syncdata);
        } catch (SQLException e) {
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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
