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

public class AdapterParrafosTemaPro extends RecyclerView.Adapter<AdapterParrafosTemaPro.ProductsViewHolder> implements View.OnClickListener{
    private View.OnClickListener listener;
    private Context miCtexttemas;
    private List<ParrafosTema> parrafoslist;

    public AdapterParrafosTemaPro(Context miCtexttemas, List<ParrafosTema>parrafoslist){
        this.miCtexttemas=miCtexttemas;
        this.parrafoslist=parrafoslist;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(miCtexttemas);
        View view = inflater.inflate(R.layout.listarparrafos, null);
view.setOnClickListener(this);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        ParrafosTema productosCategoria = parrafoslist.get(position);
        //holder.container_parrafo.setAnimation(AnimationUtils.loadAnimation(miCtexttemas, R.anim.fade_transition_animation));

        byte[] decodeString  = Base64.decode(productosCategoria.getImagenparrafo(), Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        Glide.with(miCtexttemas)
                .load(decodeByte)
                .into(holder.img_ecuation);

        holder.tv2parrafo.setText(productosCategoria.getContenidoparrafo());

    if(productosCategoria.getImagenparrafo().equals("")){
        holder.img_ecuation.setVisibility(View.GONE);
        holder.ver.setVisibility(View.GONE);
    }

    }



    @Override
    public int getItemCount() {
        return parrafoslist.size();
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }


    class ProductsViewHolder extends RecyclerView.ViewHolder{
        private AdView mAdView;
        TextView  tv2parrafo, ver;
        ImageView img_ecuation;
        RelativeLayout  container_parrafo;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv2parrafo = itemView.findViewById(R.id.tv2parrafo);
            img_ecuation=itemView.findViewById(R.id.ecuation);
            ver = itemView.findViewById(R.id.ver);
            container_parrafo = itemView.findViewById(R.id.container_parrafo);
            mAdView = itemView.findViewById(R.id.adView_p);

        }
    }


}
