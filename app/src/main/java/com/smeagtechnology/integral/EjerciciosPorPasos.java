package com.smeagtechnology.integral;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EjerciciosPorPasos extends AppCompatActivity {

    private RecyclerView recycler_ejercicios_pasos;
    List<EjerciciosPasos> ejerciciospasosList;
    ProgressBar progressBar_ejercicios_pasos;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicios_por_pasos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Ejercicios de práctica paso a paso");
        recycler_ejercicios_pasos = findViewById(R.id.recycler_ejercicios_pasos);
        recycler_ejercicios_pasos.setHasFixedSize(true);
        recycler_ejercicios_pasos.setLayoutManager(new LinearLayoutManager(this));
        ejerciciospasosList = new ArrayList<>();
        progressBar_ejercicios_pasos = findViewById(R.id.progressBar_ejercicios_pasos);
        mostrarEjercicios();
    }


    private void mostrarEjercicios() {
        String  dominio = getString(R.string.app_domain);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerEjerciciosPasos.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Temas = array.getJSONObject(i);
                                ejerciciospasosList.add(new EjerciciosPasos(
                                        Temas.getInt("IdEjercicioIntegralPasos"),
                                        Temas.getString("DescripcionEjercicioIntegralPasos"),
                                        Temas.getString("ImagenEjercicioIntegralPasos")

                                ));
                            }
                            AdapterEjercicioPasos adapter = new AdapterEjercicioPasos(EjerciciosPorPasos.this, ejerciciospasosList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle extras = new Bundle();
                                    extras.putInt("IdEjercicio" ,ejerciciospasosList.get(recycler_ejercicios_pasos.getChildAdapterPosition(view)).getIdejerciciointegralpasos());
                                    extras.putString("Imagen" ,ejerciciospasosList.get(recycler_ejercicios_pasos.getChildAdapterPosition(view)).getImagenejerciciointegralpasos());
                                    Intent abrirEjercicio = new Intent(EjerciciosPorPasos.this, SolucionPasoPorPasoActivity.class);
                                    abrirEjercicio.putExtras(extras);
                                    startActivity(abrirEjercicio);
                                }
                            });
                            recycler_ejercicios_pasos.setAdapter(adapter);
                            progressBar_ejercicios_pasos.setVisibility(View.INVISIBLE);
                            if(array.length() == 0){
                                AlertDialog.Builder Bien = new AlertDialog.Builder(EjerciciosPorPasos.this);
                                Bien.setMessage("Esta funcionalidad está en modo beta, pronto se añadiran los ejercicios, se te notificará cuando estemos listos.")
                                        .setCancelable(false)
                                        .setNegativeButton("Aceptar", new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        })
                                        ;
                                AlertDialog Titulo = Bien.create();
                                Titulo.setTitle("Disculpanos!");
                                Titulo.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_ejercicios_pasos.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(EjerciciosPorPasos.this);
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
                                ejerciciospasosList.clear();
                                progressBar_ejercicios_pasos.setVisibility(View.VISIBLE);
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