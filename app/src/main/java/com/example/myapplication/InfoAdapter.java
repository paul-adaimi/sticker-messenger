package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoViewHolder>{

    private final List<Info> infoList;
    private final Context context;

    public InfoAdapter(List<Info> infoList, Context context) {
        this.infoList = infoList;
        this.context = context;
    }
    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InfoViewHolder(LayoutInflater.from(context).inflate(R.layout.info_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
        holder.senderName.setText(infoList.get(position).senderName);
        holder.receiverName.setText(infoList.get(position).receiverName);
        holder.message.setImageResource(getImageResourceId(infoList.get(position).messageId));
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public int getImageResourceId(int messageId) {
        switch (messageId) {
            case 1:
                return R.drawable.happy; // 假设image1是你的图片资源名称
            case 2:
                return R.drawable.sad;
            case 3:
                return R.drawable.angry;
            // 以此类推
            default:
                return R.drawable.happy; // 默认图片
        }
    }
}
