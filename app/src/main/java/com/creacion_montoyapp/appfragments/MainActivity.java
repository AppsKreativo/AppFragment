package com.creacion_montoyapp.appfragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Context context;
    Timer timer = new Timer();
    Dormir dormir = new Dormir();
    Fragment fragment = null;
    FragmentPantalla1 fragmentPantalla1 = new FragmentPantalla1();
    InfoFragment infoFragment = new InfoFragment();
    ProgressDialog progressDialog;
    ExoPlayer player;
    AudioAttributes audioAttributes;
    public boolean botonPulsado,botonSleep;
    float ancho_pantalla;
    String emisora = "https://radio.veracruzstereo.com:20000/stream";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        TimerTask timerPresentacion = new TimerTask() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedorTitulo,new fragmentTitulo()).commit();
                fragment = new FragmentPantalla1();
                fragmentEmisora();
            }
        };

        timer.schedule(timerPresentacion,4000);

        comenzarServicio();//Se comienza el servicio en segundo plano

        player = new ExoPlayer.Builder(MainActivity.this).build();

        //Código para el Foco de Audio de ExoPlayer
        //************************************************************
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build();

        player.setAudioAttributes(audioAttributes, true);
        //************************************************************

        anchoPantalla();

    }

    public void reproductor(){

        if (botonPulsado) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Emisora cargando");
            progressDialog.setMessage("Espera unos segundos...");
            progressDialog.show();

            //Se establece el origen del audio a reproducir.
            MediaItem mediaItem = MediaItem.fromUri(emisora);
            // Se establece el elemento multimedia que se reproducirá.
            player.setMediaItem(mediaItem);
            // Se prepara el reproductor.
            player.prepare();
            player.setVolume(1.0f);
            // Se inicia la reproduccion.
            player.play();


            player.addListener(new Player.Listener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    Player.Listener.super.onIsPlayingChanged(isPlaying);
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    botonPulsado = isPlaying;
                    fragmentPantalla1.errorReproductor(botonPulsado);
                }
            });

            player.addListener(new Player.Listener() {
                @Override
                public void onPlayerError(@NonNull PlaybackException error) {
                    Player.Listener.super.onPlayerError(error);
                    botonPulsado = !botonPulsado;
                    fragmentPantalla1.errorReproductor(botonPulsado);
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    if (Objects.equals(error.getMessage(), "Source error")){
                        Toast.makeText(MainActivity.this, "Fallas de origen. Intenta más tarde.", Toast.LENGTH_LONG).show();
                    }
                    player.stop();

                }
            });

        } else {
            player.stop();
        }

    }


    //Función del botón atras del dispositivo
    @Override
    public void onBackPressed() {
        if (fragment instanceof InfoFragment){
            fragmentEmisora();
        } else if (fragment instanceof FragmentPantalla1){
            mensajeCerrarApp();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        fragmentPantalla1.anchoLetra(ancho_pantalla);

        /*player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                Toast.makeText(MainActivity.this,
                        "Está reproduciendo "+isPlaying, Toast.LENGTH_SHORT).show();
                botonPulsado = isPlaying;
                fragmentPantalla1.errorReproductor(botonPulsado);
            }
        });*/

    }


    //********************************************************************************
    //Metodo para mostrar y ocultar menu
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Metodo para asignar funciones al menu
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.itemSleep) {
            dormir.anchoLetra(ancho_pantalla);
            dormir.dialogoSleep(context);

        } else if (id == R.id.itemInfo){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedorReemplazar,infoFragment).commit();
            fragment = new InfoFragment();
            infoFragment.anchoLetra(ancho_pantalla);
        } else  if (id == R.id.itemEmisora) {
            fragmentEmisora();

        } else  if (id == R.id.itemPolitica){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://appskreativo.blogspot.com/2022/08/politica-de-privacidad.html"));
            startActivity(intent);

        } else if (id == R.id.itemSalir){
            mensajeCerrarApp();
        }
        return super.onOptionsItemSelected(item);
    }

    public void fragmentEmisora(){
        fragmentPantalla1.reproductorSonando(botonPulsado);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedorReemplazar,fragmentPantalla1).commit();
        fragment = new FragmentPantalla1();
    }

    //Para obtener foco del boton PlayPause del reproductor desde FragmentPantalla1
    public void playPulsado(boolean play){
        botonPulsado = play;
        reproductor();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        detenerServicio();
        if (player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public void comenzarServicio(){

        Intent serviceIntent = new Intent(this,Servicio.class);
        this.startService(serviceIntent);
    }

    public void detenerServicio(){

        Intent serviceIntent = new Intent(this,Servicio.class);
        this.stopService(serviceIntent);
    }

    // Método para calcular el ancho de la pantalla y darle tamaño a la letra.
    public void anchoPantalla() {
        Display display = MainActivity.this.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float anchoPixels = outMetrics.widthPixels;
        float densidad = outMetrics.density;

        ancho_pantalla = (anchoPixels / densidad);

    }

    //Método para recibir foco de boton modo dormir
    public void sleepActivo(boolean sleep){
        botonSleep = sleep;
    }


    //Diálogo de cierre de App
    public void mensajeCerrarApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this )
                .setMessage(R.string.mensajeDialogo)
                .setTitle(R.string.tituloDialogo)
                .setIcon(R.drawable.iconoolimpicamed)
                .setPositiveButton(R.string.boton1Dialogo, (dialog, which) -> {
                    detenerServicio();
                    finish();
                })
                .setNegativeButton(R.string.boton2Dialogo, (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}