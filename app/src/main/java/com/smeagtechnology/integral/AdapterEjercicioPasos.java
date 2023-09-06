package com.smeagtechnology.integral;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterEjercicioPasos extends RecyclerView.Adapter<AdapterEjercicioPasos.ProductsViewHolder> implements View.OnClickListener{
    private View.OnClickListener listener;
    private Context miCtextep;
    private List<EjerciciosPasos> eplist;

    public AdapterEjercicioPasos(Context miCtextep, List<EjerciciosPasos>eplist){
        this.miCtextep=miCtextep;
        this.eplist=eplist;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(miCtextep);
        View view = inflater.inflate(R.layout.listarejerciciopasos, null);
        view.setOnClickListener(this);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        EjerciciosPasos productosCategoria = eplist.get(position);

        //holder.contenedor_ejerciciospasos.setAnimation(AnimationUtils.loadAnimation(miCtextep, R.anim.fade_transition_animation));
        byte[] decodeString  = Base64.decode(productosCategoria.getImagenejerciciointegralpasos(), Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        Glide.with(miCtextep)
               .load(decodeByte)
                .into(holder.img_ejercicio_pasos);
        holder.descripcion_ejercicio_pasos.setText(productosCategoria.getDescripcionejerciciointegralpasos());
    }

    @Override
    public int getItemCount() {
        return eplist.size();
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }


    class ProductsViewHolder extends RecyclerView.ViewHolder{
        TextView descripcion_ejercicio_pasos;
        ImageView img_ejercicio_pasos;
        //RelativeLayout contenedor_respuesta;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            descripcion_ejercicio_pasos = itemView.findViewById(R.id.descripcion_ejercicio_pasos);
            img_ejercicio_pasos = itemView.findViewById(R.id.img_ejercicio_pasos);
           // contenedor_respuesta = itemView.findViewById(R.id.contenedor_respuestas);
        }
    }
}
