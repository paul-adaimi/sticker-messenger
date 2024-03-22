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

    DatabaseReference dbRef;

    public DatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("names");
    }

    void isUserPresent(String name, CheckValueCallback callBack) {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    public interface CheckValueCallback {
        void onValueChecked(String existingUserId);
    }
}
