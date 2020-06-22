package us.etsii.testteav3;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Alumnos extends AppCompatActivity {

    private EditText editText;
    private String alumno;

    private String usuario;
    private List<String> alumnos;

    private Button aceptar;

    private FirebaseDatabase bd;
    private DatabaseReference puntoDeAcceso;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);

        editText = findViewById(R.id.nombreAlumno);

        usuario=getIntent().getStringExtra(Constantes.user);
        alumnos=new ArrayList<>();

        aceptar = findViewById(R.id.aceptar);

        bd = FirebaseDatabase.getInstance();
        puntoDeAcceso = bd.getReference();

        //Crear una lista con los alumnos que tiene ese usuario
        puntoDeAcceso.child(usuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot data:dataSnapshot.getChildren())
                        alumnos.add(data.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Crear el alumno si no existe y pasar a la siguiente lista
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alumno = editText.getText().toString();
                String user = getIntent().getStringExtra(Constantes.user);

                if (!alumnos.contains(alumno)||alumnos==null||alumnos.size()==0)
                    puntoDeAcceso.child(user).child(alumno).setValue(0);

                Intent intent = new Intent(Alumnos.this, Alumno.class);
                intent.putExtra(Constantes.nombre, alumno).putExtra(Constantes.user,user);
                startActivity(intent);
            }
        });

    }
}