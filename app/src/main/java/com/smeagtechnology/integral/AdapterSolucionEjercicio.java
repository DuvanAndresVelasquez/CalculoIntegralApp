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

public class AdapterSolucionEjercicio extends RecyclerView.Adapter<AdapterSolucionEjercicio.ProductsViewHolder> implements View.OnClickListener{
    private View.OnClickListener listener;
    private Context miCtexttemas;
    private List<SolucionEjercicio> solucionlist;

    public AdapterSolucionEjercicio(Context miCtexttemas, List<SolucionEjercicio>solucionlist){
        this.miCtexttemas=miCtexttemas;
        this.solucionlist=solucionlist;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(miCtexttemas);
        View view = inflater.inflate(R.layout.listarsolucion, null);
view.setOnClickListener(this);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        SolucionEjercicio productosCategoria = solucionlist.get(position);
        holder.container_parrafo.setAnimation(AnimationUtils.loadAnimation(miCtexttemas, R.anim.fade_transition_animation));

        byte[] decodeString  = Base64.decode(productosCategoria.getFotopaso(), Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        Glide.with(miCtexttemas)
                .load(decodeByte)
                .into(holder.img_solucion);

        holder.tv2explicacion.setText(productosCategoria.getExplicacionpaso());


    }

    @Override
    public int getItemCount() {
        return solucionlist.size();
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }


    class ProductsViewHolder extends RecyclerView.ViewHolder{
        TextView  tv2explicacion;
        ImageView img_solucion;
        RelativeLayout  container_parrafo;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv2explicacion = itemView.findViewById(R.id.tv2explicacion);
            img_solucion=itemView.findViewById(R.id.ecuation_solucion);

            container_parrafo = itemView.findViewById(R.id.container_solucion);

        }
    }


}
