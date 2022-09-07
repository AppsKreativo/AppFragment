package com.creacion_montoyapp.appfragments;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notificacion extends Application {

    public static final String CHANNEL_ID = "notificacionServicio";

    @Override
    public void onCreate() {
        super.onCreate();

        creandoCanalNotificacion();
    }

    private void creandoCanalNotificacion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel canalServicio  = new NotificationChannel(CHANNEL_ID,"Servicio Emisora"
            , NotificationManager.IMPORTANCE_LOW);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(canalServicio);
        }
    }
}
