package com.example.miguelsoler.mipedidos.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguelsoler.mipedidos.Configs.Config;
import com.example.miguelsoler.mipedidos.Configs.DBHelper;
import com.example.miguelsoler.mipedidos.POJO.Articulo;
import com.example.miguelsoler.mipedidos.POJO.Carrito;
import com.example.miguelsoler.mipedidos.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
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
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    Activity activity;
    private List<Articulo> Articulos;
    private ArrayList<Articulo> arraylist;

    String Tag = "RecordAdapter";

    private DBHelper databaseHelper = null;

    public RecordAdapter(Activity activity, List<Articulo> A) {
        this.activity = activity;
        this.Articulos = A;
        arraylist = new ArrayList<Articulo>();
        arraylist.addAll(Articulos);
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.records_item, parent, false);
        return new RecordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, final int position) {
        Articulo articulos = new Articulo();
        articulos = Articulos.get(position);

        final String Name = articulos.getNombre();
        final String Desc = articulos.getDescripcion();
        final String Costo = String.valueOf(articulos.getCosto());

        final int foto = articulos.getImagen(position);

        holder.Nombre.setText(Name);
        holder.Descripcion.setText(Desc);
        holder.Costo.setText(Costo);

        holder.Foto.setImageResource(foto);

        holder.Carrito.setVisibility(View.VISIBLE);

        holder.Carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("DATA", "DATA");
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.show();

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
                                    Log.e("Int:", String.valueOf(newC));
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
                    }
                });
            }
        });
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
        TextView Nombre, Descripcion, Costo, Carrito;
        ImageView Foto;


        public RecordViewHolder(View itemView) {
            super(itemView);
            Nombre = (TextView) itemView.findViewById(R.id.namecateList);
            Descripcion = (TextView) itemView.findViewById(R.id.desccateList);
            Costo = (TextView) itemView.findViewById(R.id.costcateList);
            Foto = (ImageView) itemView.findViewById(R.id.logocateList);
            Carrito = (TextView) itemView.findViewById(R.id.costcarritoList);
        }
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        Articulos.clear();
        if (charText.length() == 0) {
            Articulos.addAll(arraylist);

        } else {
            for (Articulo postDetail : arraylist) {
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
