package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

public class SendAndReceive extends AppCompatActivity implements StickersDialog.StickersDialogListener {
    FirebaseDatabase database;
    DatabaseReference dbMessageRef;
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
        dbMessageRef = database.getReference("messages");
        databaseHelper = new DatabaseHelper();

        Intent intent = getIntent();
        senderId = intent.getStringExtra("userId");

        infoRecyclerView = findViewById(R.id.recyclerView);
        selectedSticker = findViewById(R.id.selected_sticker);
        receiverNameView = findViewById(R.id.receiver_name);

        // initialize infoList
        infoList = new ArrayList<>();

        // add several Info instances to infoList
        //infoList.add(new Info(1,2,"Alice", "Bob", 1));
        //infoList.add(new Info(3,4,"Charlie", "David", 2));
        //infoList.add(new Info(5,6,"Eve", "Frank", 3));

        // 设置 RecyclerView 的 LayoutManager 和 Adapter
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InfoAdapter(infoList, this);
        infoRecyclerView.setAdapter(adapter);

        updateInfoList();
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
                    DatabaseReference receiverRef = dbMessageRef.child(existingUserId).child(messageId);
                    DatabaseReference senderRef = dbMessageRef.child(senderId).child(messageId);

                    senderRef.child("sender").setValue(senderId);
                    senderRef.child("receiver").setValue(existingUserId);
                    senderRef.child("message").setValue(selectedStickerId);
                    senderRef.child("timeStamp").setValue(System.currentTimeMillis());

                    receiverRef.child("sender").setValue(senderId);
                    receiverRef.child("receiver").setValue(existingUserId);
                    receiverRef.child("message").setValue(selectedStickerId);
                    receiverRef.child("timeStamp").setValue(System.currentTimeMillis());

                    updateInfoList();
                } else {
                    Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        };
        databaseHelper.isUserPresent(receiverNameView.getText().toString(), callback);
    }


    public void updateInfoList() {
        infoList.clear();
        dbMessageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().toString().equals(senderId)) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            String senderId = dataSnapshot2.child("sender").getValue().toString();
                            String receiverId = dataSnapshot2.child("receiver").getValue().toString();
                            int message = Integer.parseInt(dataSnapshot2.child("message").getValue().toString());
                            long timeStamp = Long.parseLong(dataSnapshot2.child("timeStamp").getValue().toString());
                            DatabaseHelper.FindNameCallback callback = new DatabaseHelper.FindNameCallback() {
                                @Override
                                public void findName(String SenderId, String ReceiverId, String senderName, String receiverName) {
                                    infoList.add(new Info(SenderId, ReceiverId, senderName, receiverName, message, timeStamp));
                                }
                            };
                            databaseHelper.idToName(senderId, receiverId, callback);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //sort by timestamp？？？
        infoList.sort((o1, o2) -> Long.compare(o2.timeStamp, o1.timeStamp));
        adapter.notifyDataSetChanged();
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