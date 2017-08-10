package com.example.miguelsoler.mipedidos.Configs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguelsoler.mipedidos.Activity.RecordActivity;
import com.example.miguelsoler.mipedidos.POJO.Articulo;
import com.example.miguelsoler.mipedidos.POJO.Carrito;
import com.example.miguelsoler.mipedidos.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  31/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/

public class ScannerActivity extends AppCompatActivity {
    IntentIntegrator integrator;
    AppCompatActivity activity;
    boolean flag = false;
    Config config;
    private DBHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = OpenHelperManager.getHelper(ScannerActivity.this, DBHelper.class);

        activity = this;
        integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Escaneando...");
        integrator.setCameraId(0);
        integrator.setBarcodeImageEnabled(false);
        integrator.setBeepEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(activity, "Canceló el Escaneo", Toast.LENGTH_LONG).show();
                flag = false;
                Config.saveScanner(activity, false);
                Intent intent = new Intent(activity, RecordActivity.class);
                finish();
                startActivity(intent);
            } else {

                Toast.makeText(activity, result.getContents(), Toast.LENGTH_LONG).show();

                flag = true;
                Config.saveScanner(activity, true);
                Config.saveQR(activity, result.getContents());
                final Dialog dialog = new Dialog(activity);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setCancelable(false);
                dialog.show();

                try {
                    Dao<Articulo, Integer> dao;
                    dao = getHelper().getArticuloDao();

                    for (Articulo articulo : dao) {
                        if (articulo.getNombre().equals(result.getContents()))
                        {

                            final String Name = articulo.getNombre();
                            String Desc = articulo.getDescripcion();
                            String Costo = String.valueOf(articulo.getCosto());
                            int foto = articulo.getImagen();

                            final Carrito ar = new Carrito();
                            ar.setNombre(Name);
                            ar.setImagen(foto);
                            ar.setDescripcion(Desc);
                            ar.setCosto(Integer.parseInt(Costo));

                            final TextView cantidad = (TextView) dialog.findViewById(R.id.canti);
                            final TextView Costos = (TextView) dialog.findViewById(R.id.costo);
                            final TextView Nombre = (TextView) dialog.findViewById(R.id.name);
                            final ImageView Foto = (ImageView) dialog.findViewById(R.id.foto);
                            final ImageView ups = (ImageView) dialog.findViewById(R.id.ups);
                            final ImageView downs = (ImageView) dialog.findViewById(R.id.downs);

                            final int[] C = {1};
                            ups.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (C[0] >= 1) {
                                        C[0] = C[0] + 1;
                                    }
                                    cantidad.setText(String.valueOf(C[0]));
                                }
                            });

                            downs.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (C[0] > 1) {
                                        C[0] = C[0] - 1;
                                    }
                                    cantidad.setText(String.valueOf(C[0]));
                                }
                            });

                            cantidad.setText(String.valueOf(C[0]));
                            Costos.setText(Costo);
                            Nombre.setText(Name);
                            Foto.setImageResource(foto);

                            final Button guardar = (Button) dialog.findViewById(R.id.save);
                            guardar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ar.setCantidad(Integer.parseInt(cantidad.getText().toString()));

                                    try {
                                        boolean update = false;
                                        Dao<Carrito, Integer> dao;
                                        dao = getHelper().getCarritoDao();
                                        for (Carrito car : dao) {
                                            if (car.getNombre().equals(Name)) {
                                                int newC = Integer.parseInt(cantidad.getText().toString()) + car.getCantidad();
                                                UpdateBuilder<Carrito, Integer> updateBuilder = dao.updateBuilder();
                                                updateBuilder.updateColumnValue("cantidad", newC);
                                                updateBuilder.where().eq("_id", car.getId());
                                                updateBuilder.update();
                                                update = true;
                                            }
                                        }
                                        if (!update) {
                                            dao.create(ar);
                                        }
                                        dialog.dismiss();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    Intent i = new Intent(ScannerActivity.this, RecordActivity.class);
                                    startActivity(i);
                                    ScannerActivity.this.finish();
                                }
                            });
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private DBHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(activity, DBHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, RecordActivity.class);
        startActivity(i);
        this.finish();
    }
}
