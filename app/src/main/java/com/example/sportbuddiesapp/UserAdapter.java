package com.example.sportbuddiesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private Context c;
    private List<User> usersList;

    public UserAdapter(Context c, List<User> usersList) {
        this.c = c;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        View view = layoutInflater.inflate(R.layout.user_row_layout, parent , false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.userUsername.setText(user.getUsername());
        holder.userTelegramHandle.setText(user.getTelegramhandle());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView userUsername, userTelegramHandle;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userUsername = itemView.findViewById(R.id.userUsername);
            userTelegramHandle = itemView.findViewById(R.id.userTelegramHandle);
        }
    }
}
