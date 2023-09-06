package com.smeagtechnology.integral;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.ads.mediationtestsuite.MediationTestSuite;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

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

import javax.crypto.ShortBufferException;

public class PrincipalPageActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler, RewardedVideoAdListener {
    private String BloqueoTemas = "Bloqueado";
    BillingClient billingClient;
    private AlertDialog.Builder  builderDialog;
    private RewardedVideoAd mRewardedVideoAd;
    private FloatingActionButton btnExercise, btnExercisePasos;
    private BillingProcessor bp;
    private TransactionDetails transactionDetails = null;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private RecyclerView recycler_temas;
    List<Temas> temasList;
    ProgressBar progressBar_temas;
    Button button_proceso, button_progreso;
    ImageView btn_sheet;
    TextView help_banner, mensaje_alert, titulo_alert;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        //MediationTestSuite.launch(PrincipalPageActivity.this);
        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhVkBHbgKduMwayRRKYxiSpeGagqVecfMHUWWXoqFfWNtmuL/jt0ZwrMa9AVQrD1rlY7G7mCn1rekGszQ+4b9w7SHdOmIMModExiiJ6+AwPK1wi+XueCcdrBuGbwd7fta9v3v7sBJdXLr6zlXN+A+u9ReTdy71UMOV2fPrqt9NGE+qrCUCy3tQWxtw3XUS/bNpJERjBcnzBsB4ckM/46Onm9bkw2PNph7Pb/P5985HlX/b5rEtdgpS7kHQP/uWqFIThLMEPSymzcl76lii1MBLVJrRcWat6FJLY0n0iMfhX8oVVK+8SU1PpiVDt1H5lSH5hfY1x7rba5NNXL/ADmLAwIDAQAB",
                this);
        bp.initialize();

        this.setTitle("");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        help_banner = findViewById(R.id.help_banner);
        btn_sheet = findViewById(R.id.btn_sheet);
        button_progreso = findViewById(R.id.button_progreso_lineal);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        progressBar_temas = findViewById(R.id.progressBar_products);
        recycler_temas = findViewById(R.id.recycler_temas);
        recycler_temas.setHasFixedSize(true);
        recycler_temas.setLayoutManager(new LinearLayoutManager(this));
        temasList = new ArrayList<>();
        button_proceso = findViewById(R.id.button_proceso);
        btnExercise = findViewById(R.id.fabExercise);
        btnExercisePasos = findViewById(R.id.fabExercisePasos);
        IniciarCuenta();
        button_progreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progresoLineal();
            }
        });
        btnExercisePasos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //
                SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
                final String StateSuscription = preferencesId.getString("State","Not Result");
                if(StateSuscription.equals("NO")) {
                   crearDialogo();
                }else{
                    mostrarPaginaBeta();
                }
            }
        });
        btnExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ejercicios = new Intent(PrincipalPageActivity.this, EjerciciosActivity.class);
                startActivity(ejercicios);

            }
        });
        VerifyProccess();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
        final String StateSuscription = preferencesId.getString("State","Not Result");
        if(StateSuscription.equals("NO")) {
            mostrarTema();
        }
        else{
            mostrarTemaPro();
        }
        if(StateSuscription.equals("NO")) {
           mAdView.loadAd(adRequest);
        }else{
            mAdView.setVisibility(View.GONE);
        }
        btn_sheet.setVisibility(View.GONE);
        if(StateSuscription.equals("NO")) {
            //mostrarVideo();
        }
        btn_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ShowSheet();
            }
        });
        //mostrarPasePublicidad();
        //ShowSheetResena();

        help_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bp.isSubscriptionUpdateSupported()){
                    bp.subscribe(PrincipalPageActivity.this, "suscripcionintegral");
                }else{
                    Toast.makeText(PrincipalPageActivity.this, "Subscription update is not supported", Toast.LENGTH_SHORT).show();
                }
            }
        });
        verificarBanner();
        progresoLineal();
    }

    private void IniciarCuenta() {
        new CountDownTimer(10000,1000){

            @Override
            public void onTick(long l) {
               //Esto pasa cada segundo
            }

            @Override
            public void onFinish() {
                Toast.makeText(PrincipalPageActivity.this, "Finalizado", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void verificarBanner(){
        SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
        final String StateSuscription = preferencesId.getString("State","Not Result");
        if(StateSuscription.equals("NO")) {

        }else{
            help_banner.setVisibility(View.GONE);
        }
    }


    public void createAlert(){
        builderDialog = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.alerta_tema_terminado, null);
        builderDialog.setView(contactPopupView);
        AlertDialog dialog = builderDialog.create();
        dialog.setCancelable(false);
        dialog.show();
    }


    private void mostrarVideo(){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrincipalPageActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet_video_pase,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer_video_pase)
                );
        TextView nombre = bottomSheetView.findViewById(R.id.nombre_bottomSheet_video_pase);
        Button btn_pase =bottomSheetView.findViewById(R.id.Btn_adquirir_pase);
        nombre.setText("Mira los beneficios de obtener la versión pro. (3 dias gratis)");
        YouTubePlayerView youTubePlayerView = bottomSheetView.findViewById(R.id.youtube_player_view_bottomSheet_pase);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(@NonNull final YouTubePlayer youTubePlayer) {
                youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        String videoId = "X_bBRpk-zF0";
                        youTubePlayer.loadVideo(videoId,0);
                    }
                });
            }
        }, true);
        btn_pase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bp.isSubscriptionUpdateSupported()){
                    bp.subscribe(PrincipalPageActivity.this, "suscripcionintegral");
                }else{
                    Toast.makeText(PrincipalPageActivity.this, "Subscription update is not supported", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();





    }

    public void crearDialogo(){
        final Dialog dialog = new Dialog(PrincipalPageActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alerta_desbloquea_premium);
        TextView title_alert = dialog.findViewById(R.id.title_alert_pago);
        TextView descripcion_alert = dialog.findViewById(R.id.description_alert_pago);
        TextView btn_continuar_alert = dialog.findViewById(R.id.btn_continuar_alert_pago);
        TextView btn_cancelar_alert = dialog.findViewById(R.id.btn_cancelar_alert_pago);
        TextView btn_cerrar_alert = dialog.findViewById(R.id.btn_cerrar_alert_pago);
        title_alert.setText("Desbloquea premium!");
        descripcion_alert.setText("(3 días gratis) Adquiere la función premium, y práctica con la mejor actividad, que te ayudara a entender cada proceso a medida que vas eligiendo los pasos que siguen en el ejercicio.");
        btn_continuar_alert.setText(Html.fromHtml("<p><u>Ver precio</u></p>"));
        btn_cancelar_alert.setText(Html.fromHtml("<p><u>Cancelar</u></p>"));
        btn_continuar_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bp.isSubscriptionUpdateSupported()){
                    bp.subscribe(PrincipalPageActivity.this, "suscripcionintegral");
                }else{
                    Toast.makeText(PrincipalPageActivity.this, "Subscription update is not supported", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_cancelar_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_cerrar_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void crearDialogoTemaPremium(){
        final Dialog dialog = new Dialog(PrincipalPageActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alerta_desbloquea_premium);
        TextView title_alert = dialog.findViewById(R.id.title_alert_pago);
        TextView descripcion_alert = dialog.findViewById(R.id.description_alert_pago);
        TextView btn_continuar_alert = dialog.findViewById(R.id.btn_continuar_alert_pago);
        TextView btn_cancelar_alert = dialog.findViewById(R.id.btn_cancelar_alert_pago);
        TextView btn_cerrar_alert = dialog.findViewById(R.id.btn_cerrar_alert_pago);
        title_alert.setText("Desbloquea premium!");
        descripcion_alert.setText("(3 días gratis) Adquiere la función premium para desbloquear este y todos los temas premium, con explicaciones detalladas de cada proceso, y muchas funcionalidades más.");
        btn_continuar_alert.setText(Html.fromHtml("<p><u>Ver precio.</u></p>"));
        btn_cancelar_alert.setText(Html.fromHtml("<p><u>Ver video.</u></p>"));
        btn_continuar_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bp.isSubscriptionUpdateSupported()){
                    bp.subscribe(PrincipalPageActivity.this, "suscripcionintegral");
                }else{
                    Toast.makeText(PrincipalPageActivity.this, "Subscription update is not supported", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_cancelar_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadRewardedVideoAd();
                if(mRewardedVideoAd.isLoaded()){
                    mRewardedVideoAd.show();
                    dialog.dismiss();
                }else{
                    Toast.makeText(PrincipalPageActivity.this, "El video no ha cargado, click de nuevo", Toast.LENGTH_LONG).show();
                }


            }
        });
        btn_cerrar_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

private void verificarCompra(){
    SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
    final String StateSuscription = preferencesId.getString("State","Not Result");
    if(StateSuscription.equals("NO")) {
        AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
        Bien.setMessage("Esta funcionalidad requiere de una una compra unica del pase de calculo integral, adquierela y obtén acceso sin limite a todas las funcionalidades de calculo integral, desaparece la publicidad, ingresa a cientos de ejercicios con soluciones, y accede a temas muy bien explicados del calculo integral.")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ver precio", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(bp.isSubscriptionUpdateSupported()){
                            bp.subscribe(PrincipalPageActivity.this, "suscripcionintegral");
                        }else{
                            Toast.makeText(PrincipalPageActivity.this, "Subscription update is not supported", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog Titulo = Bien.create();
        Titulo.setTitle("Adquiere el pase!");
        Titulo.show();
    }
    else{
        Intent ejercicios = new Intent(PrincipalPageActivity.this, EjerciciosActivity.class);
        startActivity(ejercicios);
    }
}

    private void ShowSheetResena(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrincipalPageActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet_resena,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer_resena)
                );
        Button calificacion = bottomSheetView.findViewById(R.id.button_calificar);
        calificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.smeagtechnology.integral");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                  }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }



    private void ShowSheeHelp(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrincipalPageActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet_ayuda,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer_ayuda)
                );
        Button acceder = bottomSheetView.findViewById(R.id.button_acceder);
        Button ayuda = bottomSheetView.findViewById(R.id.button_info_ayuda);
        acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  dominio = getString(R.string.app_domain);
                Uri uri = Uri.parse("https://"+dominio);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PrincipalPageActivity.this, "Aquí se mostrará la información de la ayuda", Toast.LENGTH_SHORT).show();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }



    private void ShowSheetCompartir(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrincipalPageActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet_compartir,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer_compartir)
                );
        final EditText text_msg_share = bottomSheetView.findViewById(R.id.text_msg_share);

        Button compartir = bottomSheetView.findViewById(R.id.button_compartir);
        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = text_msg_share.getText().toString();
                String mensaje_share = "";
                Intent compartir = new Intent(android.content.Intent.ACTION_SEND);
                compartir.setType("text/plain");
                if(mensaje.equals("")){
                    mensaje_share = "Hola, mira encontre una app para aprender cálculo integral, es muy buena, te la recomiendo.  https://play.google.com/store/apps/details?id=com.smeagtechnology.integral";
                }else{
                    mensaje_share = mensaje + "  https://play.google.com/store/apps/details?id=com.smeagtechnology.integral";
                }
                compartir.putExtra(android.content.Intent.EXTRA_SUBJECT, "Empleos Baja App");
                compartir.putExtra(android.content.Intent.EXTRA_TEXT, mensaje_share);
                startActivity(Intent.createChooser(compartir, "Compartir vía"));
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
    private void ShowSheet(){
        SharedPreferences preferencesId = getSharedPreferences("NameUserSave", Context.MODE_PRIVATE);
        final String NameUserR = preferencesId.getString("UserName","Not Result");
        SharedPreferences preferencesEmail = getSharedPreferences("EmailUserSave", Context.MODE_PRIVATE);
        final String Email = preferencesEmail.getString("UserEmail","Not Result");
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrincipalPageActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer)
                );
        TextView nombre = bottomSheetView.findViewById(R.id.nombre_bottomSheet);
        nombre.setText(NameUserR);
        TextView correo = bottomSheetView.findViewById(R.id.correo_bottomSheet);
        correo.setText(Email);
       bottomSheetDialog.setContentView(bottomSheetView);
       bottomSheetDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutemas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case(R.id.action_search):
                Toast.makeText(this, "Opción no disponible", Toast.LENGTH_SHORT).show();
                break;
            case(R.id.action_refresh):
                temasList.clear();
                progressBar_temas.setVisibility(View.VISIBLE);
                SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
                final String StateSuscription = preferencesId.getString("State","Not Result");
                if(StateSuscription.equals("NO")) {
                    mostrarTema();
                }
                else{
                    mostrarTemaPro();
                }
                break;
            case(R.id.action_datos):
                Intent openActivity  = new Intent(this, DatosActivity.class);
                startActivity(openActivity);
                break;
            case(R.id.action_salir):
                AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
                Bien.setMessage("¿Quieres cerrar sesión? esto es recomendable para continuar tu proceso con otra cuenta que tengas registrada (se cerrara sesión en este dispositivo permanentemente).")
                        .setCancelable(false)
                        .setPositiveButton("Cerrar Sesión", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mGoogleSignInClient.signOut();
                                SharedPreferences preferencesName = getSharedPreferences("NameUserSave", Context.MODE_PRIVATE);
                                String PersonName = "Not Result";
                                SharedPreferences.Editor editorName = preferencesName.edit();
                                editorName.putString("UserName", PersonName);
                                editorName.commit();
                                SharedPreferences preferencesEmail = getSharedPreferences("EmailUserSave", Context.MODE_PRIVATE);
                                String PersonEmail = "Not Result";
                                SharedPreferences.Editor editorEmail = preferencesEmail.edit();
                                editorEmail.putString("UserEmail", PersonEmail);
                                editorEmail.commit();
                                Intent salir = new Intent(PrincipalPageActivity.this, IndexActivity.class);
                                startActivity(salir);
                                finish();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Cerrar sesión!");
                Titulo.show();
                break;
            case(R.id.action_apps):
                Intent openApps  = new Intent(this, AppsActivity.class);
                startActivity(openApps);
                break;
            case(R.id.action_calificar):
                AlertDialog.Builder calificar = new AlertDialog.Builder(PrincipalPageActivity.this);
                calificar.setMessage("Dinos que te ha parecido nuestra aplicación de calculo integral, para que todos puedan ver tu opinión sobre nosotros.")
                        .setCancelable(false)
                        .setPositiveButton("Calificar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.smeagtechnology.integral");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog title = calificar.create();
                title.setTitle("Califica Calculo Integral!");
                title.show();
                break;
            case(R.id.action_compartir):
                ShowSheetCompartir();
                break;
            default:break;
        }
        return true;
    }

