package com.example.ceon390_projectgroup;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID = "Channel";

    @Override
    public void onCreate(){
        super.onCreate();
        createNotificationChannel();
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Warning Level", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getString(R.string.ChannelDescription));
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}

