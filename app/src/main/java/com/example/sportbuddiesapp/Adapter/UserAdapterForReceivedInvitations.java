package com.example.sportbuddiesapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdapterForReceivedInvitations extends RecyclerView.Adapter<UserAdapterForReceivedInvitations.UserViewHolder>{

    private Context c;
    private List<User> usersList;
    FirebaseAuth firebaseAuth;
    String useremail;
    DBManager db;
    User sender;

    public UserAdapterForReceivedInvitations(Context c, List<User> usersList) {
        this.c = c;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserAdapterForReceivedInvitations.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        View view = layoutInflater.inflate(R.layout.user_row_layout_for_received_invitations, parent , false);



        db = new DBManager();

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapterForReceivedInvitations.UserViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.userUsername.setText(user.getUsername());
        holder.userTelegramHandle.setText(user.getTelegramhandle());

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 = firebaseAuth.getCurrentUser();
        useremail = user1.getEmail();

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

        // Setting onclicklistener for clicking on the accept button
        // Updates the status of the invitation entry in the database to "accepted"
        holder.ReceivedAcceptButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Invitation");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Invitation invitation = snapshot.getValue(Invitation.class);
                                String invitationId = snapshot.getKey();

                                // This is required to use updatechildren() for status to be updated to be accepted
                                Map<String, Object> postValues = new HashMap<String,Object>();
                                postValues.put("status", "Accepted");

                                if(invitation.getSender().getUseremail().equals(user.getUseremail()) && invitation.getReceiver().getUseremail().equals(useremail)){
                                    DatabaseReference invitationReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Invitation");
                                    // This sets the invitation status of the specific invitation to accepted
                                    invitationReference.child(invitationId).updateChildren(postValues);
                                    // Add the opposite log into the database such that it is easier to search for the user
                                    Invitation mirrorinvitation = new Invitation(invitation.getReceiver(), invitation.getSender(), "Accepted");
                                    db.addInvitation(mirrorinvitation);
                                    Toast.makeText(c, "Invitation Accepted. You can now chat with this user.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });


        // Setting onclicklistener for clicking on the reject button
        // Updates the status of the invitation entry in the database to "rejected"
        holder.ReceivedRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Invitation");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Invitation invitation = snapshot.getValue(Invitation.class);
                                String invitationId = snapshot.getKey();

                                // This is required to use updatechildren() for status to be updated to be rejected
                                Map<String, Object> postValues = new HashMap<String,Object>();
                                postValues.put("status", "Rejected");

                                if(invitation.getSender().getUseremail().equals(user.getUseremail()) && invitation.getReceiver().getUseremail().equals(useremail)){
                                    DatabaseReference invitationReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Invitation");
                                    invitationReference.child(invitationId).updateChildren(postValues);
                                    Toast.makeText(c, "Invitation Rejected.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        // This sets the onclick listener for the add sport buddies button
        // What it does is it logs the invitation into the database
//        holder.userAddBuddiesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User");
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.exists()) {
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                User user2 = snapshot.getValue(User.class);
//                                if(user2.getUseremail().equals(useremail)){
//                                    sender = user2;
//                                }
//                            }
//                        }
//                        Invitation invitation = new Invitation(sender, user, "pending");
//                        db.addInvitation(invitation);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//
//                Toast.makeText(c, "Invitation Sent", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView userUsername, userTelegramHandle;
        Button ReceivedAcceptButton, ReceivedRejectButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // This is the place to identify and link all the view elements in the layout
            userUsername = itemView.findViewById(R.id.receivedUserUsername);
            userTelegramHandle = itemView.findViewById(R.id.receivedUserTelegramHandle);
            ReceivedAcceptButton = itemView.findViewById(R.id.receivedAcceptButton);
            ReceivedRejectButton = itemView.findViewById(R.id.receivedRejectButton);
        }
    }
}
