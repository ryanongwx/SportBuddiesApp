package com.example.sportbuddiesapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportbuddiesapp.DBManager;
import com.example.sportbuddiesapp.MessageActivity;
import com.example.sportbuddiesapp.ObjectClass.Invitation;
import com.example.sportbuddiesapp.ObjectClass.User;
import com.example.sportbuddiesapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapterForSentInvitations extends RecyclerView.Adapter<UserAdapterForSentInvitations.UserViewHolder>{

    private Context c;
    private List<User> usersList;
    FirebaseAuth firebaseAuth;
    String useremail;
    DBManager db;
    User sender;

    public UserAdapterForSentInvitations(Context c, List<User> usersList) {
        this.c = c;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserAdapterForSentInvitations.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        View view = layoutInflater.inflate(R.layout.user_row_layout_for_sent_invitations, parent , false);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 = firebaseAuth.getCurrentUser();
        useremail = user1.getEmail();

        db = new DBManager();

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapterForSentInvitations.UserViewHolder holder, int position) {
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

        // This sets the onclick listener for the accept sport buddies button which connects 2 parties
        Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Invitation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Invitation invitation = snapshot.getValue(Invitation.class);
                        if(invitation.getSender().getUseremail().equals(useremail) && invitation.getReceiver().getUseremail().equals(user.getUseremail())){
                            holder.invitationStatus.setText(invitation.getStatus());
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
    public int getItemCount() {
        return usersList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView userUsername, userTelegramHandle, invitationStatus;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // This is the place to identify and link all the view elements in the layout
            userUsername = itemView.findViewById(R.id.sentUserUsername);
            userTelegramHandle = itemView.findViewById(R.id.sentUserTelegramHandle);
            invitationStatus  = itemView.findViewById(R.id.invitationStatus);
        }
    }
}
