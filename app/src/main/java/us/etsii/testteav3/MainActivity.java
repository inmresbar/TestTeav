package us.etsii.testteav3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN=456;
    private EditText nombre;
    private String usuario;

    private List<String> usuarios;

    private FirebaseDatabase bd;
    private DatabaseReference puntoDeAcceso;

    private Button boton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Nombre introducido por el usuario
        nombre = findViewById(R.id.editText3);

        //Lista que contiene todos los usuarios
        usuarios=new ArrayList<>();

        //Botón para aceptar el nombre de usuario
        boton = findViewById(R.id.button);

        //Declaración de la base de datos
        bd = FirebaseDatabase.getInstance();
        puntoDeAcceso = bd.getReference();

        //Lectura del nombre de ususario introduciso por pantalla
        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().trim().length() == 0) {
                    boton.setEnabled(false);
                } else {
                    boton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                usuario = nombre.getText().toString();

            }
        });

        //Crear una lista con los usuarios
        puntoDeAcceso.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot data:dataSnapshot.getChildren()) {
                        usuarios.add(data.getKey());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

        //Crear el usuario si no existe y pa
        public void pasaUsuario (View view) {
            if(!usuarios.contains(usuario)||usuarios==null||usuarios.size()==0) {
                puntoDeAcceso.child(usuario).setValue(1);
            }
                Intent intent = new Intent(MainActivity.this, Alumnos.class);
                intent.putExtra(Constantes.user, usuario);
                startActivity(intent);
        }

         public void createSignInIntent () {
             // [START auth_fui_create_intent]
             // Choose authentication providers
             List<AuthUI.IdpConfig> providers = Arrays.asList(
                     new AuthUI.IdpConfig.EmailBuilder().build(),
                     new AuthUI.IdpConfig.PhoneBuilder().build(),
                     new AuthUI.IdpConfig.GoogleBuilder().build());
//                    new AuthUI.IdpConfig.FacebookBuilder().build(),
             //                  new AuthUI.IdpConfig.TwitterBuilder().build());

             // Create and launch sign-in intent
             startActivityForResult(
                     AuthUI.getInstance()
                             .createSignInIntentBuilder()
                             .setAvailableProviders(providers)
                             .build(),
                     RC_SIGN_IN);
             // [END auth_fui_create_intent]
         }

    // [START auth_fui_result]
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {
                // Successfully signed in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    nombre.setText(user.getDisplayName());
                // ...
                } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                 }
            }
        }
    // [END auth_fui_result]
    //
         public void logout(View view) {
                    signOut();
        }

        public void signOut () {
        // [START auth_fui_signout]
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
            nombre.setText("");
        // [END auth_fui_signout]
         }

        public void login(View view) {
            if(FirebaseAuth.getInstance().getCurrentUser()==null) {
                createSignInIntent();
            }
        }

        public void delete(View view){

        }



    }






