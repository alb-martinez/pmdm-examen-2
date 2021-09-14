package com.pmdm.examen2_albertomartinez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pmdm.examen2_albertomartinez.Utils.Util;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    // Instancia de SharedPreferences
    SharedPreferences shPreferences;
    String emailP;
    String passP;

    // Instancia de FirebaseAuth
    private FirebaseAuth mAuth;

    // Intents a las siguientes Activitys
    Intent intentMain;
    Intent intentLogin;

    // Objetos del layout
    private TextView tvNombre;
    private ImageView ivLogo;
    private ProgressBar pbCarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        shPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        emailP = Util.getUserMailPrefs(shPreferences);
        passP = Util.getUserPasswordPrefs(shPreferences);

        intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
        intentMain = new Intent(SplashActivity.this, MainActivity.class);

        lanzaAnim();

        /* Al iniciar la tarea, comprueba si existen datos de email y password en las SharedPreferences.
        Si existen, cambiamos a la MainActivity, si no, cambiamos a la LoginActivity */
        TimerTask tareaSleep = new TimerTask() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(Util.getUserMailPrefs(shPreferences)) &&
                        !TextUtils.isEmpty(Util.getUserPasswordPrefs(shPreferences))) {
                    loginWithFirebaseAccount(emailP, passP);
                } else {
                    startActivity(intentLogin);
                }
                //finish();
            }
        };
        //Toast.makeText(this, emailP + " - " + passP, Toast.LENGTH_LONG).show();
        Timer tiempo = new Timer();
        tiempo.schedule(tareaSleep, 5000);
    }

    // Metodo para realizar el login, con un usuario de Firebase
    public void loginWithFirebaseAccount(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intentMain);
                            Toast.makeText(getApplicationContext(), "LOGIN CON USUARIO GUARDADO", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(intentLogin);
                            intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Toast.makeText(getApplicationContext(), "EL USUARIO NO EXISTE EN FIREBASE", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // Metodo para las animaciones
    public void lanzaAnim() {
        tvNombre = findViewById(R.id.tvNombreSplash);
        pbCarga = findViewById(R.id.pbCargaSplash);

        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.desplaz_arriba);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.desplaz_abajo);

        tvNombre.setAnimation(anim1);
        pbCarga.setAnimation(anim2);
    }

}