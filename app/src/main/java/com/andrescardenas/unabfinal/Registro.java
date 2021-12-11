package com.andrescardenas.unabfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {

    private EditText tie_nombre, tie_email, tie_contraseña;
    private Button bt_registrarnuevo;
    private View view;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Registro");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tie_nombre= findViewById(R.id.tie_nombre);
        tie_email= findViewById(R.id.tie_email);
        tie_contraseña= findViewById(R.id.tie_contraseña);
        bt_registrarnuevo= findViewById(R.id.bt_registrarnuevo);

        firebaseAuth = FirebaseAuth.getInstance();

        bt_registrarnuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = tie_email.getText().toString();
                String contraseña = tie_contraseña.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    tie_email.setError("Correo no valido");
                    tie_email.setFocusable(true);
                }else if (contraseña.length()<4){
                    tie_contraseña.setError("Contraseña muy corta, confirmela de nuevo");
                    tie_contraseña.setFocusable(true);
                }else {
                    REGISTRAR(correo, contraseña);
                }
            }
        });

    }

    private void REGISTRAR(String correo, String contraseña) {
        firebaseAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            assert user != null;
                            String uid = user.getUid();
                            String nombre = tie_nombre.getText().toString();
                            String correo = tie_email.getText().toString();
                            String contraseña = tie_contraseña.getText().toString();

                            HashMap<Object, String> DatosUsuario =new HashMap<>();

                            DatosUsuario.put("uid", uid);
                            DatosUsuario.put("Nombre", nombre);
                            DatosUsuario.put("Correo", correo);
                            DatosUsuario.put("Contraseña", contraseña);
                            DatosUsuario.put("Imagen","");
                            DatosUsuario.put("Telefono", "");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("USUARIOS_DE_LA_APP");
                            reference.child(uid).setValue(DatosUsuario);
                            Toast.makeText(Registro.this, "Se registro exitosamente", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(Registro.this, Inicio.class));

                        }else {
                            Toast.makeText(Registro.this, "Algo salio mal", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
