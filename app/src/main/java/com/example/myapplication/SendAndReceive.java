package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

public class SendAndReceive extends AppCompatActivity {

    RecyclerView infoRecyclerView;
    List<Info> infoList; // 新建一个list，list内部可以容纳Website实例
    InfoAdapter adapter; // 定义适配器为成员变量，以便在其他方法中也可以访问

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_and_receive);

        infoRecyclerView = findViewById(R.id.recyclerView);

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
}