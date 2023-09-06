package com.smeagtechnology.integral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TemaEleccionActivity extends AppCompatActivity {
    private int tarjeta = 1;
    private String accion = "siguiente";
    private AdView mAdView;
    private RecyclerView recycler_parrafos;
    List<ParrafosTema> parrafosList;
    ProgressBar progressBar_parrafos;
    TextView textview_mensaje;
    Button button_entendido, button_video, button_anterior;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tema_eleccion);
        final String title_name = getIntent().getStringExtra("NombreTema");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(title_name);
        button_video = findViewById(R.id.button_video);
        button_entendido = findViewById(R.id.button_entendido);
        button_anterior = findViewById(R.id.button_anteriror);
        textview_mensaje = findViewById(R.id.textview_mensaje);
        progressBar_parrafos = findViewById(R.id.progressBar_parrafos);
        recycler_parrafos = findViewById(R.id.recycler_parrafos);
        recycler_parrafos.setHasFixedSize(true);
        recycler_parrafos.setLayoutManager(new LinearLayoutManager(this));
        parrafosList = new ArrayList<>();

        mAdView = findViewById(R.id.adViewTema);
        AdRequest adRequest = new AdRequest.Builder().build();
        SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
        final String StateSuscription = preferencesId.getString("State","Not Result");
        mAdView.loadAd(adRequest);
        if(StateSuscription.equals("NO")) {

        }else{
            mAdView.setVisibility(View.GONE);
        }
        if(StateSuscription.equals("NO")) {
            mostrarTema();
        }
        else{
            mostrarTemaPro();
        }
        button_entendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accion = "siguiente";
                tarjeta = tarjeta+1;
                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(TemaEleccionActivity.this, R.anim.fade_transition_animation_sig_));
                parrafosList.clear();
                progressBar_parrafos.setVisibility(View.VISIBLE);
                recycler_parrafos.setVisibility(View.GONE);
                if(StateSuscription.equals("NO")) {
                    mostrarTema();
                }
                else{
                    mostrarTemaPro();
                }


            }
        });
        button_anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accion = "anterior";
                tarjeta = tarjeta-1;
                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(TemaEleccionActivity.this, R.anim.fade_transition_animation_ant_));
                parrafosList.clear();
                progressBar_parrafos.setVisibility(View.VISIBLE);
                recycler_parrafos.setVisibility(View.GONE);
                if(StateSuscription.equals("NO")) {
                    mostrarTema();
                }
                else{
                    mostrarTemaPro();
                }
            }
        });

        verifyVideo();
        button_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
                final String StateSuscription = preferencesId.getString("State","Not Result");
                if(StateSuscription.equals("NO")) {
                    AlertDialog.Builder Bien = new AlertDialog.Builder(TemaEleccionActivity.this);
                    Bien.setMessage("Para ver los videos complementarios, debes adquirir el pase premium, y desbloquearas todas la funcionalidades premium que tenemos para ti.")
                            .setCancelable(false)
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("Ver precio", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    AlertDialog Titulo = Bien.create();
                    Titulo.setTitle("Adquiere el pase!");
                    Titulo.show();
                }else{
                    mostrarVideo();
                }
            }
        });
        guardarEstadia();

    }


    private void guardarEstadia(){


        SharedPreferences preferencesId = getSharedPreferences("EstadiaSave", Context.MODE_PRIVATE);
        final String EstadiaNumVerifify = preferencesId.getString("ContEstadia","Not Result");

        if(EstadiaNumVerifify.equals("Not Result")){
            SharedPreferences preferencesEstadia = getSharedPreferences("EstadiaSave", Context.MODE_PRIVATE);
            String EstadiaNum = "1";
            SharedPreferences.Editor editorEstadia = preferencesEstadia.edit();
            editorEstadia.putString("ContEstadia", EstadiaNum);
            editorEstadia.commit();
        }else{
            int EstadiaNumVerifiyNumInt = Integer.parseInt(EstadiaNumVerifify);
            if(EstadiaNumVerifiyNumInt ==3){
                ShowSheetResena();
            }else if(EstadiaNumVerifiyNumInt < 3){
                int EstadiaNumVerifiyNumIntAdd = EstadiaNumVerifiyNumInt+1;
                String EtadiaFinal = String.valueOf(EstadiaNumVerifiyNumIntAdd);
                SharedPreferences preferencesEstadia = getSharedPreferences("EstadiaSave", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorEstadia = preferencesEstadia.edit();
                editorEstadia.putString("ContEstadia", EtadiaFinal);
                editorEstadia.commit();
            }

        }



    }

    private void ShowSheetResena(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TemaEleccionActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet_resena,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer_resena)
                );
        RatingBar ratingBar = bottomSheetView.findViewById(R.id.ratingBar);
        ratingBar.setEnabled(false);
        Button calificacion = bottomSheetView.findViewById(R.id.button_calificar);
        calificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferencesEstadia = getSharedPreferences("EstadiaSave", Context.MODE_PRIVATE);
                String EstadiaNum = "4";
                SharedPreferences.Editor editorEstadia = preferencesEstadia.edit();
                editorEstadia.putString("ContEstadia", EstadiaNum);
                editorEstadia.commit();
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.smeagtechnology.integral");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void verifyVideo(){
        if(getIntent().getStringExtra("NombreVideo").equals("No video")){
            button_video.setVisibility(View.GONE);
        }
    }


    private void mostrarVideo(){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TemaEleccionActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet_video,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer_video)
                );
        TextView nombre = bottomSheetView.findViewById(R.id.nombre_bottomSheet_video);
        nombre.setText(getIntent().getStringExtra("NombreTema"));
        YouTubePlayerView youTubePlayerView = bottomSheetView.findViewById(R.id.youtube_player_view_bottomSheet);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(@NonNull final YouTubePlayer youTubePlayer) {
                youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        String videoId = getIntent().getStringExtra("NombreVideo");
                        youTubePlayer.loadVideo(videoId,0);
                    }
                });
            }
        }, true);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();





    }


    private void mostrarTema() {
        final int id_tema = getIntent().getIntExtra("IdTema", 0);
        String  dominio = getString(R.string.app_domain);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerParrafos.php?tema="+id_tema+"&tarjeta="+tarjeta,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if(array.length() == 0){
                                if(tarjeta == 1) {
                                    textview_mensaje.setVisibility(View.VISIBLE);
                                    button_entendido.setVisibility(View.GONE);
                                }else if(tarjeta > 1){




                                    final Dialog dialog = new Dialog(TemaEleccionActivity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.setContentView(R.layout.alerta_tema_terminado);
                                    TextView title_alert = dialog.findViewById(R.id.title_alert);
                                    TextView descripcion_alert = dialog.findViewById(R.id.description_alert);
                                    TextView btn_continuar_alert = dialog.findViewById(R.id.btn_continuar_alert);
                                    TextView btn_cancelar_alert = dialog.findViewById(R.id.btn_cancelar_alert);
                                    title_alert.setText("Tema " + getIntent().getStringExtra("NombreTema") + " terminado!");
                                    descripcion_alert.setText("Terminaste la explicación del tema, puedes continuar con una prueba para saber que aprendiste.");
                                    btn_continuar_alert.setText(Html.fromHtml("<p><u>Continuar</u></p>"));
                                    btn_cancelar_alert.setText(Html.fromHtml("<p><u>Cancelar</u></p>"));
                                    btn_cancelar_alert.setVisibility(View.GONE);
                                    btn_continuar_alert.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final int id_tema = getIntent().getIntExtra("IdTema", 0);
                                            Bundle extras = new Bundle();
                                            extras.putInt("IdTema" ,id_tema);
                                            extras.putString("NombreTema" ,getIntent().getStringExtra("NombreTema") );
                                            Intent openActividad = new Intent(TemaEleccionActivity.this, ActividadActivity.class);
                                            openActividad.putExtras(extras);
                                            startActivity(openActividad);
                                            finish();
                                        }
                                    });
                                    btn_cancelar_alert.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();






                                }
                            }else{
                                if(tarjeta == 1){
                                    button_anterior.setVisibility(View.GONE);
                                }else if(tarjeta > 1){
                                    button_anterior.setVisibility(View.VISIBLE);
                                }
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Parrafos = array.getJSONObject(i);
                                parrafosList.add(new ParrafosTema(
                                        Parrafos.getInt("IdParrafo"),
                                        Parrafos.getString("ContenidoParrafo"),
                                        Parrafos.getString("ImagenParrafo")

                                ));
                            }
                            AdapterParrafosTema adapter = new AdapterParrafosTema(TemaEleccionActivity.this, parrafosList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(parrafosList.get(recycler_parrafos.getChildAdapterPosition(view)).getImagenparrafo().equals("")){
                                        }else{

                                        }
                                }
                            });
                            recycler_parrafos.setAdapter(adapter);
                            progressBar_parrafos.setVisibility(View.INVISIBLE);
                            recycler_parrafos.setVisibility(View.VISIBLE);
                            if(accion.equals("siguiente")){
                                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(TemaEleccionActivity.this, R.anim.fade_transition_animation_sig));
                            }else if(accion.equals("anterior")){
                                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(TemaEleccionActivity.this, R.anim.fade_transition_animation));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_parrafos.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(TemaEleccionActivity.this);
                Bien.setMessage("Parece que hay problemas en la conexión a internet.")
                        .setCancelable(false)
                        .setNegativeButton("Salir", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                parrafosList.clear();
                                progressBar_parrafos.setVisibility(View.VISIBLE);
                                mostrarTema();
                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Error de conexión");
                Titulo.show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }




    private void mostrarTemaPro() {
        final int id_tema = getIntent().getIntExtra("IdTema", 0);
        String  dominio = getString(R.string.app_domain);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerParrafos.php?tema="+id_tema+"&tarjeta="+tarjeta,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if(array.length() == 0){
                                if(tarjeta == 1) {
                                    textview_mensaje.setVisibility(View.VISIBLE);
                                    button_entendido.setVisibility(View.GONE);
                                }else if(tarjeta > 1){




                                    final Dialog dialog = new Dialog(TemaEleccionActivity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.setContentView(R.layout.alerta_tema_terminado);
                                    TextView title_alert = dialog.findViewById(R.id.title_alert);
                                    TextView descripcion_alert = dialog.findViewById(R.id.description_alert);
                                    TextView btn_continuar_alert = dialog.findViewById(R.id.btn_continuar_alert);
                                    TextView btn_cancelar_alert = dialog.findViewById(R.id.btn_cancelar_alert);
                                    title_alert.setText("Tema " + getIntent().getStringExtra("NombreTema") + " terminado!");
                                    descripcion_alert.setText("Terminaste la explicación del tema, puedes continuar con una prueba para saber que aprendiste.");
                                    btn_continuar_alert.setText(Html.fromHtml("<p><u>Continuar</u></p>"));
                                    btn_cancelar_alert.setText(Html.fromHtml("<p><u>Cancelar</u></p>"));
                                    btn_cancelar_alert.setVisibility(View.GONE);
                                    btn_continuar_alert.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final int id_tema = getIntent().getIntExtra("IdTema", 0);
                                            Bundle extras = new Bundle();
                                            extras.putInt("IdTema" ,id_tema);
                                            extras.putString("NombreTema" ,getIntent().getStringExtra("NombreTema") );
                                            Intent openActividad = new Intent(TemaEleccionActivity.this, ActividadActivity.class);
                                            openActividad.putExtras(extras);
                                            startActivity(openActividad);
                                            finish();
                                        }
                                    });
                                    btn_cancelar_alert.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();






                                }
                            }else{
                                if(tarjeta == 1){
                                    button_anterior.setVisibility(View.GONE);
                                }else if(tarjeta > 1){
                                    button_anterior.setVisibility(View.VISIBLE);
                                }
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Parrafos = array.getJSONObject(i);
                                parrafosList.add(new ParrafosTema(
                                        Parrafos.getInt("IdParrafo"),
                                        Parrafos.getString("ContenidoParrafo"),
                                        Parrafos.getString("ImagenParrafo")

                                ));
                            }
                            AdapterParrafosTemaPro adapter = new AdapterParrafosTemaPro(TemaEleccionActivity.this, parrafosList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(parrafosList.get(recycler_parrafos.getChildAdapterPosition(view)).getImagenparrafo().equals("")){
                                    }else{

                                    }
                                }
                            });
                            recycler_parrafos.setAdapter(adapter);
                            progressBar_parrafos.setVisibility(View.INVISIBLE);
                            recycler_parrafos.setVisibility(View.VISIBLE);
                            if(accion.equals("siguiente")){
                                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(TemaEleccionActivity.this, R.anim.fade_transition_animation_sig));
                            }else if(accion.equals("anterior")){
                                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(TemaEleccionActivity.this, R.anim.fade_transition_animation));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_parrafos.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(TemaEleccionActivity.this);
                Bien.setMessage("Parece que hay problemas en la conexión a internet.")
                        .setCancelable(false)
                        .setNegativeButton("Salir", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                parrafosList.clear();
                                progressBar_parrafos.setVisibility(View.VISIBLE);
                                mostrarTema();
                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Error de conexión");
                Titulo.show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }
}