package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseHelper {
    FirebaseDatabase database;
    DatabaseReference dbUsersRef;
    DatabaseReference dbMessageRef;

    public DatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        dbUsersRef = database.getReference("names");
        dbMessageRef = database.getReference("messages");
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
}
