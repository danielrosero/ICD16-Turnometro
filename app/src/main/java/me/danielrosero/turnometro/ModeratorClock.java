package me.danielrosero.turnometro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcgdv.asia.lib.ticktock.TickTockView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ModeratorClock extends AppCompatActivity {

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
    private TextView t_pin;
    private Button b1,b2,b3,b4,b5;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout leftRL;
    private TextView t_share_title;

    private Button b_menu;

    private ImageButton b6;


    private TextView t_time_title,t_pin_title;
    private String m_Text;


    private RelativeLayout main;

    private boolean tocoPrimeraVez;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        Oculto la barra de status

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//                ****


        setContentView(R.layout.activity_moderatorclock);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle("HABLAMELAS");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("SISARRAS");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        //mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu_camera);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        leftRL = (LinearLayout) findViewById(R.id.left_drawer);

        extraTime=false;

        Intent intent = getIntent();

        nickUsuario=intent.getStringExtra("nickUsuario");
        tipoUsuario=intent.getStringExtra("tipoUsuario");
        eventPin=intent.getStringExtra("eventPin");

        countDownSeconds=60;
        countUpSeconds=60;

        tocoPrimeraVez=true;

        t_contador=(TextView)findViewById(R.id.t_contador_moderator);

        //t_sonido=(TextView)findViewById(R.id.t_sounds);


        main = (RelativeLayout)findViewById(R.id.activity_main_layout);


        t_pin=(TextView)findViewById(R.id.t_pin_moderador);

        b1 = (Button)findViewById(R.id.b_30);
        b2 = (Button)findViewById(R.id.b_60);
        b3 = (Button)findViewById(R.id.b_2m);
        b4 = (Button)findViewById(R.id.b_3m);
        b5 = (Button)findViewById(R.id.b_5m);
        b6 = (ImageButton)findViewById(R.id.b_custom);



        b_menu = (Button) findViewById(R.id.b_menu);

        t_pin.setText(eventPin);

        t_time_title = (TextView) findViewById(R.id.t_time_title);
        t_pin_title = (TextView) findViewById(R.id.t_pin_title);


        rootFirebase = FirebaseDatabase.getInstance().getReference();


        //                        Firebase
        Map<String, Object> evento = new HashMap<String, Object>();
        evento.put("titulo","test");
        evento.put("milisegundosNegativos",0);
        evento.put("milisegundosRestantes",-1);



//                                Pin 0001
        rootFirebase.child("eventos").child(eventPin).updateChildren(evento);
//        rootFirebase.child("eventos").child("0001").child("usuarios").child(idUsuario).setValue("");


//                        ****





        mCountDown = (TickTockView) findViewById(R.id.view_ticktock_countdown);
        mCountUp = (TickTockView) findViewById(R.id.view_ticktock_countup);


        t_share_title = (TextView) findViewById(R.id.t_share_pin);



        //        Fuentes custom


        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");
        t_pin.setTypeface(tf);
        t_contador.setTypeface(tf);

        final Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold.ttf");
        b1.setTypeface(tf2);
        b2.setTypeface(tf2);
        b3.setTypeface(tf2);
        b4.setTypeface(tf2);
        b5.setTypeface(tf2);
        //b6.setTypeface(tf2);

        final Typeface tf3 = Typeface.createFromAsset(getAssets(), "fonts/LibreFranklin-Medium.ttf");
        t_time_title.setTypeface(tf3);
        t_pin_title.setTypeface(tf3);

        Typeface tf4 = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        t_share_title.setTypeface(tf4);



//        ****



        if (mCountDown != null) {
            mCountDown.setOnTickListener(new TickTockView.OnTickListener() {
                @Override
                public String getText(long timeRemaining) {

                    if(timeRemaining==0){


                        if(mCountUp !=null && !extraTime){

                            Map<String, Object> tiempo = new HashMap<String, Object>();
                            tiempo.put("milisegundosRestantes",0);

                            rootFirebase.child("eventos").child(eventPin).updateChildren(tiempo);

                            Calendar end = Calendar.getInstance();
                            end.add(Calendar.MINUTE, 0);
                            end.add(Calendar.SECOND, countUpSeconds);

                            Calendar start = Calendar.getInstance();

                            //start.add(Calendar.MINUTE, -1)  ;
                            start.add(Calendar.MINUTE, 0)  ;
                            start.add(Calendar.SECOND, 0);

//                            mCountUp.setVisibility(View.VISIBLE);
//                            mCountDown.setVisibility(View.INVISIBLE);


                            mCountUp.start(start,end);
                            negativeTimeMoment.setTime(System.currentTimeMillis()-1000);
                            extraTime=true;


                        }

                        return "";

                    }else {


                        if(vibrando){
                            stopVibrate();
                            vibrando=false;
                        }



//                        Para el cambio de color

                        if(timeRemaining > (countDownSeconds*0.5f)*1000){
                            main.setBackgroundColor(Color.parseColor("#23BC86"));

                        }else {

                            if(timeRemaining<=10000){
                                main.setBackgroundColor(Color.parseColor("#F4361C"));

                            }else {

                                if (timeRemaining <= (countDownSeconds * 0.5f) * 1000) {
                                    main.setBackgroundColor(Color.parseColor("#FFCE00"));

                                }
                            }


                        }
//                        ****



                        int seconds = (int) (timeRemaining / 1000) % 60;
                        int minutes = (int) ((timeRemaining / (1000 * 60)) % 60);
                        int hours = (int) ((timeRemaining / (1000 * 60 * 60)) % 24);
                        int days = (int) (timeRemaining / (1000 * 60 * 60 * 24));
                        boolean hasDays = days > 0;


                        Map<String, Object> tiempo = new HashMap<String, Object>();
                        tiempo.put("milisegundosRestantes",timeRemaining);

                        rootFirebase.child("eventos").child(eventPin).updateChildren(tiempo);




                        if(timeRemaining<60000){
                            t_contador.setText(timeRemaining/1000+"");
                        }else {

                            t_contador.setText(String.format("%2$02d%5$s" + ":" + "%3$02d%6$s",
                                    hasDays ? days : hours,
                                    hasDays ? hours : minutes,
                                    hasDays ? minutes : seconds,
                                    hasDays ? "d" : "",
                                    hasDays ? "h" : "",
                                    hasDays ? "m" : ""));

                        }

                        return String.format("%2$02d%5$s" + ":" + "%3$02d%6$s",
                                hasDays ? days : hours,
                                hasDays ? hours : minutes,
                                hasDays ? minutes : seconds,
                                hasDays ? "d" : "",
                                hasDays ? "h" : "",
                                hasDays ? "m" : "");
                    }
                }
            });
        }

        if (mCountUp != null) {
            mCountUp.setOnTickListener(new TickTockView.OnTickListener() {
                SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                Date date = new Date();
                @Override
                public String getText(long timeRemaining) {
                    //date.setTime(System.currentTimeMillis());


                    main.setBackgroundColor(Color.parseColor("#D3001A"));




                    date.setTime(System.currentTimeMillis()-negativeTimeMoment.getTime());

                    Map<String, Object> tiempo = new HashMap<String, Object>();
                    tiempo.put("milisegundosNegativos",System.currentTimeMillis()-negativeTimeMoment.getTime());

                    rootFirebase.child("eventos").child(eventPin).updateChildren(tiempo);



                    if(!vibrando) {

                        startVibrate();
                        vibrando=true;
                    }


                    if(timeRemaining<60000){
                        t_contador.setText("-"+(System.currentTimeMillis()-negativeTimeMoment.getTime())/1000);
                    }else {
                        t_contador.setText("-"+format.format(date));
                    }

                    return format.format(date);

                    //long secondsVa = 1+(date.getTime()-negativeTimeMoment.getTime())/1000;

                    //Log.e("Tiempo extra",secondsVa+"");

                   // return "-"+secondsVa;

                }
            });
        }


//
////        Touch listener ticktock
//
//
//            mCountDown.setOnClickListener(new View.OnClickListener() {
//
//
//                @Override
//                public void onClick(View view) {
//
//                    mCountUp.setVisibility(View.INVISIBLE);
//                    mCountDown.setVisibility(View.VISIBLE);
//
//                    Calendar end = Calendar.getInstance();
//                    end.add(Calendar.MINUTE, 0);
//                    end.add(Calendar.SECOND, countDownSeconds);
//
//                    Calendar start = Calendar.getInstance();
//
//                    start.add(Calendar.MINUTE, 0)  ;
//                    start.add(Calendar.SECOND, 0);
//                    if (mCountDown != null) {
//                        mCountDown.start(start, end);
//
//                    }
//                }
//            });
//
////        ****
//
//
//        mCountUp.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
//
//                mCountUp.setVisibility(View.INVISIBLE);
//                mCountDown.setVisibility(View.VISIBLE);
//
//                Calendar end = Calendar.getInstance();
//                end.add(Calendar.MINUTE, 0);
//                end.add(Calendar.SECOND, countDownSeconds);
//
//                Calendar start = Calendar.getInstance();
//
//                start.add(Calendar.MINUTE, 0)  ;
//                start.add(Calendar.SECOND, 0);
//                if (mCountDown != null) {
//                    mCountDown.start(start, end);
//
//                }
//            }
//        });

//        Si el usuario es moderador puede reiniciar el tiempo

if(tipoUsuario.equals("moderador")) {

        t_contador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!tocoPrimeraVez){
                    tocoPrimeraVez=true;
                    mCountDown.stop();
                    mCountUp.stop();
                    extraTime=false;
                    vibrando=false;


                    Log.e("dale","dale");

                    //t_contador.setText(""+countDownSeconds);

                    SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                    Date tiempo = new Date();
                    tiempo.setTime(countDownSeconds*1000);


                    Map<String, Object> tiempo2 = new HashMap<String, Object>();
                    tiempo2.put("milisegundosRestantes",countDownSeconds*1000);

                    rootFirebase.child("eventos").child(eventPin).updateChildren(tiempo2);


                    main.setBackgroundColor(Color.parseColor("#1670D9"));

                }else {
                    Calendar end = Calendar.getInstance();
                    end.add(Calendar.MINUTE, 0);
                    end.add(Calendar.SECOND, countDownSeconds);

                    Calendar start = Calendar.getInstance();

                    start.add(Calendar.MINUTE, 0)  ;
                    start.add(Calendar.SECOND, 0);
                    if (mCountDown != null) {
                        mCountDown.start(start, end);

                    }

                    tocoPrimeraVez=false;
                }



            }
        });
}
//        ****


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

                if(value<60000) {

                    t_contador.setText(value/1000+"");
                }else{
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

                if(value<60000) {

                    t_contador.setText(value/1000+"");
                }else{
                    t_contador.setText(format.format(tiempo));
                }

                if(extraTime) {
                    if(value<60000) {

                        t_contador.setText("-"+value/1000+"");
                    }else{
                        t_contador.setText("-"+format.format(tiempo));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

//        ****


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tocoPrimeraVez=true;
                extraTime=false;

                main.setBackgroundColor(Color.parseColor("#1670D9"));

                countDownSeconds=30;
                countUpSeconds=30;



                mCountDown.stop();
                mCountUp.stop();

                Map<String, Object> evento = new HashMap<String, Object>();
                evento.put("titulo","test");
                evento.put("milisegundosNegativos",30*1000);
                evento.put("milisegundosRestantes",30*1000);




                rootFirebase.child("eventos").child(eventPin).updateChildren(evento);


                mDrawerLayout.closeDrawer(Gravity.LEFT);

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countDownSeconds=60;
                countUpSeconds=60;

                extraTime=false;

                tocoPrimeraVez=true;

                main.setBackgroundColor(Color.parseColor("#1670D9"));


                mCountDown.stop();
                mCountUp.stop();

                Map<String, Object> evento = new HashMap<String, Object>();
                evento.put("titulo","test");
                evento.put("milisegundosNegativos",60*1000);
                evento.put("milisegundosRestantes",60*1000);




                rootFirebase.child("eventos").child(eventPin).updateChildren(evento);


                mDrawerLayout.closeDrawer(Gravity.LEFT);

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countDownSeconds=60*2;
                countUpSeconds=60*2;

                extraTime=false;

                tocoPrimeraVez=true;

                main.setBackgroundColor(Color.parseColor("#1670D9"));

                mCountDown.stop();
                mCountUp.stop();

                Map<String, Object> evento = new HashMap<String, Object>();
                evento.put("titulo","test");
                evento.put("milisegundosNegativos",60*2*1000);
                evento.put("milisegundosRestantes",60*2*1000);




                rootFirebase.child("eventos").child(eventPin).updateChildren(evento);


                mDrawerLayout.closeDrawer(Gravity.LEFT);

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countDownSeconds=60*3;
                countUpSeconds=60*3;


                extraTime=false;

                tocoPrimeraVez=true;

                main.setBackgroundColor(Color.parseColor("#1670D9"));

                mCountDown.stop();
                mCountUp.stop();

                Map<String, Object> evento = new HashMap<String, Object>();
                evento.put("titulo","test");
                evento.put("milisegundosNegativos",60*3*1000);
                evento.put("milisegundosRestantes",60*3*1000);




                rootFirebase.child("eventos").child(eventPin).updateChildren(evento);


                mDrawerLayout.closeDrawer(Gravity.LEFT);

            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countDownSeconds=60*5;
                countUpSeconds=60*5;

                extraTime=false;

                tocoPrimeraVez=true;

                main.setBackgroundColor(Color.parseColor("#1670D9"));

                mCountDown.stop();
                mCountUp.stop();

                Map<String, Object> evento = new HashMap<String, Object>();
                evento.put("titulo","test");
                evento.put("milisegundosNegativos",60*5*1000);
                evento.put("milisegundosRestantes",60*5*1000);




                rootFirebase.child("eventos").child(eventPin).updateChildren(evento);


                mDrawerLayout.closeDrawer(Gravity.LEFT);

            }
        });


        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Log.e("clickea","clickea");

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ModeratorClock.this, R.style.AlertDialogCustom));
                builder.setTitle("¿How many seconds?");

