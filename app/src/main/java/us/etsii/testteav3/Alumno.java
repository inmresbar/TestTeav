package us.etsii.testteav3;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Alumno extends AppCompatActivity {

    private String alumno;
    private String usuario;

    private TextView nombre;

    private FirebaseDatabase bd;
    private DatabaseReference puntoDeAcceso;
    private ChildEventListener childEventListener;
    private TestAdapter testAdapter;

    private ListView listViewTest;

    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);

        //Recupero el usuario y el alumno
        usuario=getIntent().getStringExtra(Constantes.user);
        alumno = getIntent().getStringExtra(Constantes.nombre);

        //Para mostrar el nombre del alumno
        nombre=findViewById(R.id.nombre);
        nombre.setText(alumno);

        //Para obtener la lista de test realizados a ese alumno
        bd = FirebaseDatabase.getInstance();
        puntoDeAcceso = bd.getReference().child(usuario).child(alumno);
        listViewTest = findViewById(R.id.testListView);
        listViewTest.setAdapter(testAdapter);
        List<Test> tests=new ArrayList<>();
        testAdapter=new TestAdapter(this,R.layout.item_alumno, tests);


        boton = findViewById(R.id.button2);

        //Acción al pulsar el botón iniciar test
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Alumno.this,MChat.class);
                intent.putExtra(Constantes.nombre, alumno).putExtra(Constantes.user,usuario);
                startActivity(intent);
            }
        });

        //Recuperar los test realizados a un alumno
        puntoDeAcceso.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Creo un objeto
                Test test=dataSnapshot.getValue(Test.class);
                //y lo añado al adapter
                testAdapter.add(test);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Clase interna para gestionar cómo muestro los test en el listView
    class TestAdapter extends ArrayAdapter<Test> {

        Context context;
        List<Test> tests;

        public TestAdapter(@NonNull Context context, int resource,
                              @NonNull List<Test> tests) {
            super(context, resource, tests);
            this.context=context;
            this.tests=tests;

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //Para el objeto i-ésimo recupero el elemento de la lista contenedora
            Test test=tests.get(i);
            //Recupero el "inflador" de layouts
            LayoutInflater layoutInflater=getLayoutInflater().from(context);
            //Inflo el layout item_message que he definido
            view=layoutInflater.inflate(R.layout.item_alumno,viewGroup,false);

            //Recupero los componentes del item_message
            TextView fecha=view.findViewById(R.id.fecha);
            TextView resultado=view.findViewById(R.id.resultado);
            TextView completo=view.findViewById(R.id.completo);

            //Le asigno los valores del test
            resultado.setText("Preguntas contestadas negativamente: "+String.valueOf(test.getResultado()));
            completo.setText("Preguntas contestadas: "+String.valueOf(test.getCompleto()));
            DateFormat date=new SimpleDateFormat("dd/MM/yyyy");
            Date diaBD=test.getFecha();
            String dia=date.format(diaBD);
            fecha.setText("Fecha: " +dia);
            //Log.d("resultado", String.valueOf(resultado));
            //Log.d("completo",String.valueOf(completo));


            //Devuelvo la vista que he creado
            return view;
        }
    }

    //Eliminar los test realizados a un alumno
    public void eliminar (View view){
        puntoDeAcceso.child(usuario).child(usuario).removeValue();
    }

}
