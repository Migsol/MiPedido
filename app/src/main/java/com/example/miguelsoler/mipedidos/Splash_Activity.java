package com.example.miguelsoler.mipedidos;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.miguelsoler.mipedidos.Activity.RecordActivity;
import com.example.miguelsoler.mipedidos.Configs.DBHelper;
import com.example.miguelsoler.mipedidos.POJO.Articulo;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.miguelsoler.mipedidos.Configs.Config.FILE_DIR;


/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  31/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/
public class Splash_Activity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 2000;
    private DBHelper databaseHelper = null;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    boolean flag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        requestPermissions1();
        if (flag){
            File dbPath = new File(Environment.getExternalStorageDirectory() + File.separator + FILE_DIR + File.separator);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    try {
                        databaseHelper = OpenHelperManager.getHelper(Splash_Activity.this, DBHelper.class);
                        fillData();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getApplicationContext(), RecordActivity.class));
                    Splash_Activity.this.finish();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
        } else {
            Toast.makeText(this, "Necesita aceptar los permisos", Toast.LENGTH_LONG).show();
            requestPermissions1();
        }

    }

    private void requestPermissions1() {

        final List<String> requiredSDKPermissions = new ArrayList<String>();
        requiredSDKPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ActivityCompat.requestPermissions(this,
                requiredSDKPermissions.toArray(new String[requiredSDKPermissions.size()]),
                REQUEST_CODE_ASK_PERMISSIONS);

        flag = true;
    }



    public void fillData() throws SQLException {
        Dao<Articulo, Integer> dao;
        dao = getHelper().getArticuloDao();

        DeleteBuilder<Articulo, Integer> db = dao.deleteBuilder();
        db.delete();

        Articulo data1 = new Articulo();
        data1.setNombre("Refresco");
        data1.setCosto(3500);
        data1.setDescripcion("Bebida endulzante saborizada gasificada");
        data1.setCodigoQR(null);
        data1.setImagen(R.drawable.draw_imag1);
        dao.create(data1);

        Articulo data2 = new Articulo();
        data2.setNombre("Jugo de Naranja");
        data2.setCosto(2500);
        data2.setDescripcion("Bebida natural");
        data2.setCodigoQR(null);
        data2.setImagen(R.drawable.draw_imag2);
        dao.create(data2);

        Articulo data3 = new Articulo();
        data3.setNombre("Queso");
        data3.setCosto(11000);
        data3.setDescripcion("Alimento sólido que se obtiene por maduración de la cuajada de la leche");
        data3.setCodigoQR(null);
        data3.setImagen(R.drawable.draw_imag3);
        dao.create(data3);

        Articulo data4 = new Articulo();
        data4.setNombre("Jamón");
        data4.setCosto(7000);
        data4.setDescripcion("Producto alimenticio obtenido de las patas traseras del cerdo");
        data4.setCodigoQR(null);
        data4.setImagen(R.drawable.draw_imag4);
        dao.create(data4);
    }

    private DBHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return databaseHelper;
    }

}
