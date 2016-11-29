package me.danielrosero.turnometro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bcgdv.asia.lib.ticktock.TickTockView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AttendantClock extends AppCompatActivity {

    private TickTockView mCountDown;
    private TickTockView mCountUp;
    private boolean extraTime;
    private Date negativeTimeMoment = new Date();

    private int countDownSeconds,countUpSeconds;
    private DatabaseReference rootFirebase;

    private String nickUsuario;
    private String tipoUsuario;
    private TextView t_contador;
    private Button b_arranca;
    private String eventPin;
    private Vibrator vibrator;
    private boolean vibrando;
    private boolean negativo;

    private RelativeLayout main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        Oculto la barra de status

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//                ****


        setContentView(R.layout.activity_main);

        extraTime=false;

        Intent intent = getIntent();

        nickUsuario=intent.getStringExtra("nickUsuario");
        tipoUsuario=intent.getStringExtra("tipoUsuario");
        eventPin=intent.getStringExtra("eventPin");

        countDownSeconds=60;
        countUpSeconds=60;

        t_contador=(TextView)findViewById(R.id.t_contador_attendant);

        main = (RelativeLayout) findViewById(R.id.activity_main_layout);



        rootFirebase = FirebaseDatabase.getInstance().getReference();



        // Read from the database
        rootFirebase.child("eventos").child(eventPin).child("milisegundosRestantes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                long value = dataSnapshot.getValue(Long.class);
                // Log.d(TAG, "Value is: " + value);

                Date tiempo= new Date();
                SimpleDateFormat format = new SimpleDateFormat("mm:ss");

                tiempo.setTime(value);

                if(value==0){
                    extraTime=true;
                }else {

                    if(vibrando){
                        stopVibrate();
                        vibrando=false;
                    }
                    t_contador.setText(format.format(tiempo));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

//        ****


        // Read from the database
        rootFirebase.child("eventos").child(eventPin).child("milisegundosNegativos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                long value = dataSnapshot.getValue(Long.class);
                // Log.d(TAG, "Value is: " + value);




                Date tiempo= new Date();
                SimpleDateFormat format = new SimpleDateFormat("mm:ss");

                tiempo.setTime(value);

                if(extraTime) {
                    t_contador.setText("-" + format.format(tiempo));

                    if(!vibrando) {

                        startVibrate();
                        vibrando=true;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onStop() {
        super.onStop();
        mCountDown.stop();

    
    }
    public void startVibrate() {
        long pattern[] = { 0, 100, 200, 300, 400 };
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);
    }

    public void stopVibrate() {
        vibrator.cancel();
    }
}
