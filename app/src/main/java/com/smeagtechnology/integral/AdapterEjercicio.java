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

public class AdapterEjercicio extends RecyclerView.Adapter<AdapterEjercicio.ProductsViewHolder> implements View.OnClickListener{
    private View.OnClickListener listener;
    private Context miCtextejercicio;
    private List<Ejercicio> ejerciciolist;

    public AdapterEjercicio(Context miCtextejercicio, List<Ejercicio>ejerciciolist){
        this.miCtextejercicio=miCtextejercicio;
        this.ejerciciolist=ejerciciolist;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(miCtextejercicio);
        View view = inflater.inflate(R.layout.listarejercicio, null);
view.setOnClickListener(this);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Ejercicio productosCategoria = ejerciciolist.get(position);
        byte[] decodeString  = Base64.decode(productosCategoria.getFotoejercicio(), Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        Glide.with(miCtextejercicio)
                .load(decodeByte)
                .into(holder.img_ejercicio);
        holder.textview_enunciado_ejercicio.setText(productosCategoria.getNombreejercicio());
    }

    @Override
    public int getItemCount() {
        return ejerciciolist.size();
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
        ImageView img_ejercicio;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
          textview_enunciado_ejercicio = itemView.findViewById(R.id.textview_enunciado_ejercicio);
            img_ejercicio = itemView.findViewById(R.id.img_ejercicio);
        }
    }
}
