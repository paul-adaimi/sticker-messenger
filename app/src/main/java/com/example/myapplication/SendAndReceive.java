package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class SendAndReceive extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_and_receive);
    }

    public void openStickersDialog(View view) {
        StickersDialog dialog = new StickersDialog();
        dialog.show(getSupportFragmentManager(), "Stickers Dialog");
    }
}