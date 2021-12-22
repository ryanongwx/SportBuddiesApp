package com.example.sportbuddiesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportbuddiesapp.Adapter.MessageAdapter;
import com.example.sportbuddiesapp.ObjectClass.Chat;
import com.example.sportbuddiesapp.ObjectClass.Invitation;
import com.example.sportbuddiesapp.ObjectClass.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    ImageView chatProfileImage;
    ImageButton backArrowImage;
    ImageButton sendMessage;
    EditText chatBox;

    TextView chatUsername;

    Intent intent;

    DBManager db = new DBManager();

    private FirebaseAuth firebaseAuth;

    User sender;
    User receiver;

    MessageAdapter messageAdapter;
    List<Chat> chatList;

    RecyclerView recyclerView;
    TextView chatWarning;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        intent = getIntent();
        String receiverusername = intent.getStringExtra("username");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String senderemail = user.getEmail();

        chatProfileImage = findViewById(R.id.chatProfileImage);
        chatUsername = findViewById(R.id.chatUsername);
        chatUsername.setText(receiverusername);

        // This is setting up the top left back button which leads back to the chat fragment
        backArrowImage = findViewById(R.id.backArrowImagebutton);
        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, Buddies_Network_Activity.class);
                startActivity(intent);
            }
        });

        sendMessage = findViewById(R.id.sendButton);
        chatBox = findViewById(R.id.chatBox);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatBox.getText().toString();
                sendChatMessage(sender, receiver, message);
            }
        });

        // Setting up the chat messages recyclerview
        recyclerView = findViewById(R.id.chatMessages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        chatWarning = findViewById(R.id.chatWarning);


        // Search the database for the sender and receiver's username and obtain the user
        Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Filter the booking list by all the bookings made by the user in this user session
                        // So it displays only the booking made by this user and not anu other user
                        if(snapshot.child("username").getValue().toString().equals(receiverusername))
                        {
                            receiver = snapshot.getValue(User.class);
                        }
                        if(snapshot.child("useremail").getValue().toString().equals(senderemail))
                        {
                            sender = snapshot.getValue(User.class);
                        }
                    }
                }
                checkBuddiesStatus();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void sendChatMessage(User sender, User receiver, String message){
        // This function logs the chat as a chat object into the database

        // Adding the chat to the database
        Chat chat = new Chat(sender, receiver, message, false);
        db.addchat(chat);
        checkBuddiesStatus();

    }

    private void readMessages(User sender, User receiver){
        chatList = new ArrayList<>();
        Query databaseReference2 = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Chat");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        if(chat.getSender().getUsername().equals(sender.getUsername()) && chat.getReceiver().getUsername().equals(receiver.getUsername())
                                || chat.getReceiver().getUsername().equals(sender.getUsername()) && chat.getSender().getUsername().equals(receiver.getUsername()))
                        {
                            chatList.add(chat);
                        }
                        if(chat.getReceiver().getUsername().equals(sender.getUsername()) && chat.getSender().getUsername().equals(receiver.getUsername()))
                        {
                            String chatId = snapshot.getKey();

                            // This is required to use updatechildren() for status to be updated to be accepted
                            Map<String, Object> postValues = new HashMap<String,Object>();
                            postValues.put("isseen", true);
                            DatabaseReference chatReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Chat");
                            chatReference.child(chatId).updateChildren(postValues);
                        }
                        messageAdapter = new MessageAdapter(MessageActivity.this, chatList);
                        recyclerView.setAdapter(messageAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MessageActivity.this, "Failed Datasbase Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkBuddiesStatus(){
        // Check if the users are Sport Buddies and prints the chat if they are already Sports Buddies
        // This is such that the users will not be able to send texts to one another if they are not sport buddies
        // Uses the chatWarning chatview to appear if the users are not sports buddies yet
        Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Invitation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Invitation invitation = snapshot.getValue(Invitation.class);
                        if(invitation.getReceiver().getUseremail().equals(receiver.getUseremail()) && invitation.getSender().getUseremail().equals(sender.getUseremail()) && invitation.getStatus().equals("Accepted")||
                                invitation.getSender().getUseremail().equals(receiver.getUseremail()) && invitation.getReceiver().getUseremail().equals(sender.getUseremail()) && invitation.getStatus().equals("Accepted"))
                        {
                            readMessages(sender, receiver);
                            chatWarning.setVisibility(View.GONE);

                        }else{
                            chatWarning.setText("You are not Sports Buddies with this user yet.");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
