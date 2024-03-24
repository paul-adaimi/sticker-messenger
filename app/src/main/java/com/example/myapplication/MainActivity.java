package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button loginButton;
    EditText userName;

    FirebaseDatabase database;

    DatabaseReference dbRef;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("names");
        databaseHelper = new DatabaseHelper();

        loginButton = findViewById(R.id.button);
        userName = findViewById(R.id.edit_user_name);
        loginButton.setOnClickListener(view -> {
            login(userName.getText().toString());
        });
    }

    private void login(String userName) {
        DatabaseHelper.CheckValueCallback callback = new DatabaseHelper.CheckValueCallback() {
            @Override
            public void onValueChecked(String existingUserId) {
                String id;
                if (existingUserId != null) {
                    id = existingUserId;
                } else {
                    String uuid = UUID.randomUUID().toString();
                    id = uuid;
                    dbRef.child(uuid).setValue(userName);
                    initializeCounts(id);

                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        return;
                                    }

                                    // Get new FCM registration token
                                    String token = task.getResult();
                                    DatabaseReference tokenRef = database.getReference("tokens");
                                    tokenRef.child(id).setValue(token);
                                }
                            });
                }

                Intent intent = new Intent(MainActivity.this, SendAndReceive.class);
                intent.putExtra("userId", id);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        };

        databaseHelper.isUserPresent(userName, callback);
    }

    public void initializeCounts(String id) {
        DatabaseReference dbCountRef = database.getReference("count");

        DatabaseReference sentRef = dbCountRef.child(id).child("sent");
        DatabaseReference receivedRef =  dbCountRef.child(id).child("received");
        setStickersZero(sentRef);
        setStickersZero(receivedRef);
    }

    private void setStickersZero(DatabaseReference ref) {
        for(int i = 1; i < 4; i++) {
            ref.child(String.valueOf(i)).setValue(0);
        }
    }
}