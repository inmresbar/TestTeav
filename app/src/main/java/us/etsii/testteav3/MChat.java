package us.etsii.testteav3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MChat extends AppCompatActivity {

    private RadioButton botonSi, botonNo;
    private RadioGroup grupo;

    private String[] all_questions;
    private TextView text_question;
    private int i;
    private HashMap<Integer, String> map;

    private FirebaseDatabase bd;
    private DatabaseReference puntoDeAcceso;

    private String usuario;
    private String alumno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mchat);

        //Lista de preguntas
        all_questions = getResources().getStringArray(R.array.all_questions);
        text_question = (TextView) findViewById(R.id.question);

        //Seleccionamos el usuario y el alumno
        usuario = getIntent().getStringExtra(Constantes.user);
        alumno = getIntent().getStringExtra(Constantes.nombre);

        //Declaración del botón si y el botón no
        botonSi = findViewById(R.id.si);
        botonNo = findViewById(R.id.no);
        grupo = (RadioGroup) findViewById(R.id.RGroup);

        i=0;
        map = new HashMap<>();

        //Declaración de la base de datos y a dónde tiene que apuntar
        bd = FirebaseDatabase.getInstance();
        puntoDeAcceso = bd.getReference().child(usuario).child(alumno);

    }

    public void si(View view) {
        map.put(i,"si");

    }
    public void no(View view) {
        map.put(i,"no");
        if (map.containsKey(i)) {
            String s = map.get(i);
            if (s.equals("no")) {
                grupo.check(botonNo.getId());
            } else {
                grupo.check(botonSi.getId());
            }
        }
    }

    public void siguiente(View view) {
        if (i < all_questions.length-1) {
            i++;
            String q = all_questions[i];
            text_question.setText(q);
            if (map.containsKey(i)) {
                String s = map.get(i);
                if (s.equals("si")) {
                    grupo.check(botonSi.getId());
                } else {
                    grupo.check(botonNo.getId());
                }
            } else {
                grupo.clearCheck();
            }
        } else { Toast.makeText(MChat.this, "No hay más cuestiones, pulse terminar", Toast.LENGTH_SHORT).show(); }

    }

    public void anterior(View view) {
        if (i > 0) {
            i--;
            String q = all_questions[i];
            text_question.setText(q);
            if (map.containsKey(i)) {
            String s = map.get(i);
                if (s.equals("si")) {
                    grupo.check(botonSi.getId());
                } else {
                    grupo.check(botonNo.getId());
                }
            } else {
                grupo.clearCheck();
            }
        }else{
            Toast.makeText(MChat.this, "Está en la primera pregunta", Toast.LENGTH_SHORT).show();
        }
    }
    public void terminar (View view){
        int x = 0;
        List<Integer> preguntas = new ArrayList<>();
        while(x<map.size()){
           if (map.get(x).equals("no")){
               preguntas.add(x+1);
           }
           x++;
        }
        Date fecha=Calendar.getInstance().getTime();
        Test test=new Test(map.size(),preguntas,fecha);
        puntoDeAcceso.push().setValue(test);
        Intent intent=new Intent(MChat.this,Alumno.class);
        startActivity(intent);
    }
}
