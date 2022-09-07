package com.creacion_montoyapp.appfragments;

import static com.creacion_montoyapp.appfragments.Notificacion.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;



public class Servicio extends Service {


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Intent notificationIntent = new Intent(this,MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(Servicio.this,0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(Servicio.this,CHANNEL_ID)
                .setContentTitle("Mi Servicio")
                .setContentText("Veracruz Estéreo")
                .setSmallIcon(R.drawable.radio)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() { super.onDestroy(); }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
