package com.example.miguelsoler.mipedidos.Configs;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * | ## |   FECHA           |     PROGRAMADOR            | TWITTER
 * |###|--MES/AÑO---|-------------------------------------------------
 * | 01 |  07/2016          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.miguelsoler.mipedidos.POJO.Articulo;
import com.example.miguelsoler.mipedidos.POJO.Carrito;
import com.example.miguelsoler.mipedidos.POJO.Pedido;
import com.example.miguelsoler.mipedidos.POJO.Vendedor;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private Dao<Articulo, Integer> ArticuloDao;
    private Dao<Vendedor, Integer> VendedorDao;
    private Dao<Carrito, Integer> CarritoDao;
    private Dao<Pedido, Integer> PedidoDao;

    public DBHelper(Context context) {
        super(context, Environment.getExternalStorageDirectory()
                        + File.separator
                        + Config.FILE_DIR
                        + File.separator
                        + Config.DATABASE_NAME,
                null,
                Config.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Vendedor.class);
            TableUtils.createTable(connectionSource, Articulo.class);
            TableUtils.createTable(connectionSource, Carrito.class);
            TableUtils.createTable(connectionSource, Pedido.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.createTable(connectionSource, Vendedor.class);
            TableUtils.createTable(connectionSource, Articulo.class);
            TableUtils.createTable(connectionSource, Carrito.class);
            TableUtils.createTable(connectionSource, Pedido.class);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Dao<Articulo, Integer> getArticuloDao() throws SQLException {
        if (ArticuloDao == null) {
            ArticuloDao = getDao(Articulo.class);
        }
        return ArticuloDao;
    }

    public Dao<Vendedor, Integer> getVendedorDao() throws SQLException {
        if (VendedorDao == null) {
            VendedorDao = getDao(Vendedor.class);
        }
        return VendedorDao;
    }

    public Dao<Carrito, Integer> getCarritoDao() throws SQLException {
        if (CarritoDao == null) {
            CarritoDao = getDao(Carrito.class);
        }
        return CarritoDao;
    }

    public Dao<Pedido, Integer> getPedidoDao() throws SQLException {
        if (PedidoDao == null) {
            PedidoDao = getDao(Pedido.class);
        }
        return PedidoDao;
    }


    @Override
    public void close() {
        super.close();
        ArticuloDao = null;
        VendedorDao = null;
        CarritoDao = null;
        PedidoDao = null;
    }
}
