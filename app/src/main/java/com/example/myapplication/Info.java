package com.example.myapplication;

public class Info {
    public String senderId;
    public String receiverId;
    public String senderName;
    public String receiverName;
    public int messageId;
    long timeStamp;

    public Info(String senderId, String receiverId, String senderName, String receiverName, int messageId, long timeStamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.messageId = messageId;
        this.timeStamp = timeStamp;
    }
}
