package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendAndReceive extends AppCompatActivity implements StickersDialog.StickersDialogListener {
    FirebaseDatabase database;

    DatabaseReference dbRef;

    DatabaseHelper databaseHelper;

    EditText receiverNameView;
    ImageView selectedSticker;
    RecyclerView infoRecyclerView;
    List<Info> infoList; // 新建一个list，list内部可以容纳Website实例
    InfoAdapter adapter; // 定义适配器为成员变量，以便在其他方法中也可以访问
    String senderId;
    int selectedStickerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_and_receive);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("messages");
        databaseHelper = new DatabaseHelper();

        Intent intent = getIntent();
        senderId = intent.getStringExtra("userId");

        infoRecyclerView = findViewById(R.id.recyclerView);
        selectedSticker = findViewById(R.id.selected_sticker);
        receiverNameView = findViewById(R.id.receiver_name);

        // initialize infoList
        infoList = new ArrayList<>();

        // add several Info instances to infoList
        infoList.add(new Info(1,2,"Alice", "Bob", 1));
        infoList.add(new Info(3,4,"Charlie", "David", 2));
        infoList.add(new Info(5,6,"Eve", "Frank", 3));

        // 设置 RecyclerView 的 LayoutManager 和 Adapter
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InfoAdapter(infoList, this);
        infoRecyclerView.setAdapter(adapter);
    }

    public void openStickersDialog(View view) {
        StickersDialog dialog = new StickersDialog();
        dialog.show(getSupportFragmentManager(), "Stickers Dialog");
    }

    public void onSend(View view) {
        DatabaseHelper.CheckValueCallback callback = new DatabaseHelper.CheckValueCallback() {
            @Override
            public void onValueChecked(String existingUserId) {
                if (existingUserId != null) {
                    String messageId = UUID.randomUUID().toString();
                    DatabaseReference receiverRef = dbRef.child(existingUserId).child("received").child(senderId).child("messages").child(messageId);
                    DatabaseReference senderRef = dbRef.child(senderId).child("sent").child(existingUserId).child("messages").child(messageId);
                    receiverRef.child("message").setValue(selectedStickerId);
                    receiverRef.child("timeStamp").setValue(System.currentTimeMillis());
                    senderRef.child("message").setValue(selectedStickerId);
                    senderRef.child("timeStamp").setValue(System.currentTimeMillis());
                } else {
                    Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        };
        databaseHelper.isUserPresent(receiverNameView.getText().toString(), callback);

    }

    @Override
    public void applyStickerId(int stickerId) {
        selectedStickerId = stickerId;
        switch(stickerId) {
            case 1:
                selectedSticker.setImageDrawable(getDrawable(R.drawable.sticker_22));
                break;
            case 2:
                selectedSticker.setImageDrawable(getDrawable(R.drawable.sticker_7));
                break;
            case 3:
                selectedSticker.setImageDrawable(getDrawable(R.drawable.sticker_2));
                break;
            default:
        }
    }
}