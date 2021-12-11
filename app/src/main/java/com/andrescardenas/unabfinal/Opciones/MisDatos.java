package com.andrescardenas.unabfinal.Opciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrescardenas.unabfinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MisDatos extends AppCompatActivity {

    private ImageView ImagenDato;
    private TextView uiDatotxt, uiDato,NombreDatotxt, NombreDato, CorreoDatotxt, CorreoDato,ContraseñaDatotxt,
            ContraseñaDato,TelefonoDatotxt, TelefonoDato;

    private Button bt_actualizar, bt_actualizarcontra;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private DatabaseReference BASE_DE_DATOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Mis Datos");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        ImagenDato = findViewById(R.id.ImagenDato);
        uiDato = findViewById(R.id.uiDato);
        uiDatotxt = findViewById(R.id.uiDatotxt);
        NombreDato = findViewById(R.id.NombreDato);
        NombreDatotxt = findViewById(R.id.NombreDatotxt);
        CorreoDato = findViewById(R.id.CorreoDato);
        CorreoDatotxt = findViewById(R.id.CorreoDatotxt);
        ContraseñaDato = findViewById(R.id.ContraseñaDato);
        ContraseñaDatotxt = findViewById(R.id.ContraseñaDatotxt);
        TelefonoDato = findViewById(R.id.TelefonoDato);
        TelefonoDatotxt = findViewById(R.id.TelefonoDatotxt);

        bt_actualizar = findViewById(R.id.bt_actualizar);
        bt_actualizarcontra = findViewById(R.id.bt_actualizarcontra);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_LA_APP");

        CambioDeLetra();

        BASE_DE_DATOS.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String imagen = ""+snapshot.child("Imagen").getValue();
                    String uid = ""+snapshot.child("uid").getValue();
                    String nombre = ""+snapshot.child("Nombre").getValue();
                    String correo = ""+snapshot.child("Correo").getValue();
                    String contraseña = ""+snapshot.child("Contraseña").getValue();
                    String telefono = ""+snapshot.child("Telefono").getValue();

                    uiDato.setText(uid);
                    NombreDato.setText(nombre);
                    CorreoDato.setText(correo);
                    ContraseñaDato.setText(contraseña);
                    TelefonoDato.setText(telefono);

                    try {
                        Picasso.get().load(imagen).placeholder(R.drawable.ic_perfil).into(ImagenDato);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_perfil).into(ImagenDato);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void CambioDeLetra(){
        String ubicacion = "fuentes/sans_meddio.ttf";
        Typeface tf = Typeface.createFromAsset(MisDatos.this.getAssets(),ubicacion);

        uiDatotxt.setTypeface(tf);
        uiDato.setTypeface(tf);
        NombreDatotxt.setTypeface(tf);
        NombreDato.setTypeface(tf);
        CorreoDatotxt.setTypeface(tf);
        CorreoDato.setTypeface(tf);
        ContraseñaDatotxt.setTypeface(tf);
        ContraseñaDato.setTypeface(tf);
        TelefonoDatotxt.setTypeface(tf);
        TelefonoDato.setTypeface(tf);

        bt_actualizar.setTypeface(tf);
        bt_actualizarcontra.setTypeface(tf);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}