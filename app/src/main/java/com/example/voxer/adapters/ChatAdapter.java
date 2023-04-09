package com.example.voxer.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voxer.activities.ChatActivity;
import com.example.voxer.databinding.ItemContainerRecievedMessageBinding;
import com.example.voxer.databinding.ItemContainerSentMessageBinding;
import com.example.voxer.models.ChatMessage;
import com.example.voxer.network.TTSServer;

import java.util.HashMap;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private Bitmap receiverImageProfile;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public void setReceiverImageProfile(Bitmap bitmap){
        receiverImageProfile = bitmap;
    }

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverImageProfile, String senderId) {
        this.chatMessages = chatMessages;
        this.receiverImageProfile = receiverImageProfile;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT){
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }else {
            return new ReceivedMessageViewHolder(
                    ItemContainerRecievedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        }else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiverImageProfile);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding){
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
            binding.textMessage.setOnClickListener(this);
        }

        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == binding.textMessage.getId()) {
                String messageText = binding.textMessage.getText().toString();
                // do something with the message text, like open a new activity or show a dialog
                TTSServer tts = new TTSServer(itemView.getContext());
                tts.ttsServer(messageText,new ChatActivity.ChatBotCallback() {
                    @Override
                    public void onSuccess(String result) {
                        // update UI with result
                        //Toast.makeText(chatActivity, result, Toast.LENGTH_SHORT).show();
                        tts.playAudio(result);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // handle exception
                    }
                });
            }
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemContainerRecievedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerRecievedMessageBinding itemContainerRecievedMessageBinding){
            super(itemContainerRecievedMessageBinding.getRoot());
            binding = itemContainerRecievedMessageBinding;
            binding.textMessage.setOnClickListener(this);
        }

        void setData(ChatMessage chatMessage, Bitmap receiverImageProfile){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            if(receiverImageProfile != null){
                binding.imageProfile.setImageBitmap(receiverImageProfile);
            }

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == binding.textMessage.getId()) {
                String messageText = binding.textMessage.getText().toString();
                // do something with the message text, like open a new activity or show a dialog
                TTSServer tts = new TTSServer(itemView.getContext());
                tts.ttsServer(messageText,new ChatActivity.ChatBotCallback() {
                    @Override
                    public void onSuccess(String result) {
                        // update UI with result
                        //Toast.makeText(chatActivity, result, Toast.LENGTH_SHORT).show();
                        tts.playAudio(result);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // handle exception
                    }
                });
            }
        }
    }

}
