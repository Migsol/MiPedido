package com.example.miguelsoler.mipedidos.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miguelsoler.mipedidos.Adapters.CarritoAdapter;
import com.example.miguelsoler.mipedidos.Adapters.RecordAdapter;
import com.example.miguelsoler.mipedidos.Configs.Config;
import com.example.miguelsoler.mipedidos.Configs.DBHelper;
import com.example.miguelsoler.mipedidos.Configs.ScannerActivity;
import com.example.miguelsoler.mipedidos.Configs.SessionManager;
import com.example.miguelsoler.mipedidos.POJO.Carrito;
import com.example.miguelsoler.mipedidos.POJO.Pedido;
import com.example.miguelsoler.mipedidos.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  30/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/

public class CarritoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TIME_DELAY = 1500;
    int Sumatoria, total;
    RecyclerView Articulos;
    SessionManager session;
    List<Carrito> CarritoList;

    CarritoAdapter adapter;
    LinearLayout layout;

    TextView Costo;

    Config config;

    Button saveCarrito;

    private LinearLayoutManager linearLayoutManager;
    private DBHelper databaseHelper = null;
    private Dao<Carrito, Integer> CarritoDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mi Carrito");

        session = new SessionManager(this);
        session.checkLogin();
        databaseHelper = OpenHelperManager.getHelper(this, DBHelper.class);

        Articulos = (RecyclerView) findViewById(R.id.recycler_data);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Articulos.setLayoutManager(linearLayoutManager);

        layout = (LinearLayout) findViewById(R.id.Carrito);
        layout.setVisibility(View.VISIBLE);

        Costo = (TextView) findViewById(R.id.campo_costoTotal);

        saveCarrito = (Button) findViewById(R.id.sendPedido);
        saveCarrito.setOnClickListener(this);

        fillRecycler();

        Calculate();
    }

    public void Calculate() {

        final Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                total = Config.getSumas(CarritoActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Costo.setText(String.valueOf(total));
                    }
                });


                h.postDelayed(this, TIME_DELAY);
            }
        }, TIME_DELAY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendPedido:
                insert(CarritoList);
                break;
        }

    }

    private void insert(List<Carrito> carritoList) {

        Random randomGenerator = new Random();
        int Cod = randomGenerator.nextInt(1000);
        String Codigo = "Cod-00" + Cod;

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String currentDateandTime = sdf.format(new Date());

        SimpleDateFormat sdf2 = new SimpleDateFormat("h:mm a");
        String currentTime = sdf2.format(new Date());

        int size = carritoList.size();

        Dao<Pedido, Integer> dao;
        try {
            dao = getHelper().getPedidoDao();
            Pedido syncdata = new Pedido();
            syncdata.setCodigo(Codigo);
            syncdata.setFecha(currentDateandTime);
            syncdata.setHora(currentTime);
            syncdata.setCostoTotal(Costo.getText().toString());
            syncdata.setNumItems(size);
            dao.create(syncdata);

            for (Carrito car : carritoList) {
                Dao<Carrito, Integer> daoo;
                daoo = getHelper().getCarritoDao();
                DeleteBuilder<Carrito, Integer> deleteBuilder = daoo.deleteBuilder();
                daoo.deleteById(car.getId());
                Log.e("Indice", String.valueOf(car.getId()));
            }

            Config.saveSuma(this, 0);

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        } catch (SQLException e) {
            Log.e("Error:", e.toString());
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

    public void fillRecycler() {
        try {
            CarritoDao = getHelper().getCarritoDao();
            CarritoList = CarritoDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new CarritoAdapter(this, CarritoList);
        Articulos.setAdapter(adapter);

        Calculate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_carrito, menu);
        MenuItem searchItem = menu.findItem(R.id.action_buscar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                adapter.filter(searchQuery.toString().trim());
                Articulos.invalidate();
                return true;
            }
        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_buscar:
                return true;

            case R.id.action_update:
                try {
                    CarritoDao = getHelper().getCarritoDao();
                    CarritoList = CarritoDao.queryForAll();
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;

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
