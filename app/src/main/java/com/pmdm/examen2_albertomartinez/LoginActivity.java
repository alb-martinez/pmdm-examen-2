package com.pmdm.examen2_albertomartinez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pmdm.examen2_albertomartinez.Utils.Util;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences shPreferences;

    // Elementos del layout
    private EditText etEmail;
    private EditText etPassword;
    private Switch swRemember;
    private Button btLogin;
    private Button btSingIn;

    // Instancia de FirebaseAuth
    private FirebaseAuth mAuth;
    private FirebaseFirestore dbFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Inicialización de la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Llamada al metodo que referencia los elementos del layout
        bindUI();

        dbFirebase = FirebaseFirestore.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    // Metodo para referenciar los elementos del layout
    private void bindUI() {
        // Configuración de la actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("EXAMEN2 - LOGIN");
        getSupportActionBar().setIcon(R.mipmap.ic_icono_insti_round);

        // Referencia a los elementos del layout
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        swRemember = (Switch) findViewById(R.id.swRemember);
        btLogin = (Button) findViewById(R.id.btLogin);
        btSingIn = (Button) findViewById(R.id.btSingIn);

        // Las SharedPreferences necesitan un nombre de archivo y un modo de creación
        // Esta instancia nos sirve solo para leer
        shPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        // Llamada al metodo que comprueba si hay credenciales guardadas y las muestra en los campos
        setCredentialsIfExist();

        // Evento click del boton de LOGIN
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithFirebaseAccount(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

        // Evento click del boton SING IN
        btSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewFirebaseAccount(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    // Metodo para crear cuenta nueva en Firebase
    private void createNewFirebaseAccount(final String email, final String password) {
        if (isValidLogin(email, password)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                saveOnPreferences(email, password);
                                FirebaseUser user = mAuth.getCurrentUser();
                                goToMain(user.getEmail());
                            } else {
                                showAlert("User already exists in Firebase");
                            }
                        }
                    });
        } else {
            showAlert("Email or Password is not valid, please try again");
        }
    }

    // Metodo para realizar el login, con un usuario de Firebase
    public void loginWithFirebaseAccount(final String email, final String password) {
        if (isValidLogin(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // El metodo saveOnPreferences, solo las guarda si está activado el switch
                                saveOnPreferences(email, password);
                                FirebaseUser user = mAuth.getCurrentUser();
                                goToMain(user.getEmail());
                            } else {
                                showAlert("User authenticacion error in Firebase");
                            }
                        }
                    });
        } else {
            showAlert("Email or Password is not valid, please try again");
        }
    }

    // Metodo para validar el email
    // Que no esté vacío y que cumpla las normas básicas de forma
    private boolean isValidEmail(String email) {
        // Utilización de los métodos TextUtils.isEmpty() y android.util.Patterns.EMAIL_ADDRESS.matcher()
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Metodo para validar el password
    // Que no sea inferior a 4 digitos
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    // Metodo para validar el login completo e informar de los errores
    private boolean isValidLogin(String username, String password) {
        if (!isValidEmail(username)) {
            Toast.makeText(this, "Email is not valid, please try again", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isValidPassword(password)) {
            Toast.makeText(this, "Password is not valid, 4 character or more, please try again", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    // Metodo para pasar a la siguiente activity, despues del login correcto
    private void goToMain(String email) {
        Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
        // Si desde MainActivity se pulsa "atrás", se saldrá de la aplicación, no volverá al login.
        intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Datos que pasaremos a la siguiente Activity
        intentMain.putExtra("email", email);
        startActivity(intentMain);
    }

    // Metodo para guardar las SharedPreferences, si el switch está activado
    private void saveOnPreferences(String email, String password) {
        if (swRemember.isChecked()) {
            // Creamos un editor de SharedPreferences para poder editar y añadir datos
            SharedPreferences.Editor spEditor = shPreferences.edit();
            // Se añaden los datos con el formato KEY-VALUE
            spEditor.putString("email", email);
            spEditor.putString("pass", password);
            spEditor.commit();
            // Como son pocos datos, guardamos de forma asincrona en segundo plano
            spEditor.apply();
        }
    }

    // Metodo para que si existen credenciales guardadas, las ponga al iniciar la App
    private void setCredentialsIfExist() {
        String email = Util.getUserMailPrefs(shPreferences);
        String password = Util.getUserPasswordPrefs(shPreferences);
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            etEmail.setText(email);
            etPassword.setText(password);
        }
    }

    // Metodo para mostrar mensaje de alerta
    private void showAlert(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}