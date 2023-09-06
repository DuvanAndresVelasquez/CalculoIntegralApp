package com.smeagtechnology.integral;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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

public class SolucionEjerciciosActivity extends AppCompatActivity {
    private RecyclerView recycler_solucion_ejercicio;
    List<SolucionEjercicio> solucionList;
    ProgressBar progressBar_solucion_ejercicio;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solucion_ejercicios);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("");
        progressBar_solucion_ejercicio = findViewById(R.id.progressBar_solucion_ejercicio);
        recycler_solucion_ejercicio = findViewById(R.id.recycler_solucion_ejercicio);
        recycler_solucion_ejercicio.setHasFixedSize(true);
        recycler_solucion_ejercicio.setLayoutManager(new LinearLayoutManager(this));
        solucionList = new ArrayList<>();
        mostrarSolucion();
    }



    private void mostrarSolucion() {
        final int id_ejercicio = getIntent().getIntExtra("IdEjercicio", 0);
        String  dominio = getString(R.string.app_domain);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+dominio+"/ApiCalculoIntegralApp/obtenerSolucionEjercicio.php?ejercicio="+id_ejercicio,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if(array.length() == 0){
                                Toast.makeText(SolucionEjerciciosActivity.this, "No hay elementos para mostrar en este momento", Toast.LENGTH_LONG).show();
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Parrafos = array.getJSONObject(i);
                                solucionList.add(new SolucionEjercicio(
                                        Parrafos.getInt("IdSolucion"),
                                        Parrafos.getString("ExplicacionPaso"),
                                        Parrafos.getString("FotoPaso")

                                ));
                            }
                            AdapterSolucionEjercicio adapter = new AdapterSolucionEjercicio(SolucionEjerciciosActivity.this, solucionList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            recycler_solucion_ejercicio.setAdapter(adapter);
                            progressBar_solucion_ejercicio.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_solucion_ejercicio.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(SolucionEjerciciosActivity.this);
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
                                solucionList.clear();
                                progressBar_solucion_ejercicio.setVisibility(View.VISIBLE);
                                mostrarSolucion();
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