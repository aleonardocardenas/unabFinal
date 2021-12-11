package com.andrescardenas.unabfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrescardenas.unabfinal.Opciones.MisDatos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Inicio extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference BASE_DE_DATOS;

    private Button MisDatosOpcion, CrearCitaMedicaOpcion, HistorialOpciones, UsuariosOpcion, ChatsOpcion, CerrarSesion;

    ImageView foto_perfil;
    TextView fecha;
    TextView uidtxt, uidPerfil, correotxt, correoPerfil,nombretxt, nombrePerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Inicio");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS_DE_LA_APP");

        fecha = findViewById(R.id.fecha);
        uidtxt = findViewById(R.id.uidtxt);
        correotxt = findViewById(R.id.correotxt);
        nombretxt = findViewById(R.id.nombretxt);

        foto_perfil = findViewById(R.id.foto_perfil);
        uidPerfil = findViewById(R.id.uidPerfil);
        correoPerfil = findViewById(R.id.correoPerfil);
        nombrePerfil = findViewById(R.id.nombrePerfil);

        MisDatosOpcion = findViewById(R.id.MisDatosOpcion);
        CrearCitaMedicaOpcion = findViewById(R.id.CrearCitaMedicaOpcion);
        HistorialOpciones = findViewById(R.id.HistorialOpciones);
        UsuariosOpcion = findViewById(R.id.UsuariosOpcion);
        ChatsOpcion = findViewById(R.id.ChatsOpcion);
        CerrarSesion = findViewById(R.id.CerrrarSesion);

        CambioDeLetra();


        Date date = new Date();
        SimpleDateFormat fechaC = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String sFecha = fechaC.format(date);
        fecha.setText(sFecha);


        MisDatosOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Inicio.this, MisDatos.class));
                Toast.makeText(Inicio.this, "Mis Datos", Toast.LENGTH_SHORT).show();
            }
        });

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesion();
            }
        });

    }

    private void CambioDeLetra() {

        String ubicacion = "fuentes/sans_meddio.ttf";
        Typeface tf = Typeface.createFromAsset(Inicio.this.getAssets(), ubicacion);

        fecha.setTypeface(tf);
        uidtxt.setTypeface(tf);
        uidPerfil.setTypeface(tf);
        correoPerfil.setTypeface(tf);
        correotxt.setTypeface(tf);
        nombrePerfil.setTypeface(tf);
        nombretxt.setTypeface(tf);

        MisDatosOpcion.setTypeface(tf);
        CrearCitaMedicaOpcion.setTypeface(tf);
        HistorialOpciones.setTypeface(tf);
        UsuariosOpcion.setTypeface(tf);
        ChatsOpcion.setTypeface(tf);
        CerrarSesion.setTypeface(tf);

    }

    @Override
    protected void onStart() {
        VerificacionInicioSesion();
        super.onStart();
    }

    private void VerificacionInicioSesion(){
        if (firebaseUser != null){
            CargarDatos();
            Toast.makeText(this, "Se ha iniciado sesion", Toast.LENGTH_SHORT).show();
        }else {
            startActivity(new Intent(Inicio.this, MainActivity.class));
            finish();
        }
    }

    private void CargarDatos(){

        Query query = BASE_DE_DATOS.orderByChild("Correo").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String uid = ""+ds.child("uid").getValue();
                    String correo = ""+ds.child("Correo").getValue();
                    String nombre = ""+ds.child("Nombre").getValue();
                    String imagen = ""+ds.child("Imagen").getValue();

                    uidPerfil.setText(uid);
                    correoPerfil.setText(correo);
                    nombrePerfil.setText(nombre);

                    try {
                        Picasso.get().load(imagen).placeholder(R.drawable.ic_perfil).into(foto_perfil);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_perfil).into(foto_perfil);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void CerrarSesion(){
        firebaseAuth.signOut();
        Toast.makeText(this, "Ha cerrado sesion", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Inicio.this, MainActivity.class));
        finishAffinity();
    }

}