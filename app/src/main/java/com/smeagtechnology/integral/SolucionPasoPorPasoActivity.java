package com.smeagtechnology.integral;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SolucionPasoPorPasoActivity extends AppCompatActivity {
    private int numero_paso = 1;

    private RecyclerView recycler_solucion_pasos;
    private Context context;
    List<SolucionPP> solucionppList;
    ProgressBar progressBar_solucion_pasos;
    ImageView img_ejercicio_eleccion;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solucion_paso_por_paso);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Solución paso a paso");
        String imagen = getIntent().getStringExtra("Imagen");
        img_ejercicio_eleccion = findViewById(R.id.img_ejercicio_eleccion);
        byte[] decodeString  = Base64.decode(imagen, Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        Glide.with(SolucionPasoPorPasoActivity.this)
                .load(decodeByte)
                .into(img_ejercicio_eleccion);
        recycler_solucion_pasos = findViewById(R.id.recycler_solucion_pasos);
        recycler_solucion_pasos.setHasFixedSize(true);
        recycler_solucion_pasos.setLayoutManager(new LinearLayoutManager(this));
        solucionppList = new ArrayList<>();
        progressBar_solucion_pasos = findViewById(R.id.progressBar_solucion_pasos);
        mostrarEjercicios();
    }

    private void mostrarEjercicios() {
        final int id_ejercicio = getIntent().getIntExtra("IdEjercicio", 0);
        String id_ejercicio_string = String.valueOf(id_ejercicio);
        String numero_paso_string = String.valueOf(numero_paso);
        String  dominio = getString(R.string.app_domain);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerPasosIntegral.php?paso="+numero_paso_string+"&ejercicio="+id_ejercicio_string,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Temas = array.getJSONObject(i);
                                solucionppList.add(new SolucionPP(
                                        Temas.getInt("IdPasoEjercicioIntegral"),
                                        Temas.getString("MensajeErrorExplicación"),
                                        Temas.getString("ImagenPasoEjercicioIntegral"),
                                        Temas.getInt("IdResultadoPaso"),
                                        Temas.getInt("NumeroPaso")
                                ));
                            }
                            AdapterSolucionPP adapter = new AdapterSolucionPP(SolucionPasoPorPasoActivity.this, solucionppList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int resultado = solucionppList.get(recycler_solucion_pasos.getChildAdapterPosition(view)).getIdresultadopasos();
                                    String mensajeerrorexplicacion = solucionppList.get(recycler_solucion_pasos.getChildAdapterPosition(view)).getMensajeerrorexplicacionpasos();
                                    if(resultado == 1){
                                        numero_paso = numero_paso+1;
                                        String imagen = solucionppList.get(recycler_solucion_pasos.getChildAdapterPosition(view)).getImagenejerciciointegralpasos();
                                        byte[] decodeString  = Base64.decode(imagen, Base64.DEFAULT);
                                        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                                        Glide.with(SolucionPasoPorPasoActivity.this)
                                                .load(decodeByte)
                                                .into(img_ejercicio_eleccion);
                                        final Dialog dialog = new Dialog(SolucionPasoPorPasoActivity.this);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialog.setCancelable(false);
                                        dialog.setContentView(R.layout.alerta_eleccion_bien);
                                        TextView title_alert = dialog.findViewById(R.id.title_alert_e_bien);
                                        TextView descripcion_alert = dialog.findViewById(R.id.description_alert_e_bien);
                                        TextView btn_continuar_alert = dialog.findViewById(R.id.btn_continuar_alert_e_bien);
                                        TextView btn_cancelar_alert = dialog.findViewById(R.id.btn_cancelar_alert_e_bien);
                                        TextView btn_cerrar_alert = dialog.findViewById(R.id.btn_cerrar_alert_e_bien);
                                        title_alert.setText("Excelente!");
                                        descripcion_alert.setText(mensajeerrorexplicacion);
                                        btn_continuar_alert.setText(Html.fromHtml("<p><u>Siguiente paso</u></p>"));
                                        btn_cancelar_alert.setVisibility(View.GONE);
                                        btn_cancelar_alert.setText(Html.fromHtml("<p><u>Salir</u></p>"));
                                        btn_continuar_alert.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                                solucionppList.clear();
                                                progressBar_solucion_pasos.setVisibility(View.VISIBLE);
                                                mostrarEjercicios();

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
                                                dialog.dismiss();
                                                solucionppList.clear();
                                                progressBar_solucion_pasos.setVisibility(View.VISIBLE);
                                                mostrarEjercicios();
                                                      }
                                        });
                                        dialog.show();




                                    }else{





                                        final Dialog dialog = new Dialog(SolucionPasoPorPasoActivity.this);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialog.setContentView(R.layout.alerta_eleccion_erronea);
                                        TextView title_alert = dialog.findViewById(R.id.title_alert_e_erronea);
                                        TextView descripcion_alert = dialog.findViewById(R.id.description_alert_e_erronea);
                                        TextView btn_continuar_alert = dialog.findViewById(R.id.btn_continuar_alert_e_erronea);
                                        TextView btn_cancelar_alert = dialog.findViewById(R.id.btn_cancelar_alert_e_erronea);
                                        TextView btn_cerrar_alert = dialog.findViewById(R.id.btn_cerrar_alert_e_erronea);
                                        title_alert.setText("Que mal!");
                                        descripcion_alert.setText(mensajeerrorexplicacion);
                                        btn_continuar_alert.setText(Html.fromHtml("<p><u>Reintentar</u></p>"));
                                        btn_cancelar_alert.setText(Html.fromHtml("<p><u>Salir</u></p>"));
                                        btn_continuar_alert.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
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
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();

                                        }
                                         }
                            });
                            recycler_solucion_pasos.setAdapter(adapter);
                            progressBar_solucion_pasos.setVisibility(View.INVISIBLE);
                            if(array.length() == 0){



                                final Dialog dialog = new Dialog(SolucionPasoPorPasoActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.alerta_eleccion_bien);
                                TextView title_alert = dialog.findViewById(R.id.title_alert_e_bien);
                                TextView descripcion_alert = dialog.findViewById(R.id.description_alert_e_bien);
                                TextView btn_continuar_alert = dialog.findViewById(R.id.btn_continuar_alert_e_bien);
                                TextView btn_cancelar_alert = dialog.findViewById(R.id.btn_cancelar_alert_e_bien);
                                TextView btn_cerrar_alert = dialog.findViewById(R.id.btn_cerrar_alert_e_bien);
                                title_alert.setText("Muy bien!");
                                descripcion_alert.setText("Has terminado el ejercicio, todos los pasos son correctos, práctica con más ejercicios.");
                                btn_continuar_alert.setText(Html.fromHtml("<p><u>Aceptar</u></p>"));
                                btn_cancelar_alert.setVisibility(View.GONE);
                                btn_cancelar_alert.setText(Html.fromHtml("<p><u>Salir</u></p>"));
                                btn_continuar_alert.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                      finish();

                                    }
                                });
                                btn_cancelar_alert.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_solucion_pasos.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(SolucionPasoPorPasoActivity.this);
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
                                solucionppList.clear();
                                progressBar_solucion_pasos.setVisibility(View.VISIBLE);
                                mostrarEjercicios();
                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Error de conexión!");
                Titulo.show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

}