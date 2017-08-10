package com.example.miguelsoler.mipedidos.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
 * |  * | * | 01 |  31/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *****************************************************************************************/

public class LoginActivity extends AppCompatActivity {

    private EditText Codigo, Clave;
    private DBHelper databaseHelper = null;
    private SessionManager sessionManager;

    private String codigo, clave;
    private Button Register, Login;
    String Names, Pass, Codi;

    boolean flagUser = false;
    boolean flagPass = false;

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sessionManager = new SessionManager(this);
        Codigo = (EditText)findViewById(R.id.campo_usuario);
        Clave = (EditText)findViewById(R.id.campo_pass);
        Register = (Button)findViewById(R.id.register);
        Login = (Button) findViewById(R.id.login);

        if (sessionManager.isLoggedIn()){

            Intent i = new Intent(this, Splash_Activity.class);
            startActivity(i);
            this.finish();

        } else {

            requestPermissions2();

            Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(i);
                    LoginActivity.this.finish();
                }
            });

            Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    codigo = Codigo.getText().toString();
                    clave = Clave.getText().toString();

                    requestPermissions1();
                    try {
                        Dao<Vendedor, Integer> VendedorDao;
                        VendedorDao = getHelper().getVendedorDao();

                        for (Vendedor vendedor : VendedorDao){
                            if (vendedor.getCodigoVendedor().equals(codigo)){
                                flagUser = true;
                                if (vendedor.getPassword().equals(clave)){
                                    flagPass = true;
                                    Codi = codigo;
                                    Pass = clave;
                                    Names = vendedor.getNombres();
                                }
                            }
                        }

                        onNextStep(Codi,Names, Pass);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    private DBHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return databaseHelper;
    }

    private void requestPermissions2() {
        final List<String> requiredSDKPermissions = new ArrayList<String>();
        requiredSDKPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        requiredSDKPermissions.add(Manifest.permission.READ_PHONE_STATE);

        ActivityCompat.requestPermissions(this,
                requiredSDKPermissions.toArray(new String[requiredSDKPermissions.size()]),
                REQUEST_CODE_ASK_PERMISSIONS);
    }

    private void requestPermissions1() {

        final List<String> requiredSDKPermissions = new ArrayList<String>();
        requiredSDKPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ActivityCompat.requestPermissions(this,
                requiredSDKPermissions.toArray(new String[requiredSDKPermissions.size()]),
                REQUEST_CODE_ASK_PERMISSIONS);
    }

    private void onNextStep(String Cod, String Name, String Pass){
        if(flagUser){
            if(flagPass){
                sessionManager.createLoginSession(Cod, Name, Pass);
                Intent i = new Intent(LoginActivity.this, Splash_Activity.class);
                startActivity(i);
                LoginActivity.this.finish();
            } else {
                Toast.makeText(LoginActivity.this, "Clave Incorrecta por favor verifique e intente nuevamente", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(LoginActivity.this, "Usuario no encontrado en nuestra Base de Datos", Toast.LENGTH_LONG).show();
        }

    }


}

