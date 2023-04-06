package com.example.voxer.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voxer.databinding.ItemContainerUserBinding;
import com.example.voxer.adapters.listeners.UserListener;
import com.example.voxer.models.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private final List<User> users;
    private final UserListener userListener;
    //This is the list of users that are available to chat with

    //This is the constructor class we have created for it
    public UsersAdapter(List<User> users, UserListener userListener) {

        this.users = users;
        this.userListener = userListener;
    }

    //These are the method implements
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
            return users.size();
    }

    //This is the class that is used to create the userview blobs for the chat application
    class UserViewHolder extends RecyclerView.ViewHolder{

        ItemContainerUserBinding binding;

        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }

        //We bind the respective values of each user to the TextViews in item_container_user.xml file

        void setUserData(User user){
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }
    }
    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
