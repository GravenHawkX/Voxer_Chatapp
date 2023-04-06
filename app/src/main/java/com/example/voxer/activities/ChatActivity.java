package com.example.voxer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.voxer.R;
import com.example.voxer.databinding.ActivityChatBinding;
import com.example.voxer.models.User;
import com.example.voxer.utilities.Constants;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private User receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
    }

    private void loadReceiverDetails(){
        receiverUser = (User)getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);

    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }
}