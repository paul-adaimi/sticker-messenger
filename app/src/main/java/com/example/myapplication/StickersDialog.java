package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StickersDialog extends AppCompatDialogFragment {
    ArrayList<ImageView> stickers = new ArrayList<>();
    private StickersDialogListener listener;
    TextView sent1;
    TextView received1;
    TextView sent2;
    TextView received2;
    TextView sent3;
    TextView received3;
    String senderId;
    int selectedId;
    FirebaseDatabase database;
    DatabaseReference dbCountRef;

    public StickersDialog(String senderId) {
        this.senderId = senderId;
        database = FirebaseDatabase.getInstance();
        dbCountRef = database.getReference("count");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_stickers_dialog, null);

        builder.setView(view)
         .setTitle("Choose Sticker")
         .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.applyStickerId(selectedId);
            }
        });

        sent1 = view.findViewById(R.id.sent_1);
        received1 = view.findViewById(R.id.received_1);
        sent2 = view.findViewById(R.id.sent_2);
        received2 = view.findViewById(R.id.received_2);
        sent3 = view.findViewById(R.id.sent_3);
        received3 = view.findViewById(R.id.received_3);

        addStickers(view);
        showCounts(senderId);

        return builder.create();
    }

    private void addStickers(View view) {
        stickers.add(view.findViewById(R.id.sticker_1));
        stickers.add(view.findViewById(R.id.sticker_2));
        stickers.add(view.findViewById(R.id.sticker_3));

        for(ImageView imageView: stickers) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(ImageView imageView: stickers) {
                        imageView.setBackgroundColor(getResources().getColor(R.color.white));
                    }

                    selectedId = stickers.indexOf(view) + 1;
                    view.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }
            });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (StickersDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public interface StickersDialogListener {
        void applyStickerId(int stickerId);
    }

    private void showCounts(String senderId) {
        dbCountRef.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot sentSnapshot = snapshot.child("sent");
                DataSnapshot receivedSnapshot = snapshot.child("received");

                for (DataSnapshot childSnapshot : sentSnapshot.getChildren()) {
                    switch (childSnapshot.getKey()) {
                        case "1":
                            sent1.setText("sent: " + childSnapshot.getValue().toString());
                            break;
                        case "2":
                            sent2.setText("sent: " + childSnapshot.getValue().toString());
                            break;
                        case "3":
                            sent3.setText("sent: " + childSnapshot.getValue().toString());
                            break;
                        default:
                            break;
                    }
                }

                for(DataSnapshot childSnapshot : receivedSnapshot.getChildren()) {
                    switch(childSnapshot.getKey()) {
                        case "1":
                            received1.setText("received: " + childSnapshot.getValue().toString());
                            break;
                        case "2":
                            received2.setText("received: " + childSnapshot.getValue().toString());
                            break;
                        case "3":
                            received3.setText("received: " + childSnapshot.getValue().toString());
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

