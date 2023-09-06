package com.smeagtechnology.integral;

import androidx.annotation.RequiresApi;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProgresoLinealActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler{
    private int tema = 1;
    private int tarjeta = 1;
    private String accion = "siguiente";
    BillingClient billingClient;
    private BillingProcessor bp;
    private TransactionDetails transactionDetails = null;
    private AdView mAdView;
    private RecyclerView recycler_parrafos;
    List<ParrafosTema> parrafosList;
    ProgressBar progressBar_parrafos;
    TextView textview_mensaje;
    Button button_entendido, button_video, button_anterior;

    @Override
    public boolean onSupportNavigateUp() {

        AlertDialog.Builder Bien = new AlertDialog.Builder(ProgresoLinealActivity.this);
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
        setContentView(R.layout.activity_progreso_lineal);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhVkBHbgKduMwayRRKYxiSpeGagqVecfMHUWWXoqFfWNtmuL/jt0ZwrMa9AVQrD1rlY7G7mCn1rekGszQ+4b9w7SHdOmIMModExiiJ6+AwPK1wi+XueCcdrBuGbwd7fta9v3v7sBJdXLr6zlXN+A+u9ReTdy71UMOV2fPrqt9NGE+qrCUCy3tQWxtw3XUS/bNpJERjBcnzBsB4ckM/46Onm9bkw2PNph7Pb/P5985HlX/b5rEtdgpS7kHQP/uWqFIThLMEPSymzcl76lii1MBLVJrRcWat6FJLY0n0iMfhX8oVVK+8SU1PpiVDt1H5lSH5hfY1x7rba5NNXL/ADmLAwIDAQAB",
                this);
        bp.initialize();
        button_video = findViewById(R.id.button_video_lineal);
        button_entendido = findViewById(R.id.button_entendido_lineal);
        button_anterior = findViewById(R.id.button_anteriror_lineal);
        textview_mensaje = findViewById(R.id.textview_mensaje_lineal);
        progressBar_parrafos = findViewById(R.id.progressBar_parrafos_lineal);
        recycler_parrafos = findViewById(R.id.recycler_parrafos_lineal);
        recycler_parrafos.setHasFixedSize(true);
        recycler_parrafos.setLayoutManager(new LinearLayoutManager(this));
        parrafosList = new ArrayList<>();
        tema = getIntent().getIntExtra("IdTema", 1);

        mAdView = findViewById(R.id.adViewTema_lineal);
        AdRequest adRequest = new AdRequest.Builder().build();
        SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
        final String StateSuscription = preferencesId.getString("State","Not Result");
        mAdView.loadAd(adRequest);
        if(tema >=5){
            resena();
        }
        if(StateSuscription.equals("NO")) {
            if(tema >=13){
                crearDialogoTemaPremium();
            }
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
                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(ProgresoLinealActivity.this, R.anim.fade_transition_animation_sig_));
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
                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(ProgresoLinealActivity.this, R.anim.fade_transition_animation_ant_));
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

        //verifyVideo();
        button_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
                final String StateSuscription = preferencesId.getString("State","Not Result");
                if(StateSuscription.equals("NO")) {
                    AlertDialog.Builder Bien = new AlertDialog.Builder(ProgresoLinealActivity.this);
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
                    //mostrarVideo();
                }
            }
        });
        //verificarProgreso();

    }

    private void resena(){
        SharedPreferences preferencesId = getSharedPreferences("EstadiaSave", Context.MODE_PRIVATE);
        final String EstadiaNumVerifify = preferencesId.getString("ContEstadia","Not Result");
        if(EstadiaNumVerifify.equals("4")){
        }else{
            ShowSheetResena();
        }
    }

    private void ShowSheetResena(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProgresoLinealActivity.this, R.style.BottomSheetDialogTheme);
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

    private void crearDialogoTemaPremium(){
        final Dialog dialog = new Dialog(ProgresoLinealActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alerta_desbloquea_premium);
        dialog.setCancelable(false);
        TextView title_alert = dialog.findViewById(R.id.title_alert_pago);
        TextView descripcion_alert = dialog.findViewById(R.id.description_alert_pago);
        TextView btn_continuar_alert = dialog.findViewById(R.id.btn_continuar_alert_pago);
        TextView btn_cancelar_alert = dialog.findViewById(R.id.btn_cancelar_alert_pago);
        TextView btn_cerrar_alert = dialog.findViewById(R.id.btn_cerrar_alert_pago);
        title_alert.setText("Desbloquea premium!");
        descripcion_alert.setText("(3 días gratis) Desbloquea premium para continuar con tu progreso, y seguir aprendiendo de manera lineal los temas más importantes del cálculo integral.");
        btn_continuar_alert.setText(Html.fromHtml("<p><u>Ver precio.</u></p>"));
        btn_cancelar_alert.setText(Html.fromHtml("<p><u>Cancelar</u></p>"));
        btn_continuar_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bp.isSubscriptionUpdateSupported()){
                    bp.subscribe(ProgresoLinealActivity.this, "suscripcionintegral");
                }else{
                    Toast.makeText(ProgresoLinealActivity.this, "Subscription update is not supported", Toast.LENGTH_SHORT).show();
                }

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
    }


    private void mostrarTema() {
        String  dominio = getString(R.string.app_domain);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerParrafos.php?tema="+tema+"&tarjeta="+tarjeta,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if(array.length() == 0){
                                if(tarjeta == 1) {
                                    if(tema == 6){
                                        tema = tema + 2;
                                        mostrarTema();
                                    }else{
                                        textview_mensaje.setVisibility(View.VISIBLE);
                                        button_entendido.setVisibility(View.GONE);
                                    }

                                }else if(tarjeta > 1){




                                    final Dialog dialog = new Dialog(ProgresoLinealActivity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.setContentView(R.layout.alerta_tema_terminado);
                                    TextView title_alert = dialog.findViewById(R.id.title_alert);
                                    TextView descripcion_alert = dialog.findViewById(R.id.description_alert);
                                    TextView btn_continuar_alert = dialog.findViewById(R.id.btn_continuar_alert);
                                    TextView btn_cancelar_alert = dialog.findViewById(R.id.btn_cancelar_alert);
                                    title_alert.setText("Sigue a la prueba!");
                                    descripcion_alert.setText("Terminaste la explicación del tema, puedes continuar con una prueba para saber que aprendiste.");
                                    btn_continuar_alert.setText(Html.fromHtml("<p><u>Continuar</u></p>"));
                                    btn_cancelar_alert.setText(Html.fromHtml("<p><u>Cancelar</u></p>"));
                                    btn_cancelar_alert.setVisibility(View.GONE);
                                    btn_continuar_alert.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Bundle extras = new Bundle();
                                            extras.putInt("IdTema" ,tema);
                                            //extras.putString("NombreTema" ,getIntent().getStringExtra("NombreTema") );
                                            Intent openActividad = new Intent(ProgresoLinealActivity.this, ActividadProgresoLinealActivity.class);
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
                            AdapterParrafosTema adapter = new AdapterParrafosTema(ProgresoLinealActivity.this, parrafosList);


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
                                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(ProgresoLinealActivity.this, R.anim.fade_transition_animation_sig));
                            }else if(accion.equals("anterior")){
                                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(ProgresoLinealActivity.this, R.anim.fade_transition_animation));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_parrafos.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(ProgresoLinealActivity.this);
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
        String  dominio = getString(R.string.app_domain);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerParrafos.php?tema="+tema+"&tarjeta="+tarjeta,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if(array.length() == 0){
                                if(tarjeta == 1) {
                                    if(tema == 6){
                                        tema = tema + 2;
                                        mostrarTemaPro();
                                    }else{
                                        textview_mensaje.setVisibility(View.VISIBLE);
                                        button_entendido.setVisibility(View.GONE);
                                    }
                                }else if(tarjeta > 1){




                                    final Dialog dialog = new Dialog(ProgresoLinealActivity.this);
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
                                            Bundle extras = new Bundle();
                                            extras.putInt("IdTema" ,tema);
                                            //extras.putString("NombreTema" ,getIntent().getStringExtra("NombreTema") );
                                            Intent openActividad = new Intent(ProgresoLinealActivity.this, ActividadProgresoLinealActivity.class);
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
                            AdapterParrafosTemaPro adapter = new AdapterParrafosTemaPro(ProgresoLinealActivity.this, parrafosList);


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
                                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(ProgresoLinealActivity.this, R.anim.fade_transition_animation_sig));
                            }else if(accion.equals("anterior")){
                                recycler_parrafos.setAnimation(AnimationUtils.loadAnimation(ProgresoLinealActivity.this, R.anim.fade_transition_animation));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_parrafos.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(ProgresoLinealActivity.this);
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

    private void verificarProgreso(){
        int id_tema = getIntent().getIntExtra("IdTema", 0);
        if(id_tema > 0){
            Toast.makeText(this, "Se debería mostrar el siguiente tema", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasSuscription(){

        if(transactionDetails != null) {
            return transactionDetails.purchaseInfo != null;
        }
        return  false;
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        SharedPreferences preferencesName = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
        String State = "SI";
        SharedPreferences.Editor editorName = preferencesName.edit();
        editorName.putString("State", State);
        editorName.commit();
        // Toast.makeText(this, "Hubo una suscripcion exitosa", Toast.LENGTH_SHORT).show();

        SharedPreferences preferencesId = getSharedPreferences("NameUserSave", Context.MODE_PRIVATE);
        final String NameUserR = preferencesId.getString("UserName","Not Result");
        SharedPreferences preferencesEmail = getSharedPreferences("EmailUserSave", Context.MODE_PRIVATE);
        final String Email = preferencesEmail.getString("UserEmail","Not Result");
        String Nombre = NameUserR;
        String Correo = Email;


        new ProgresoLinealActivity.RegistrarSuscripcion(ProgresoLinealActivity.this).execute(Nombre, Correo);

        AlertDialog.Builder Bien = new AlertDialog.Builder(ProgresoLinealActivity.this);
        Bien.setMessage("Has realizado la compra del pase exitosamente, ya tienes acceso a la totalidad de temas en el inventario, tienes acceso a los ejercicios de practica, que te permiten ver una solución muy bien explicada, y desaparece la publicidad (algunos anuncios se verán hasta que cierres y habras la app). No importa si cierras sesión, tu compra seguira activa por siempre.")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog Titulo = Bien.create();
        Titulo.setTitle("Que bien, compra exitosa!");
        Titulo.show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        AlertDialog.Builder Bien = new AlertDialog.Builder(ProgresoLinealActivity.this);
        Bien.setMessage("¿Ha habido un problema? puedes volver a intentarlo.")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Volver a la compra", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bp.subscribe(ProgresoLinealActivity.this, "suscripcionintegral");
                    }
                });
        AlertDialog Titulo = Bien.create();
        Titulo.setTitle("Oh no!");
        Titulo.show();
    }

    @Override
    public void onBillingInitialized() {
        transactionDetails =bp.getSubscriptionTransactionDetails ( "suscripcionintegral" );

        bp.loadOwnedPurchasesFromGoogle();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }



    public  class RegistrarSuscripcion extends AsyncTask<String, Void, String> {
        private WeakReference<Context> context;

        public RegistrarSuscripcion(Context context){
            this.context = new WeakReference<>(context);


        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(String... params){
            String  dominio = getString(R.string.app_domain);

            String registrar_url = "https://"+dominio+"/ApiCalculoIntegralApp/RegistrarSuscripcion.php";
            String resultado;

            try{
                URL url = new URL(registrar_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String Nombre = params[0];
                String Correo = params[1];


                String data = URLEncoder.encode("Nombre", "UTF-8") + "="  + URLEncoder.encode(Nombre, "UTF-8")
                        + "&" + URLEncoder.encode("Correo", "UTF-8") + "=" + URLEncoder.encode(Correo, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                resultado = stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            }catch (MalformedURLException e){
                Log.d("MiAPP", "Se ha utilizado un url con formato incorrecto.");
                resultado = "se ha producido un error";
            }catch (IOException e){
                Log.d("MiAPP", "Erro inesperado!, posibles problemas de conexion de red");
                resultado = "Se ha producido un Error, comprueba tu conexión a internet";

            }return resultado;
        }
        protected void onPostExecute(String resultado){

        }



    }
}