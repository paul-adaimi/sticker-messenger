package com.example.myapplication;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Send your instance ID token to your app server.
//        sendRegistrationToServer(token);
    }
}
