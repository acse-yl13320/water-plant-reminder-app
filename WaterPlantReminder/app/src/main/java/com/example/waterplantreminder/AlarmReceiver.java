package com.example.waterplantreminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "", Toast.LENGTH_LONG).show();

        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "default";
            String channelName = "default";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        Intent playIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "default")
                .setAutoCancel(true)
                .setContentTitle("You need to water or feed your plants!")
                .setSmallIcon(R.drawable.default_plant_image)
                .setContentIntent(pendingIntent)
                .setVibrate(new long [] {0, 1500, 1000, 1000});
        manager.notify(1, notification.build());

    }
}


