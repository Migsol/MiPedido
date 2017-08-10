package com.example.miguelsoler.mipedidos.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.miguelsoler.mipedidos.Adapters.RecordAdapter;
import com.example.miguelsoler.mipedidos.Configs.Config;
import com.example.miguelsoler.mipedidos.Configs.DBHelper;
import com.example.miguelsoler.mipedidos.Configs.ScannerActivity;
import com.example.miguelsoler.mipedidos.Configs.SessionManager;
import com.example.miguelsoler.mipedidos.POJO.Articulo;
import com.example.miguelsoler.mipedidos.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  30/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/

public class RecordActivity extends AppCompatActivity{

    RecyclerView Articulos;
    SessionManager session;
    List<Articulo> articuloList;

    RecordAdapter adapter;

    private LinearLayoutManager linearLayoutManager;
    private DBHelper databaseHelper = null;
    private Dao<Articulo, Integer> ArticuloDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Catálogo");

        session = new SessionManager(this);
        session.checkLogin();
        databaseHelper = OpenHelperManager.getHelper(this, DBHelper.class);

        Articulos = (RecyclerView) findViewById(R.id.recycler_data);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Articulos.setLayoutManager(linearLayoutManager);

        if(session.isLoggedIn()){
            fillRecycler();
        } else {
            Intent in = new Intent(this, LoginActivity.class);
            finish();
            startActivity(in);
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
            ArticuloDao = getHelper().getArticuloDao();
            articuloList = ArticuloDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapter = new RecordAdapter(this, articuloList);
        Articulos.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_record, menu);
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
                boolean qr = Config.getScanner(RecordActivity.this);
                String Search = Config.getQR(RecordActivity.this);
                if (qr){
                    adapter.filter(Search);
                    Articulos.invalidate();
                } else {
                    adapter.filter(searchQuery.toString().trim());
                    Articulos.invalidate();
                }
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
                    ArticuloDao = getHelper().getArticuloDao();
                    articuloList = ArticuloDao.queryForAll();
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

}
