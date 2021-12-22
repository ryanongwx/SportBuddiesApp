package com.example.sportbuddiesapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sportbuddiesapp.MessageActivity;
import com.example.sportbuddiesapp.ObjectClass.Chat;
import com.example.sportbuddiesapp.R;
import com.example.sportbuddiesapp.ObjectClass.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private Context c;
    private List<Chat> chatsList;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    FirebaseUser firebaseUser;


    public MessageAdapter(Context c, List<Chat> chatsList) {
        this.c = c;
        this.chatsList = chatsList;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            LayoutInflater layoutInflater = LayoutInflater.from(c);
            View view = layoutInflater.inflate(R.layout.chat_item_right, parent, false);
            return new MessageViewHolder(view);
        }else{
            LayoutInflater layoutInflater = LayoutInflater.from(c);
            View view = layoutInflater.inflate(R.layout.chat_item_left, parent, false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {

        Chat chat = chatsList.get(position);

        holder.chatMessage.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView chatMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            chatMessage = itemView.findViewById(R.id.chat_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String senderemail = firebaseUser.getEmail();
        if (chatsList.get(position).getSender().getUseremail().equals(senderemail)){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