private void VerifyProccess(){
    SharedPreferences preferencesTema = getSharedPreferences("TemaEnProceso", Context.MODE_PRIVATE);
    final String NombreTema = preferencesTema.getString("NombreTema","Not Result");



    if(NombreTema.equals("Not Result")){
        button_proceso.setText("Comienza a aprender desde yá");
    }else{
        SharedPreferences preferencesIdTemaTema = getSharedPreferences("IdTemaEnProceso", Context.MODE_PRIVATE);
        final String IdTemaTexto = preferencesIdTemaTema.getString("IdTema","Not Result");
        final int IdTema = Integer.parseInt(IdTemaTexto);
        button_proceso.setText("Continua tu progreso con " + NombreTema);
        button_proceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterstitialAd = new InterstitialAd(PrincipalPageActivity.this);
                mInterstitialAd.setAdUnitId("ca-app-pub-1778393545986901/9119002383");
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
                mInterstitialAd.setAdListener(new AdListener() {


                    public void onAdLoaded(){
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }

                });
                Bundle extras = new Bundle();
                extras.putInt("IdTema" ,IdTema);
                extras.putString("NombreTema" ,NombreTema );
                Intent openTema = new Intent(PrincipalPageActivity.this, TemaEleccionActivity.class);
                openTema.putExtras(extras);
                //startActivity(openTema);
            }
        });
    }
}

    private void loadRewardedVideoAd(){
        if(!mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.loadAd("ca-app-pub-1778393545986901/5572292005",  new AdRequest.Builder().build());
        }
    }

    private void mostrarTema() {
        String  dominio = getString(R.string.app_domain);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerTemas.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Temas = array.getJSONObject(i);
                                temasList.add(new Temas(
                                        Temas.getInt("IdTema"),
                                        Temas.getString("NombreTema"),
                                        Temas.getString("DescripcionTema"),
                                        Temas.getString("FotoTema"),
                                        Temas.getString("suscripcion"),
                                        Temas.getString("NombreVideo")

                                ));
                            }
                            AdapterTemas adapter = new AdapterTemas(PrincipalPageActivity.this, temasList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
                                    final String StateSuscription = preferencesId.getString("State","Not Result");

                                    if(StateSuscription.equals("NO")){
                                        if (temasList.get(recycler_temas.getChildAdapterPosition(view)).getSuscripcion().equals("NO")){
                                            /*mInterstitialAd = new InterstitialAd(PrincipalPageActivity.this);
                                            mInterstitialAd.setAdUnitId("ca-app-pub-1778393545986901/4282959745");
                                            AdRequest adRequest = new AdRequest.Builder().build();
                                            mInterstitialAd.loadAd(adRequest);
                                            mInterstitialAd.setAdListener(new AdListener() {


                                                public void onAdLoaded(){
                                                    if (mInterstitialAd.isLoaded()) {
                                                        mInterstitialAd.show();
                                                    }
                                                }

                                            });*/
                                            button_proceso.setText("Continua tu progreso con " + temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                            SharedPreferences preferencesTema = getSharedPreferences("TemaEnProceso", Context.MODE_PRIVATE);
                                            String Tema = temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema();
                                            SharedPreferences.Editor editorTema = preferencesTema.edit();
                                            editorTema.putString("NombreTema", Tema);
                                            editorTema.commit();


                                            SharedPreferences preferencesIdTema = getSharedPreferences("IdTemaEnProceso", Context.MODE_PRIVATE);
                                            int IdTemaNumero = temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema();
                                            String IdTema = String.valueOf(IdTemaNumero);
                                            SharedPreferences.Editor editorIdTema = preferencesIdTema.edit();
                                            editorIdTema.putString("IdTema", IdTema);
                                            editorIdTema.commit();

                                            Bundle extras = new Bundle();
                                            extras.putInt("IdTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema());
                                            extras.putString("NombreTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                            extras.putString("NombreVideo" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombrevideo());
                                            Intent openTema = new Intent(PrincipalPageActivity.this, TemaEleccionActivity.class);
                                            openTema.putExtras(extras);
                                            startActivity(openTema);
                                        }else{

                                            if(BloqueoTemas.equals("Bloqueado")){
                                               crearDialogoTemaPremium();
                                            }else if(BloqueoTemas.equals("Desbloqueado")){
                                                button_proceso.setText("Continua tu progreso con " + temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                                Toast.makeText(PrincipalPageActivity.this, "Tema canjeado", Toast.LENGTH_SHORT).show();
                                                BloqueoTemas = "Bloqueado";
                                                Bundle extras = new Bundle();
                                                extras.putInt("IdTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema());
                                                extras.putString("NombreTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                                extras.putString("NombreVideo" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombrevideo());


                                                Intent openTema = new Intent(PrincipalPageActivity.this, TemaEleccionActivity.class);
                                                openTema.putExtras(extras);
                                                startActivity(openTema);
                                            }

                                        }

                                    }else{
                                        SharedPreferences preferencesTema = getSharedPreferences("TemaEnProceso", Context.MODE_PRIVATE);
                                        String Tema = temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema();
                                        SharedPreferences.Editor editorTema = preferencesTema.edit();
                                        editorTema.putString("NombreTema", Tema);
                                        editorTema.commit();


                                        SharedPreferences preferencesIdTema = getSharedPreferences("IdTemaEnProceso", Context.MODE_PRIVATE);
                                        int IdTemaNumero = temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema();
                                        String IdTema = String.valueOf(IdTemaNumero);
                                        SharedPreferences.Editor editorIdTema = preferencesIdTema.edit();
                                        editorIdTema.putString("IdTema", IdTema);
                                        editorIdTema.commit();

                                        Bundle extras = new Bundle();
                                        extras.putInt("IdTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema());
                                        extras.putString("NombreTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                        extras.putString("NombreVideo" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombrevideo());


                                        Intent openTema = new Intent(PrincipalPageActivity.this, TemaEleccionActivity.class);
                                        openTema.putExtras(extras);
                                        startActivity(openTema);
                                    }








                                         }
                            });
                            recycler_temas.setAdapter(adapter);
                            progressBar_temas.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_temas.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
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
                                temasList.clear();
                                progressBar_temas.setVisibility(View.VISIBLE);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerTemas.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Temas = array.getJSONObject(i);
                                temasList.add(new Temas(
                                        Temas.getInt("IdTema"),
                                        Temas.getString("NombreTema"),
                                        Temas.getString("DescripcionTema"),
                                        Temas.getString("FotoTema"),
                                        Temas.getString("suscripcion"),
                                        Temas.getString("NombreVideo")

                                ));
                            }
                            AdapterTemasPro adapter = new AdapterTemasPro(PrincipalPageActivity.this, temasList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
                                    final String StateSuscription = preferencesId.getString("State","Not Result");

                                    if(StateSuscription.equals("NO")){
                                        if (temasList.get(recycler_temas.getChildAdapterPosition(view)).getSuscripcion().equals("NO")){
                                            mInterstitialAd = new InterstitialAd(PrincipalPageActivity.this);
                                            mInterstitialAd.setAdUnitId("ca-app-pub-1778393545986901/4282959745");
                                            AdRequest adRequest = new AdRequest.Builder().build();
                                            mInterstitialAd.loadAd(adRequest);
                                            mInterstitialAd.setAdListener(new AdListener() {


                                                public void onAdLoaded(){
                                                    if (mInterstitialAd.isLoaded()) {
                                                        mInterstitialAd.show();
                                                    }
                                                }

                                            });
                                            button_proceso.setText("Continua tu progreso con " + temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                            SharedPreferences preferencesTema = getSharedPreferences("TemaEnProceso", Context.MODE_PRIVATE);
                                            String Tema = temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema();
                                            SharedPreferences.Editor editorTema = preferencesTema.edit();
                                            editorTema.putString("NombreTema", Tema);
                                            editorTema.commit();


                                            SharedPreferences preferencesIdTema = getSharedPreferences("IdTemaEnProceso", Context.MODE_PRIVATE);
                                            int IdTemaNumero = temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema();
                                            String IdTema = String.valueOf(IdTemaNumero);
                                            SharedPreferences.Editor editorIdTema = preferencesIdTema.edit();
                                            editorIdTema.putString("IdTema", IdTema);
                                            editorIdTema.commit();

                                            Bundle extras = new Bundle();
                                            extras.putInt("IdTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema());
                                            extras.putString("NombreTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                            extras.putString("NombreVideo" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombrevideo());
                                            Intent openTema = new Intent(PrincipalPageActivity.this, TemaEleccionActivity.class);
                                            openTema.putExtras(extras);
                                            startActivity(openTema);
                                        }else{

                                            if(BloqueoTemas.equals("Bloqueado")){
                                                crearDialogoTemaPremium();
                                            }else if(BloqueoTemas.equals("Desbloqueado")){
                                                button_proceso.setText("Continua tu progreso con " + temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                                Toast.makeText(PrincipalPageActivity.this, "Tema canjeado", Toast.LENGTH_SHORT).show();
                                                BloqueoTemas = "Bloqueado";
                                                Bundle extras = new Bundle();
                                                extras.putInt("IdTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema());
                                                extras.putString("NombreTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                                extras.putString("NombreVideo" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombrevideo());


                                                Intent openTema = new Intent(PrincipalPageActivity.this, TemaEleccionActivity.class);
                                                openTema.putExtras(extras);
                                                startActivity(openTema);
                                            }

                                        }

                                    }else{
                                        SharedPreferences preferencesTema = getSharedPreferences("TemaEnProceso", Context.MODE_PRIVATE);
                                        String Tema = temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema();
                                        SharedPreferences.Editor editorTema = preferencesTema.edit();
                                        editorTema.putString("NombreTema", Tema);
                                        editorTema.commit();


                                        SharedPreferences preferencesIdTema = getSharedPreferences("IdTemaEnProceso", Context.MODE_PRIVATE);
                                        int IdTemaNumero = temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema();
                                        String IdTema = String.valueOf(IdTemaNumero);
                                        SharedPreferences.Editor editorIdTema = preferencesIdTema.edit();
                                        editorIdTema.putString("IdTema", IdTema);
                                        editorIdTema.commit();

                                        Bundle extras = new Bundle();
                                        extras.putInt("IdTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema());
                                        extras.putString("NombreTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                        extras.putString("NombreVideo" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombrevideo());


                                        Intent openTema = new Intent(PrincipalPageActivity.this, TemaEleccionActivity.class);
                                        openTema.putExtras(extras);
                                        startActivity(openTema);
                                    }








                                }
                            });
                            recycler_temas.setAdapter(adapter);
                            progressBar_temas.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_temas.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
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
                                temasList.clear();
                                progressBar_temas.setVisibility(View.VISIBLE);
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


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PrincipalPageActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private boolean hasSuscription(){

        if(transactionDetails != null) {
            return transactionDetails.purchaseInfo != null;
        }
        return  false;
    }


    @Override
    public void onBillingInitialized() {
        transactionDetails =bp.getSubscriptionTransactionDetails ( "suscripcionintegral" );

        bp.loadOwnedPurchasesFromGoogle();
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


        new PrincipalPageActivity.RegistrarSuscripcion(PrincipalPageActivity.this).execute(Nombre, Correo);

        AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
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
        Titulo.setTitle("Que bien compra exitosa!");
        Titulo.show();
    }

    @Override
    public void onPurchaseHistoryRestored() {
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

        AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
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
                        bp.subscribe(PrincipalPageActivity.this, "suscripcionintegral");
                    }
                });
        AlertDialog Titulo = Bien.create();
        Titulo.setTitle("Oh no!");
        Titulo.show();
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

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
        Bien.setMessage("No se ha podido cargar el video, posiblemente es tu conexión a internet o has llegado al limite de hoy.")
                .setCancelable(true)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Volver a intentar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadRewardedVideoAd();
                        if(mRewardedVideoAd.isLoaded()){
                            mRewardedVideoAd.show();
                        }
                    }
                });
        AlertDialog Titulo = Bien.create();
        Titulo.setTitle("Oh no!");
        Titulo.show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        BloqueoTemas = "Desbloqueado";
        AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
        Bien.setMessage("Has desbloqueado los temas premium para una elección, elige el que necesitas para que lo puedas aprender.")
                .setCancelable(true)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog Titulo = Bien.create();
        Titulo.setTitle("Excelente!");
        Titulo.show();
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
    private void mostrarPasePublicidad(){
        SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
        final String StateSuscription = preferencesId.getString("State","Not Result");
        if(StateSuscription.equals("NO")) {
            AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
            Bien.setMessage("Tenemos una promoción especial para ti, adquiere el pase calculo integral de compra unica y obtén acceso a todos los temas premium del calculo integral, a cientos de ejercicios de práctica con solución muy bien explicada, obtén accesoa funcionalidades en espera de términación  tales como solución de ejercicios que tengas, calculadora de integrales y desaparece la publicidad.  ")
                    .setCancelable(false)
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("Ver precio", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (bp.isSubscriptionUpdateSupported()) {
                                bp.subscribe(PrincipalPageActivity.this, "suscripcionintegral");
                            } else {
                                Toast.makeText(PrincipalPageActivity.this, "Subscription update is not supported", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            AlertDialog Titulo = Bien.create();
            Titulo.setTitle("Adquiere el pase calculo integral!");
            Titulo.show();
        }else{

        }
    }

    private void mostrarPaginaBeta(){
        Intent paginaBeta = new Intent(PrincipalPageActivity.this, EjerciciosPorPasos.class);
        startActivity(paginaBeta);
    }

    private void progresoLineal(){
        SharedPreferences preferencesId = getSharedPreferences("ProgresoLineal", Context.MODE_PRIVATE);
        final String progreso = preferencesId.getString("Temaprogreso","Not Result");
        if(progreso.equals("Not Result")){
            Intent progresoLineal = new Intent(PrincipalPageActivity.this, ProgresoLinealActivity.class);
            startActivity(progresoLineal);
        }else{
            int progreso_id = Integer.parseInt(progreso);
            Bundle extras = new Bundle();
            extras.putInt("IdTema" ,progreso_id);
            Intent continuarProgreso = new Intent(PrincipalPageActivity.this, ProgresoLinealActivity.class);
            continuarProgreso.putExtras(extras);
            startActivity(continuarProgreso);
            Toast.makeText(this, "Continuamos donde lo dejaste.", Toast.LENGTH_SHORT).show();
        }




    }

}