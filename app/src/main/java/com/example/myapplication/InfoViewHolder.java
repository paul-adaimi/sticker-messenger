package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InfoViewHolder extends RecyclerView.ViewHolder{
    public TextView senderName;
    public TextView receiverName;
    public ImageView message;

    public InfoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.senderName = itemView.findViewById(R.id.textViewSenderName);
        this.receiverName = itemView.findViewById(R.id.textViewRecipientName);
        this.message = itemView.findViewById(R.id.imageViewPicture);
    }


}
