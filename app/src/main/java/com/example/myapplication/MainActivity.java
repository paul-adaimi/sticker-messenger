package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button loginButton;
    EditText userName;

    FirebaseDatabase database;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("names");

        loginButton = findViewById(R.id.button);
        userName = findViewById(R.id.edit_user_name);
        loginButton.setOnClickListener(view -> {
            testName(userName.getText().toString());
        });
    }

    private void testName(String userName) {
        CheckValueCallback callback = new CheckValueCallback() {
            @Override
            public void onValueChecked(String existingUserId) {
                String id;
                if (existingUserId != null) {
                    id = existingUserId;
                } else {
                    String uuid = UUID.randomUUID().toString();
                    id = uuid;
                    dbRef.child(uuid).setValue(userName);
                }

                Intent intent = new Intent(MainActivity.this, SendAndReceive.class);
                intent.putExtra("userId", id);
                startActivity(intent);
            }
        };

        isUserPresent(userName, callback);
    }

    private void isUserPresent(String name, CheckValueCallback callBack) {
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