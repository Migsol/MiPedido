package com.example.miguelsoler.mipedidos.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.miguelsoler.mipedidos.Configs.Config;
import com.example.miguelsoler.mipedidos.Configs.DBHelper;
import com.example.miguelsoler.mipedidos.POJO.Carrito;
import com.example.miguelsoler.mipedidos.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  30/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/
public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.RecordViewHolder> {

    private List<Carrito> Articulos;
    private ArrayList<Carrito> arraylist;
    private Activity activity;

    private int Sum;

    public CarritoAdapter(Activity AC, List<Carrito> A) {
        this.activity = AC;
        this.Articulos = A;
        arraylist = new ArrayList<Carrito>();
        arraylist.addAll(Articulos);
    }

    private DBHelper databaseHelper = null;

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.records_item, parent, false);
        return new RecordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecordViewHolder holder, final int position) {

        Carrito articulos = new Carrito();
        articulos = Articulos.get(position);

        final String Name = articulos.getNombre();
        String Desc = articulos.getDescripcion();
        final String Costo = String.valueOf(articulos.getCosto() * articulos.getCantidad());

        int foto = articulos.getImagen(position);

        holder.Nombre.setText(Name);
        holder.Descripcion.setText(Desc);
        holder.Costo.setText(Costo);

        holder.Foto.setImageResource(foto);

        holder.layout.setVisibility(View.VISIBLE);
        holder.Cant.setVisibility(View.VISIBLE);
        holder.delete.setVisibility(View.VISIBLE);
        final int[] cantidad = {articulos.getCantidad()};

        Sum += articulos.getCantidad() * articulos.getCosto();
        Config.saveSuma(activity, Sum);
        Log.e("Sumatoria", String.valueOf(Sum));

        holder.Cant.setText(String.valueOf(cantidad[0]));
        final Carrito finalArticulos = articulos;
        holder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cantidad[0] >= 1) {
                    cantidad[0]++;
                    try {
                        Dao<Carrito, Integer> dao;
                        dao = getHelper().getCarritoDao();
                        for (Carrito car : dao) {
                            if (car.getNombre().equals(Name)) {
                                UpdateBuilder<Carrito, Integer> updateBuilder = dao.updateBuilder();
                                updateBuilder.updateColumnValue("cantidad", cantidad[0]);
                                updateBuilder.where().eq("_id", car.getId());
                                updateBuilder.update();
                            }
                        }

                        Sum += finalArticulos.getCantidad() * finalArticulos.getCosto();
                        Config.saveSuma(activity, Sum);
                        Log.e("Sumatoria", String.valueOf(Sum));

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                holder.Cant.setText(String.valueOf(cantidad[0]));
                holder.Costo.setText(String.valueOf(finalArticulos.getCosto() * cantidad[0]));
            }
        });

        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cantidad[0] > 1) {
                    cantidad[0]--;
                    try {
                        Dao<Carrito, Integer> dao;
                        dao = getHelper().getCarritoDao();
                        for (Carrito car : dao) {
                            if (car.getNombre().equals(Name)) {
                                UpdateBuilder<Carrito, Integer> updateBuilder = dao.updateBuilder();
                                updateBuilder.updateColumnValue("cantidad", cantidad[0]);
                                updateBuilder.where().eq("_id", car.getId());
                                updateBuilder.update();
                            }
                        }

                        Sum -= finalArticulos.getCantidad() * finalArticulos.getCosto();
                        Config.saveSuma(activity, Sum);
                        Log.e("Sumatoria", String.valueOf(Sum));

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                holder.Cant.setText(String.valueOf(cantidad[0]));
                holder.Costo.setText(String.valueOf(finalArticulos.getCosto() * cantidad[0]));

            }
        });

        final Carrito finalArticulos1 = articulos;
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(finalArticulos1.getId(), position);
            }
        });


    }

    public void delete(int id , int position){
        try {
            Dao<Carrito, Integer> dao;
            dao = getHelper().getCarritoDao();
            DeleteBuilder<Carrito, Integer> deleteBuilder = dao.deleteBuilder();
            dao.deleteById(id);
            Articulos.remove(position);
            notifyItemRemoved(position);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DBHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(activity, DBHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public int getItemCount() {
        return Articulos.size();
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView Nombre, Descripcion, Costo, Cant, delete;
        ImageView Foto, up, down;
        LinearLayout layout;

        public RecordViewHolder(View itemView) {
            super(itemView);
            Nombre = (TextView) itemView.findViewById(R.id.namecateList);
            Descripcion = (TextView) itemView.findViewById(R.id.desccateList);
            Costo = (TextView) itemView.findViewById(R.id.costcateList);
            Foto = (ImageView) itemView.findViewById(R.id.logocateList);
            up = (ImageView) itemView.findViewById(R.id.up);
            down = (ImageView) itemView.findViewById(R.id.down);
            layout = (LinearLayout) itemView.findViewById(R.id.butons);
            Cant = (TextView) itemView.findViewById(R.id.costcantList);
            delete = (TextView) itemView.findViewById(R.id.deleteList);
        }
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        Articulos.clear();
        if (charText.length() == 0) {
            Articulos.addAll(arraylist);

        } else {
            for (Carrito postDetail : arraylist) {
                if (charText.length() != 0 && postDetail.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) {
                    Articulos.add(postDetail);
                } else if (charText.length() != 0 && postDetail.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) {
                    Articulos.add(postDetail);
                }
            }
        }
        notifyDataSetChanged();
    }
}
