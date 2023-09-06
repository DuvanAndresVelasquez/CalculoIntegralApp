package com.smeagtechnology.integral;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterSolucionPP extends RecyclerView.Adapter<AdapterSolucionPP.ProductsViewHolder> implements View.OnClickListener{
    private View.OnClickListener listener;
    private Context miCtextsolucion;
    private List<SolucionPP> solucionpplist;

    public AdapterSolucionPP(Context miCtextsolucion, List<SolucionPP>solucionpplist){
        this.miCtextsolucion=miCtextsolucion;
        this.solucionpplist=solucionpplist;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(miCtextsolucion);
        View view = inflater.inflate(R.layout.listarsolucionpp, null);
view.setOnClickListener(this);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
       SolucionPP productosCategoria = solucionpplist.get(position);
        byte[] decodeString  = Base64.decode(productosCategoria.getImagenejerciciointegralpasos(), Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        Glide.with(miCtextsolucion)
                .load(decodeByte)
                .into(holder.img_ejercicio_solucionpp);
        //holder.textview_enunciado_ejercicio.setText(productosCategoria.getNombreejercicio());
    }

    @Override
    public int getItemCount() {
        return solucionpplist.size();
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }


    class ProductsViewHolder extends RecyclerView.ViewHolder{
        TextView textview_enunciado_ejercicio;
        ImageView img_ejercicio_solucionpp;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            textview_enunciado_ejercicio = itemView.findViewById(R.id.textview_enunciado_ejercicio);
            img_ejercicio_solucionpp = itemView.findViewById(R.id.ecuation_solucion_pp);
        }
    }
}
