package com.smeagtechnology.integral;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class AdapterTemasPro extends RecyclerView.Adapter<AdapterTemasPro.ProductsViewHolder> implements View.OnClickListener{

    private View.OnClickListener listener;
    private Context miCtexttemas;
    private List<Temas> temaslist;

    public AdapterTemasPro(Context miCtexttemas, List<Temas>temaslist){
        this.miCtexttemas=miCtexttemas;
        this.temaslist=temaslist;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(miCtexttemas);
        View view = inflater.inflate(R.layout.listartema, null);
        view.setOnClickListener(this);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {


        Temas productosCategoria = temaslist.get(position);
        holder.setIsRecyclable(false);
        //holder.container_tema.setAnimation(AnimationUtils.loadAnimation(miCtexttemas, R.anim.fade_transition_animation));
        byte[] decodeString  = Base64.decode(productosCategoria.getFototema(), Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        Glide.with(miCtexttemas)
                .load(decodeByte)
                .into(holder.imagep);
        holder.tv1p.setText(productosCategoria.getNombretema());
        holder.tv2p.setText(productosCategoria.getDescripciontema());
        String suscripcion = productosCategoria.getSuscripcion();
        if (suscripcion.equals("NO")){

        }else {
            holder.tv3p.setText("Ver Tema - Premium");
        }
        int res = productosCategoria.getIdtema() % 2;




    }

    @Override
    public int getItemCount() {
        return temaslist.size();
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }


    class ProductsViewHolder extends RecyclerView.ViewHolder{
        TextView tv1p, tv2p, tv3p;
        ImageView imagep;
        RelativeLayout container_tema;
        private AdView mAdView;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1p = itemView.findViewById(R.id.tv1p);
            tv2p = itemView.findViewById(R.id.tv2p);
            tv3p = itemView.findViewById(R.id.tv3p);
            imagep = itemView.findViewById(R.id.imgp);
            container_tema = itemView.findViewById(R.id.container_tema);
            mAdView = itemView.findViewById(R.id.adView_t);
        }
    }
}
