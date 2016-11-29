package me.danielrosero.turnometro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AttendantActivity extends AppCompatActivity {

    private EditText et_nickname;
    private Button b_go;
    private DatabaseReference rootFirebase;
    private String eventPin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        Oculto la barra de status

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//                ****

        setContentView(R.layout.activity_attendant);


        rootFirebase = FirebaseDatabase.getInstance().getReference();

        et_nickname = (EditText) findViewById(R.id.et_nickname);
        b_go = (Button) findViewById(R.id.b_go);

        et_nickname.setHint(Html.fromHtml("<center><font color='#BDBDBD'>Nickname</font></center> "));

        Intent intent = getIntent();
        eventPin = intent.getStringExtra("pinEvent");


        b_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Valido que el nickname no haya sido tomado ya

                rootFirebase.child("eventos").child(eventPin).child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(et_nickname.getText().toString())){
                            Log.e("Nickname","ya ha sido tomado");

                            new AlertDialog.Builder(AttendantActivity.this)
                                    .setTitle("Sorry :(")
                                    .setMessage("The nickname you entered has been taken.")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();


                        }else{
                            Map<String, String> usuario = new HashMap<String, String>();
                            usuario.put("rol","participante");
                            rootFirebase.child("eventos").child(eventPin).child("usuarios").child(et_nickname.getText().toString()).setValue(usuario);

                            Intent intent = new Intent(getApplicationContext(), AttendantClock.class);
                            intent.putExtra("tipoUsuario", "participante");
                            intent.putExtra("nickUsuario",et_nickname.getText().toString());
                            intent.putExtra("eventPin",eventPin);
                            startActivity(intent);

                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//                ****





            }
        });
    }



}
