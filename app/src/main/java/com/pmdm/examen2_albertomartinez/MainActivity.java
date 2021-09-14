package com.pmdm.examen2_albertomartinez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences shPreferences;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Button btUbicacioCentro;
    private Button btTuUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Carga de las sharedPreferences desde el archivo "Preferencies" en modo solo lectura
        shPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        // Llamada al setUp
        bindUI();

    }

    private void bindUI() {

        // Configuración del actionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("EXAMEN2 - MAIN");
        getSupportActionBar().setIcon(R.mipmap.ic_icono_insti_round);

        // Referencias a los elementos del layout
        btUbicacioCentro = findViewById(R.id.btUbicacioCentro);
        btTuUbicacion = findViewById(R.id.btTuUbicacion);

        // Evento click del boton "ubicación instituto"
        btUbicacioCentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMapsDef = new Intent(getApplicationContext(), MapDefectoActivity.class);
                startActivity(intentMapsDef);
            }
        });

        // Evento click del boton "ubicación tu ubicacion"
        btTuUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMapsReal = new Intent(getApplicationContext(), MapRealActivity.class);
                startActivity(intentMapsReal);
            }
        });

    }

    // Metodo para inflar el menú de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Metodo para las acciones a realizar, según la opcion seleccionada en el menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logout();
                return true;
            case R.id.menu_forget_login:
                removeSharedPreferences();
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Metodo para realizar el logout y cambiar de Activity
    private void logout() {
        Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentLogin);
    }

    // Metodo para borrar las SharedPreferences
    private void removeSharedPreferences() {
        shPreferences.edit().clear().apply();
    }

}