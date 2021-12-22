package com.example.sportbuddiesapp.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sportbuddiesapp.Adapter.UserAdapter;
import com.example.sportbuddiesapp.ObjectClass.Chat;
import com.example.sportbuddiesapp.ObjectClass.User;
import com.example.sportbuddiesapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment {

    List<User> chatsList;
    RecyclerView recyclerView;
    EditText nameSearch;
    UserAdapter adapter;


    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String useremail = user.getEmail();

        chatsList = new ArrayList<>();
        adapter = new UserAdapter(getContext(), chatsList);
        recyclerView = view.findViewById(R.id.listofchats);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        nameSearch = view.findViewById(R.id.namesearch);
        nameSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase(Locale.ROOT));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Chat");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatsList.clear();
                if (dataSnapshot.exists() && nameSearch.getText().toString().equals("")){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chat chat = snapshot.getValue(Chat.class);

                        // This is to check that when the current user is the sender, retrieve who the receiver is from the database
                        // and add it to the list of chats
                        if(chat.getSender().getUseremail().equals(useremail)){
                            User user2 = chat.getReceiver();

                            // This condition is to prevent repeating of users such that there will not be more than one of each
                            // chat showing on the ChatFragment page

                            if(!(chatsList.size() == 0)) {
                                int count = 0;
                                for(int i = 0; i < chatsList.size(); i++) {
                                    if (chatsList.get(i).getUseremail().equals(user2.getUseremail())) {
                                        count += 1;
                                    }
                                }
                                if(count == 0){
                                    chatsList.add(user2);
                                }
                            }else{
                                chatsList.add(user2);
                            }
                        }

                        // This is to check that when the current user is the receiver, retrieve who the sender is from the database
                        // and add it to the list of chats
                        if(chat.getReceiver().getUseremail().equals(useremail)){
                            User user1 = chat.getSender();

                            // This condition is to prevent repeating of users such that there will not be more than one of each
                            // chat showing on the ChatFragment page
                            if(!(chatsList.size() == 0)) {
                                int count = 0;
                                for (int i = 0; i < chatsList.size(); i++) {
                                    if (chatsList.get(i).getUseremail().equals(user1.getUseremail())) {
                                        count += 1;
                                    }
                                }
                                if(count==0){
                                    chatsList.add(user1);
                                }
                            }else{
                                chatsList.add(user1);
                            }

                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }

    private void searchUsers(String s) {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Chat").orderByChild("sender/username")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if(!chat.getSender().getUseremail().equals(fuser.getEmail())){
                        chatsList.add(chat.getSender());
                    }
                }
                adapter = new UserAdapter(getContext(), chatsList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}