// Set up the input
                final EditText input = new EditText(getApplicationContext());




                input.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                input.setTypeface(tf3);



// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();

                        Date tiempo= new Date();
                        SimpleDateFormat format = new SimpleDateFormat("mm:ss");

                        tiempo.setTime(Integer.parseInt(m_Text)*1000);

                        countDownSeconds=Integer.parseInt(m_Text);
                        countUpSeconds=Integer.parseInt(m_Text);

                        extraTime=false;
                        tocoPrimeraVez=true;
                        main.setBackgroundColor(Color.parseColor("#1670D9"));








                        Map<String, Object> evento = new HashMap<String, Object>();
                        evento.put("titulo","test");
                        evento.put("milisegundosNegativos",countUpSeconds*1000);
                        evento.put("milisegundosRestantes",countDownSeconds*1000);




                        rootFirebase.child("eventos").child(eventPin).updateChildren(evento);


                        mCountDown.stop();
                        mCountUp.stop();

                        t_contador.setText(format.format(tiempo));

                        mDrawerLayout.closeDrawer(Gravity.LEFT);



                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTypeface(tf3);
                nbutton.setTextColor(Color.WHITE);
                //nbutton.setBackgroundColor(Color.MAGENTA);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTypeface(tf3);
                pbutton.setTextColor(Color.WHITE);
                //pbutton.setBackgroundColor(Color.YELLOW);
               // builder.show();


            }
        });




//        Boton de menu

//        final Drawable upArrow = getResources().getDrawable(R.drawable.menu);
//        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
//
//        int sdk = android.os.Build.VERSION.SDK_INT;
//        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            b_menu.setBackgroundDrawable(upArrow);
//        } else {
//            b_menu.setBackground(upArrow);
//        }




        b_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

//        ****






//        final float density = getResources().getDisplayMetrics().density;
//        final Drawable drawable = getResources().getDrawable(R.drawable.lapiz);
//
//        final int width = Math.round(2 * density);
//        final int height = Math.round(2 * density);
//
//        drawable.setBounds(0, 0, width, height);
//        b6.setCompoundDrawables(drawable, null, null, null);

    }


    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onStop() {
        super.onStop();

        //stopVibrate();
//        mCountDown.stop();

    
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
