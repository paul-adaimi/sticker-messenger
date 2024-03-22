package com.example.myapplication;

public class Info {
    public int senderId;
    public int receiverId;
    public String senderName;
    public String receiverName;
    public int messageId;

    public Info(int senderId, int receiverId, String senderName, String receiverName, int messageId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.messageId = messageId;
    }
}
