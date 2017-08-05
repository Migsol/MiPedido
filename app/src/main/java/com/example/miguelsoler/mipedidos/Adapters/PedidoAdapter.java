package com.example.miguelsoler.mipedidos.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.miguelsoler.mipedidos.Configs.Config;
import com.example.miguelsoler.mipedidos.Configs.DBHelper;
import com.example.miguelsoler.mipedidos.POJO.Pedido;
import com.example.miguelsoler.mipedidos.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  30/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/
public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private List<Pedido> Articulos;
    private ArrayList<Pedido> arraylist;
    private Activity activity;

    private int Sum;

    public Config config;

    public PedidoAdapter(Activity AC, List<Pedido> A) {
        this.activity = AC;
        this.Articulos = A;
        arraylist = new ArrayList<Pedido>();
        arraylist.addAll(Articulos);
    }

    private DBHelper databaseHelper = null;

    @Override
    public PedidoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carrito_item ,parent, false);
        return new PedidoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PedidoViewHolder holder, final int position) {

        Pedido articulos = new Pedido();
        articulos = Articulos.get(position);

        String codigo = articulos.getCodigo();
        String Fecha = articulos.getFecha();
        String Hora = articulos.getHora();
        String Cantidad = String.valueOf(articulos.getNumItems());
        String Total = String.valueOf(articulos.getCostoTotal());

        holder.Codigo.setText(codigo);
        holder.Fecha.setText(Fecha);
        holder.Hora.setText(Hora);
        holder.Cantidad.setText(Cantidad);
        holder.Total.setText(Total);

    }
    @Override
    public int getItemCount() {
        return Articulos.size();
    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder {

        TextView Codigo, Fecha, Hora, Total, Cantidad;

        public PedidoViewHolder(View itemView) {
            super(itemView);
            Codigo = (TextView)itemView.findViewById(R.id.campo_codigopedido);
            Fecha = (TextView)itemView.findViewById(R.id.campo_fechapedido);
            Hora = (TextView)itemView.findViewById(R.id.campo_horapedido);
            Cantidad = (TextView)itemView.findViewById(R.id.campo_itemspedido);
            Total = (TextView)itemView.findViewById(R.id.campo_totalpedido);

        }
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        Articulos.clear();
        if (charText.length() == 0) {
            Articulos.addAll(arraylist);

        } else {
            for (Pedido postDetail : arraylist) {
                if (charText.length() != 0 && postDetail.getCodigo().toLowerCase(Locale.getDefault()).contains(charText)) {
                    Articulos.add(postDetail);
                } else if (charText.length() != 0 && postDetail.getCodigo().toLowerCase(Locale.getDefault()).contains(charText)) {
                    Articulos.add(postDetail);
                }
            }
        }
        notifyDataSetChanged();
    }
}
