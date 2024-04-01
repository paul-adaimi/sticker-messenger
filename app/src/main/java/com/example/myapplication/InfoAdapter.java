package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoViewHolder>{

    private final List<Info> infoList;
    private final Context context;
    private final String username;

    public InfoAdapter(List<Info> infoList, Context context, String username) {
        this.infoList = infoList;
        this.context = context;
        this.username = username;
    }
    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InfoViewHolder(LayoutInflater.from(context).inflate(R.layout.info_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
        String senderName = infoList.get(position).senderName;
        String receiverName = infoList.get(position).receiverName;
        holder.senderName.setText(senderName);
        holder.receiverName.setText(receiverName);
        holder.message.setImageResource(getImageResourceId(infoList.get(position).messageId));
        long time = infoList.get(position).timeStamp;
        Date date = new Date(time);

        if(senderName.equals(username)) {
            holder.messageLayout.setBackgroundColor(0xFF8BC34A);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        String formattedDate = sdf.format(date);
        holder.time.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public int getImageResourceId(int messageId) {
        switch (messageId) {
            case 1:
                return R.drawable.sticker_22; // 假设image1是你的图片资源名称
            case 2:
                return R.drawable.sticker_7;
            case 3:
                return R.drawable.sticker_2;
            // 以此类推
            default:
                return R.drawable.sticker_22; // 默认图片
        }
    }
}
