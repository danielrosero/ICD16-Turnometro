package me.danielrosero.turnometro;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {

    private EditText et_pin;
    private Button b_enter,b_own_event;
    private DatabaseReference rootFirebase;
    private boolean successPin;
    private boolean haConsultado;
    private String pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        Oculto la barra de status

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//                ****

        setContentView(R.layout.activity_welcome);

        rootFirebase = FirebaseDatabase.getInstance().getReference();


        et_pin = (EditText) findViewById(R.id.et_pin);

        b_enter = (Button) findViewById(R.id.b_enter);

        b_own_event = (Button) findViewById(R.id.b_own_event);

        successPin=false;
        haConsultado=false;

        et_pin.setHint(Html.fromHtml("<center><font color='#BDBDBD'>Event PIN</font></center> "));


        b_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Valido que el PIN es de 4 digitos

                if (et_pin.getText().toString().length() != 4) {
                    new AlertDialog.Builder(WelcomeActivity.this)
                            .setTitle("Sorry :(")
                            .setMessage("PIN's are 4 digits.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    et_pin.setText("");
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {

//

//                Valido que el PIN existe en la database


                    rootFirebase.child("eventos").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(et_pin.getText().toString())) {
                                Log.e("pin", "existe");

//                                El PIN existe


                                Intent intent = new Intent(getApplicationContext(), AttendantActivity.class);
                                intent.putExtra("pinEvent", et_pin.getText().toString());
                                startActivity(intent);


//                                ****


                            } else {
                                new AlertDialog.Builder(WelcomeActivity.this)
                                        .setTitle("Sorry :(")
                                        .setMessage("The PIN you entered doesn't exist.")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
//
//


//                ****


            }
        });







        b_own_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    pin = generatePIN();

                    Log.e("sobre el pin",pin);



                    rootFirebase.child("eventos").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild(pin)){

                                Log.e("sobre el pin","pin existe");

                                new AlertDialog.Builder(WelcomeActivity.this)
                                        .setTitle("Sorry :(")
                                        .setMessage("Please try again :).")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();


                            }else{
                                Log.e("sobre el pin","pin no existe");


//                                El PIN existe


                                Intent intent = new Intent(getApplicationContext(), ModeratorClock.class);

                                intent.putExtra("tipoUsuario", "moderador");
                                intent.putExtra("nickUsuario","moderador");
                                intent.putExtra("eventPin",pin);
                                startActivity(intent);


//                                ****

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }







        });

    }


    public String generatePIN()
    {

        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;


        return String.valueOf(randomPIN);
    }

}
