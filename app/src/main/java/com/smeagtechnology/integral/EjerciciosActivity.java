package com.smeagtechnology.integral;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class EjerciciosActivity extends AppCompatActivity {
    private RecyclerView recycler_ejercicios;
    List<Ejercicio> ejerciciosList;
    ProgressBar progressBar_ejercicios;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicios);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Ejercicios de práctica");
        recycler_ejercicios = findViewById(R.id.recycler_ejercicios);
        recycler_ejercicios.setHasFixedSize(true);
        recycler_ejercicios.setLayoutManager(new LinearLayoutManager(this));
        ejerciciosList = new ArrayList<>();
        progressBar_ejercicios = findViewById(R.id.progressBar_ejercicios);
        mostrarEjercicios();
    }







    private void mostrarEjercicios() {
        String  dominio = getString(R.string.app_domain);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerEjercicios.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Temas = array.getJSONObject(i);
                                ejerciciosList.add(new Ejercicio(
                                        Temas.getInt("IdEjercicio"),
                                        Temas.getString("NombreEjercicio"),
                                        Temas.getString("FotoEjercicio")

                                ));
                            }
                            AdapterEjercicio adapter = new AdapterEjercicio(EjerciciosActivity.this, ejerciciosList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle extras = new Bundle();
                                    extras.putInt("IdEjercicio" ,ejerciciosList.get(recycler_ejercicios.getChildAdapterPosition(view)).getIdejercicio());
                                     Intent openSolution = new Intent(EjerciciosActivity.this, SolucionEjerciciosActivity.class);
                                    openSolution.putExtras(extras);
                                    startActivity(openSolution);



                                }
                            });
                            recycler_ejercicios.setAdapter(adapter);
                            progressBar_ejercicios.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_ejercicios.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(EjerciciosActivity.this);
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
                                ejerciciosList.clear();
                                progressBar_ejercicios.setVisibility(View.VISIBLE);
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