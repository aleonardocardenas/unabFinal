package com.andrescardenas.unabfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.PasswordAuthentication;

public class Login extends AppCompatActivity {

    private EditText correoLogin, contraseñaLogin;
    private Button bt_login;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Login");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        correoLogin = findViewById(R.id.correoLogin);
        contraseñaLogin = findViewById(R.id.contraseñaLogin);
        bt_login = findViewById(R.id.bt_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this);
        dialog = new Dialog(Login.this);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = correoLogin.getText().toString();
                String contraseña = contraseñaLogin.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    correoLogin.setError("Correo Invalido");
                    correoLogin.setFocusable(true);

                }else if (contraseña.length()<6){
                    contraseñaLogin.setError("La contraseña debe ser mayor a 6 caracteres");
                    contraseñaLogin.setFocusable(true);
                }else {

                    LOGINUSUARIO(correo, contraseña);
                }
            }
        });

    }

    private void LOGINUSUARIO(String correo, String contraseña) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            startActivity(new Intent(Login.this, Inicio.class));
                            assert user != null;
                            Toast.makeText(Login.this, "Bienvenido"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();

                        }else {
                            progressDialog.dismiss();
                            Dialog_No_Inicio();
                            //Toast.makeText(Login.this, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Dialog_No_Inicio(){

        Button ok_no_inicio;

        dialog.setContentView(R.layout.no_sesion);
        ok_no_inicio = dialog.findViewById(R.id.ok_no_inicio);

        ok_no_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}