package com.example.sportbuddiesapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sportbuddiesapp.DBManager;
import com.example.sportbuddiesapp.MessageActivity;
import com.example.sportbuddiesapp.ObjectClass.Invitation;
import com.example.sportbuddiesapp.R;
import com.example.sportbuddiesapp.ObjectClass.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private Context c;
    private List<User> usersList;
    FirebaseAuth firebaseAuth;
    String useremail;
    DBManager db;
    User sender;

    public UserAdapter(Context c, List<User> usersList) {
        this.c = c;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        View view = layoutInflater.inflate(R.layout.user_row_layout, parent , false);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 = firebaseAuth.getCurrentUser();
        useremail = user1.getEmail();

        db = new DBManager();

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.userUsername.setText(user.getUsername());
        holder.userTelegramHandle.setText(user.getTelegramhandle());

        // This is the onclicklistener for the entire row of user profile in order to navigate to the chat
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, MessageActivity.class);
                intent.putExtra("username", user.getUsername());
                c.startActivity(intent);

            }
        });

        // This sets the onclick listener for the add sport buddies button
        // What it does is it logs the invitation into the database
        holder.userAddBuddiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user2 = snapshot.getValue(User.class);
                                if(user2.getUseremail().equals(useremail)){
                                    sender = user2;
                                }
                            }
                        }


                        // Before i add the invitation log, I must make sure that there are no previous invitations
                        Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Invitation");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Invitation invitation = snapshot.getValue(Invitation.class);

                                        if(invitation.getSender().getUseremail().equals(useremail) && invitation.getReceiver().getUseremail().equals(user.getUseremail())
                                                && invitation.getStatus().equals("Accepted") ||
                                                invitation.getReceiver().getUseremail().equals(useremail) && invitation.getSender().getUseremail().equals(user.getUseremail())
                                                        && invitation.getStatus().equals("Accepted")){
                                            Toast.makeText(c, "You are already Sports Buddies with this user.", Toast.LENGTH_SHORT).show();
                                        }

                                        else if(invitation.getSender().getUseremail().equals(useremail) && invitation.getReceiver().getUseremail().equals(user.getUseremail())
                                                && invitation.getStatus().equals("Pending") ||
                                                invitation.getReceiver().getUseremail().equals(useremail) && invitation.getSender().getUseremail().equals(user.getUseremail())
                                                        && invitation.getStatus().equals("Pending")){
                                            Toast.makeText(c, "Invitation has already been sent", Toast.LENGTH_SHORT).show();
                                        }

                                        else if(invitation.getSender().getUseremail().equals(useremail) && invitation.getReceiver().getUseremail().equals(user.getUseremail())
                                                && invitation.getStatus().equals("Rejected") ||
                                                invitation.getReceiver().getUseremail().equals(useremail) && invitation.getSender().getUseremail().equals(user.getUseremail())
                                                        && invitation.getStatus().equals("Rejected")){
                                            invitation.setStatus("Pending");
                                            Toast.makeText(c, "Invitation Sent", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            invitation = new Invitation(sender, user, "Pending");
                                            db.addInvitation(invitation);
                                            Toast.makeText(c, "Invitation Sent", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView userUsername, userTelegramHandle;
        ImageView userAddBuddiesButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // This is the place to identify and link all the view elements in the layout
            userUsername = itemView.findViewById(R.id.sentUserUsername);
            userTelegramHandle = itemView.findViewById(R.id.sentUserTelegramHandle);
            userAddBuddiesButton = itemView.findViewById(R.id.addBuddiesButton);
        }
    }
}
