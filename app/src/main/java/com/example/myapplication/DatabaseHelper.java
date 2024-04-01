package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DatabaseHelper {
    FirebaseDatabase database;
    DatabaseReference dbUsersRef;
    DatabaseReference dbMessageRef;
    DatabaseReference dbTokenRef;

    final String serverKey = "key=AAAAqdJnky8:APA91bGB5qkFQars2eVUhWEqXfl24iLWtOjIICE7fS7lW5U4lqGI6muueKSOlBPBAsr3NW0Y_BkI03_s1liCMQMtHYOzHaL5s25FAtWcjGLBqseGvDSiAOIYTfSW_BO4NE686SguT7MZ";
    final String willserverKey = "key=AAAAWavgJxs:APA91bE6NgLMEJtg0apu7se8nuldhDaJBW4UsRYQZHEosZfgLn-_vGRR6CLBur3AsXpf3qMGt0BUlB0V20Fp98UKwW-TD525lIqLpTPYBl3O2SJBpWp2124PDxd4jFfJeH337UQdbPF3";


    public DatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        dbUsersRef = database.getReference("names");
        dbMessageRef = database.getReference("messages");
        dbTokenRef = database.getReference("tokens");
    }

    void isUserPresent(String name, CheckValueCallback callBack) {
        dbUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getValue().toString().equals(name)) {
                        callBack.onValueChecked(dataSnapshot1.getKey());
                        return;
                    }
                }
                callBack.onValueChecked(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("WILL", "Failed to read value.", error.toException());
            }
        });
    }

    void getUserToken(String userId, GetTokenCallback tokenCallback) {
        dbTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String token = dataSnapshot.child(userId).getValue().toString();
                tokenCallback.getToken(token);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("WILL", "Failed to read value.", error.toException());
            }
        });
    }

    void idToName(String SenderId, String ReceiverId, FindNameCallback callBack) {
        dbUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String senderId="";
                String receiverId="";
                String senderName="";
                String receiverName="";
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().toString().equals(SenderId)) {
                        senderId = dataSnapshot1.getKey().toString();
                        senderName = dataSnapshot1.getValue().toString();
                    }
                    if (dataSnapshot1.getKey().toString().equals(ReceiverId)) {
                        receiverId = dataSnapshot1.getKey().toString();
                        receiverName = dataSnapshot1.getValue().toString();
                    }
                }
                callBack.findName(senderId,receiverId,senderName,receiverName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("WILL", "Failed to read value.", error.toException());
            }
        });
    }

    public interface CheckValueCallback {
        void onValueChecked(String existingUserId);
    }

    public interface FindNameCallback {
        void findName(String SenderId,String ReceiverId,String SenderName,String ReceiverName);
    }

    public interface GetTokenCallback {
        void getToken(String token);
    }

    public void sendMessageToDevice(String from, String toToken, String imageUrl) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();

        try {
            jNotification.put("title", "Message From " + from);
            jNotification.put("body", "Message Body");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("image", imageUrl);

            jData.put("title", "Data title");
            jData.put("content", "Data Content");

            jPayload.put("to", toToken);
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", willserverKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes("UTF-8"));
            outputStream.close();

            // Check response code to determine success or failure
            int responseCode = conn.getResponseCode();
            System.out.println("FCM response code: " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
