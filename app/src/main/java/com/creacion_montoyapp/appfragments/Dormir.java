package com.creacion_montoyapp.appfragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Dormir {

    float anchoPantalla;
    int tiempo = 0;
    long tiempoMilisegundos;
    boolean tiempoActivado;
    TextView tvSleep1,tvSleep2,tvSleep3,tvSleep4,tvSleep5;
    Button btnActivar;
    Spinner spinnerTiempo;
    LinearLayout layoutSpinner;
    CountDownTimer timerSleep;
    MainActivity mainActiviy;

    //Dialogo Sleep ***********************************
    Dialog dialogSleep;

    //Conexión Diseño Cuadro diálogo sleep
    public void dialogoSleep(Context context){

        dialogSleep = new Dialog(context);
        mainActiviy = new MainActivity();

        dialogSleep.setCancelable(true);
        dialogSleep.setContentView(R.layout.sleep);
        //Iniciar las vistas
        tvSleep1 = dialogSleep.findViewById(R.id.tvSleep1);
        tvSleep2 = dialogSleep.findViewById(R.id.tvSleep2);
        tvSleep3 = dialogSleep.findViewById(R.id.tvSleep3);
        tvSleep4 = dialogSleep.findViewById(R.id.tvSleep4);
        tvSleep5 = dialogSleep.findViewById(R.id.tvSleep5);
        btnActivar = dialogSleep.findViewById(R.id.btnActivar);
        spinnerTiempo = dialogSleep.findViewById(R.id.spinnerTiempo);
        layoutSpinner = dialogSleep.findViewById(R.id.layoutSpinner);

        String[] opciones = {"1 min","15 min","30 min","45 min","60 min","75 min","90 min"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (context, R.layout.spinner_item_style,opciones);
        spinnerTiempo.setAdapter(adapter);


        anchoLetra(anchoPantalla);
        tvSleep1.setTextSize(anchoPantalla * 0.07F);
        tvSleep2.setTextSize(anchoPantalla * 0.05F);
        tvSleep3.setTextSize(anchoPantalla * 0.04F);
        tvSleep4.setTextSize(anchoPantalla * 0.05F);
        tvSleep5.setTextSize(anchoPantalla * 0.04F);
        btnActivar.setTextSize(anchoPantalla * 0.06F);
        layoutSpinner.setMinimumHeight((int) (anchoPantalla * 0.1F));

        Toast.makeText(context, "tiempoActivado "+tiempoActivado, Toast.LENGTH_SHORT).show();
        inicioDialogo();

        dialogSleep.show();

        btnActivar.setOnClickListener(view -> {

            if (!tiempoActivado){
                tiempoApagado();
                startTimerSleep();
                btnActivar.setText(R.string.btnSleep2);
                spinnerTiempo.setEnabled(false);
                Toast.makeText(context, "La App se apagará en "+tiempo+" minutos", Toast.LENGTH_SHORT).show();

            } else {
                timerSleep.cancel();
                btnActivar.setText(R.string.btnSleep1);
                tvSleep4.setText(R.string.tvSleep5);
                spinnerTiempo.setEnabled(true);
                Toast.makeText(context, "Se desactivó el Modo Dormir", Toast.LENGTH_SHORT).show();
            }

            tiempoActivado = !tiempoActivado;
            mainActiviy.sleepActivo(tiempoActivado);

        });
    }

    public void inicioDialogo(){

        if (tiempoActivado){
            btnActivar.setText(R.string.btnSleep2);
            spinnerTiempo.setEnabled(false);
        } else {
            btnActivar.setText(R.string.btnSleep1);
            spinnerTiempo.setEnabled(true);
        }
    }

    public void anchoLetra(float ancho){
        anchoPantalla = ancho;
    }

    public void tiempoApagado(){
        String seleccion_String = spinnerTiempo.getSelectedItem().toString();
        switch (seleccion_String){
            case "1 min":
                tiempo = 1;
                break;
            case "15 min":
                tiempo = 15;
                break;
            case "30 min":
                tiempo = 30;
                break;
            case "45 min":
                tiempo = 45;
                break;
            case "60 min":
                tiempo = 60;
                break;
            case "75 min":
                tiempo = 75;
                break;
            case "90 min":
                tiempo = 90;
                break;
        }
        tiempoMilisegundos = tiempo * 60000L;
    }

    public void startTimerSleep(){
        timerSleep = new CountDownTimer(tiempoMilisegundos,1000) {
            @Override
            public void onTick(long l) {
                //Cuando marque
                //Se convierten los milisegundos en Minutos y Segundos
                String tiempoConvertido = String.format(Locale.US,"%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                //Cuando falten 30 segundos se muestra un mensaje
                if ((int)l/1000 == 30){
                    Toast.makeText(dialogSleep.getContext(), "La App de apagará en 30 segundos", Toast.LENGTH_SHORT).show();
                }

                //El tiempoConvertido se establece en la etiqueta tvSleep5
                tvSleep4.setText(tiempoConvertido);
            }

            @Override
            public void onFinish() {
                //Cuando finalice el conteo
                //Se detiene el servicio de segundo plano
                Intent serviceIntent = new Intent(dialogSleep.getContext(),Servicio.class);
                dialogSleep.getContext().stopService(serviceIntent);
                //Se cierra la Aplicación
                System.exit(0);

            }
        }.start();
    }

}
