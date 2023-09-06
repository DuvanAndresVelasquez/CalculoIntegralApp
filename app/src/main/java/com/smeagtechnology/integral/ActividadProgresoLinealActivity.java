package com.smeagtechnology.integral;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActividadProgresoLinealActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    private RecyclerView recycler_actividad, recycler_seleccion_respuesta;
    private AdView mAdView;
    List<Actividad> actividadList;
    List<Respuesta> rtaList;
    ProgressBar progressBar_actividad;
    TextView textview_mensaje_actividad;
    @Override
    public boolean onSupportNavigateUp() {
        AlertDialog.Builder Bien = new AlertDialog.Builder(ActividadProgresoLinealActivity.this);
        Bien.setMessage("¿Deseas salir del progreso automatico? El tema no se guardara en tu progreso.")
                .setCancelable(false)
                .setNegativeButton("Seguir progreso", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                });
        AlertDialog Titulo = Bien.create();
        Titulo.setTitle("Retirada!");
        Titulo.show();

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_progreso_lineal);



        SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
        final String StateSuscription = preferencesId.getString("State","Not Result");
        if(StateSuscription.equals("NO")) {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-1778393545986901/3786784656");
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
            mInterstitialAd.setAdListener(new AdListener() {


                public void onAdLoaded() {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }

            });
        }else{

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Estás en una prueba.");
        progressBar_actividad = findViewById(R.id.progressBar_actividad_lineal);

        MobileAds.initialize(getApplicationContext(),"ca-app-pub-1778393545986901/6147007076");

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        mAdView = findViewById(R.id.adView_a_lineal);
        AdRequest adRequest = new AdRequest.Builder().build();
        recycler_actividad = findViewById(R.id.recycler_actividad_lineal);
        recycler_actividad.setHasFixedSize(true);
        recycler_actividad.setLayoutManager(new LinearLayoutManager(this));
        actividadList = new ArrayList<>();
        textview_mensaje_actividad = findViewById(R.id.textview_mensaje_actividad_lineal);
        recycler_seleccion_respuesta = findViewById(R.id.recycler_seleccion_respuesta_lineal);
        recycler_seleccion_respuesta.setHasFixedSize(true);
        recycler_seleccion_respuesta.setLayoutManager(new LinearLayoutManager(this));
        rtaList = new ArrayList<>();
        if(StateSuscription.equals("NO")) {
            mAdView.loadAd(adRequest);
        }else{
            mAdView.setVisibility(View.GONE);
        }
        mostrarActividad();
    }




    private void loadRewardedVideoAd(){
        if(!mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.loadAd("ca-app-pub-1778393545986901/6147007076",  new AdRequest.Builder().build());
        }
    }



    private void mostrarActividad() {
        final int id_tema = getIntent().getIntExtra("IdTema", 0);
        String  dominio = getString(R.string.app_domain);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerActividad.php?tema=" + id_tema,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if(array.length() == 0){
                                int id_tema = getIntent().getIntExtra("IdTema", 0) + 1;
                                Bundle extras = new Bundle();
                                extras.putInt("IdTema" ,id_tema);
                                Intent continuarProgreso = new Intent(ActividadProgresoLinealActivity.this, ProgresoLinealActivity.class);
                                continuarProgreso.putExtras(extras);
                                startActivity(continuarProgreso);
                                finish();
                                textview_mensaje_actividad.setVisibility(View.VISIBLE);
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Temas = array.getJSONObject(i);
                                actividadList.add(new Actividad(
                                        Temas.getInt("IdActividad"),
                                        Temas.getString("EnunciadoActividad"),
                                        Temas.getString("ImagenEnunciado")

                                ));
                            }
                            AdapterActividad adapter = new AdapterActividad(ActividadProgresoLinealActivity.this, actividadList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    progressBar_actividad.setVisibility(View.VISIBLE);


                                    rtaList.clear();

                                    int id_actividad = actividadList.get(recycler_actividad.getChildAdapterPosition(view)).getIdactividad();
                                    mostrarRespuestas(id_actividad);


                                }
                            });
                            recycler_actividad.setAdapter(adapter);
                            progressBar_actividad.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_actividad.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(ActividadProgresoLinealActivity.this);
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
                                actividadList.clear();
                                progressBar_actividad.setVisibility(View.VISIBLE);
                                mostrarActividad();
                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Error de conexión");
                Titulo.show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }






    private void mostrarRespuestas(int id_actividad) {
        String  dominio = getString(R.string.app_domain);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerRespuestas.php?actividad="+ id_actividad,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Temas = array.getJSONObject(i);
                                rtaList.add(new Respuesta(
                                        Temas.getInt("IdRtaActividad"),
                                        Temas.getString("NombreRta"),
                                        Temas.getString("ImagenRta"),
                                        Temas.getInt("IdSolucionRta")

                                ));
                            }
                            AdapterRespuesta adapter = new AdapterRespuesta(ActividadProgresoLinealActivity.this, rtaList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(rtaList.get(recycler_seleccion_respuesta.getChildAdapterPosition(view)).getIdsolucionrta() == 1){

                                        SharedPreferences preferencesName = getSharedPreferences("ProgresoLineal", Context.MODE_PRIVATE);
                                        int Tema = getIntent().getIntExtra("IdTema", 0)+1;
                                        String Tema_progreso = String.valueOf(Tema);
                                        SharedPreferences.Editor editorName = preferencesName.edit();
                                        editorName.putString("Temaprogreso", Tema_progreso);
                                        editorName.commit();
                                        final Dialog dialog = new Dialog(ActividadProgresoLinealActivity.this);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialog.setContentView(R.layout.alerta_eleccion_bien);
                                        TextView title_alert = dialog.findViewById(R.id.title_alert_e_bien);
                                        TextView descripcion_alert = dialog.findViewById(R.id.description_alert_e_bien);
                                        TextView btn_continuar_alert = dialog.findViewById(R.id.btn_continuar_alert_e_bien);
                                        TextView btn_cancelar_alert = dialog.findViewById(R.id.btn_cancelar_alert_e_bien);
                                        TextView btn_cerrar_alert = dialog.findViewById(R.id.btn_cerrar_alert_e_bien);
                                        title_alert.setText("Excelente!");
                                        descripcion_alert.setText("La respuesta que has seleccionado es correcta, se guardó tu progreso, ya puedes continuar con el siguiente tema.");
                                        btn_continuar_alert.setText(Html.fromHtml("<p><u>Continuar</u></p>"));
                                        btn_cancelar_alert.setVisibility(View.GONE);
                                        btn_cancelar_alert.setText(Html.fromHtml("<p><u>Salir</u></p>"));
                                        btn_continuar_alert.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int id_tema = getIntent().getIntExtra("IdTema", 0) + 1;
                                                Bundle extras = new Bundle();
                                                extras.putInt("IdTema" ,id_tema);
                                                Intent continuarProgreso = new Intent(ActividadProgresoLinealActivity.this, ProgresoLinealActivity.class);
                                                continuarProgreso.putExtras(extras);
                                                startActivity(continuarProgreso);
                                                finish();
                                            }
                                        });
                                        btn_cancelar_alert.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                finish();
                                            }
                                        });
                                        btn_cerrar_alert.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                finish();
                                            }
                                        });
                                        dialog.show();





                                    }else{
                                        recycler_seleccion_respuesta.setVisibility(View.GONE);



                                        final Dialog dialog = new Dialog(ActividadProgresoLinealActivity.this);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialog.setContentView(R.layout.alerta_eleccion_erronea);
                                        dialog.setCancelable(false);
                                        TextView title_alert = dialog.findViewById(R.id.title_alert_e_erronea);
                                        TextView descripcion_alert = dialog.findViewById(R.id.description_alert_e_erronea);
                                        TextView btn_continuar_alert = dialog.findViewById(R.id.btn_continuar_alert_e_erronea);
                                        TextView btn_cancelar_alert = dialog.findViewById(R.id.btn_cancelar_alert_e_erronea);
                                        TextView btn_cerrar_alert = dialog.findViewById(R.id.btn_cerrar_alert_e_erronea);
                                        title_alert.setText("Que mal!");
                                        descripcion_alert.setText("La respuesta que seleccionaste es incorrecta, puedes volver a intentarlo viendo un video.");
                                        btn_continuar_alert.setText(Html.fromHtml("<p><u>Ver video</u></p>"));
                                        btn_cancelar_alert.setText(Html.fromHtml("<p><u>Repasar</u></p>"));
                                        btn_continuar_alert.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                loadRewardedVideoAd();
                                                if(mRewardedVideoAd.isLoaded()){
                                                    mRewardedVideoAd.show();
                                                    dialog.dismiss();
                                                }else{
                                                    Toast.makeText(ActividadProgresoLinealActivity.this, "El video no ha cargado, click de nuevo", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        btn_cancelar_alert.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                SharedPreferences preferencesId = getSharedPreferences("ProgresoLineal", Context.MODE_PRIVATE);
                                                final String progreso = preferencesId.getString("Temaprogreso","Not Result");
                                                if(progreso.equals("Not Result")){
                                                    Intent progresoLineal = new Intent(ActividadProgresoLinealActivity.this, ProgresoLinealActivity.class);
                                                    startActivity(progresoLineal);
                                                    Toast.makeText(ActividadProgresoLinealActivity.this, "Vamos a repasar.", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }else{
                                                    int progreso_id = Integer.parseInt(progreso);
                                                    Bundle extras = new Bundle();
                                                    extras.putInt("IdTema" ,progreso_id);
                                                    Intent continuarProgreso = new Intent(ActividadProgresoLinealActivity.this, ProgresoLinealActivity.class);
                                                    continuarProgreso.putExtras(extras);
                                                    startActivity(continuarProgreso);
                                                    Toast.makeText(ActividadProgresoLinealActivity.this, "Vamos a repasar.", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                        });
                                        btn_cerrar_alert.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                SharedPreferences preferencesId = getSharedPreferences("ProgresoLineal", Context.MODE_PRIVATE);
                                                final String progreso = preferencesId.getString("Temaprogreso","Not Result");
                                                if(progreso.equals("Not Result")){
                                                    Intent progresoLineal = new Intent(ActividadProgresoLinealActivity.this, ProgresoLinealActivity.class);
                                                    startActivity(progresoLineal);
                                                }else{
                                                    int progreso_id = Integer.parseInt(progreso);
                                                    Bundle extras = new Bundle();
                                                    extras.putInt("IdTema" ,progreso_id);
                                                    Intent continuarProgreso = new Intent(ActividadProgresoLinealActivity.this, ProgresoLinealActivity.class);
                                                    continuarProgreso.putExtras(extras);
                                                    startActivity(continuarProgreso);
                                                    Toast.makeText(ActividadProgresoLinealActivity.this, "Vamos a repasar.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        dialog.show();
                                    }
                                }
                            });
                            recycler_seleccion_respuesta.setAdapter(adapter);
                            progressBar_actividad.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_actividad.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(ActividadProgresoLinealActivity.this);
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
                                rtaList.clear();
                                progressBar_actividad.setVisibility(View.VISIBLE);
                                mostrarActividad();
                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Error de conexión");
                Titulo.show();
            }
        });
        Volley.newRequestQueue(ActividadProgresoLinealActivity.this).add(stringRequest);


    }


    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        recycler_seleccion_respuesta.setVisibility(View.VISIBLE);
        loadRewardedVideoAd();
        Toast.makeText(this, "Ya puedes volver a intentarlo.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "No se ha podido cargar el video, intentalo de nuevo.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        recycler_seleccion_respuesta.setVisibility(View.VISIBLE);
        loadRewardedVideoAd();
        Toast.makeText(this, "Ya puedes volver a intentarlo.", Toast.LENGTH_LONG).show();
    }
}