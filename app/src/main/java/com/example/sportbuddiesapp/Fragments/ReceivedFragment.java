package com.example.sportbuddiesapp.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sportbuddiesapp.Adapter.UserAdapter;
import com.example.sportbuddiesapp.Adapter.UserAdapterForReceivedInvitations;
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

import java.util.ArrayList;
import java.util.List;


public class ReceivedFragment extends Fragment {

    List<User> usersList;
    RecyclerView recyclerView;
    UserAdapterForReceivedInvitations adapter;

    FirebaseAuth firebaseAuth;
    String useremail;

    // This is the fragment that deals with the received invitations to be sports buddies
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_received, container, false);

        usersList = new ArrayList<>();
        adapter = new UserAdapterForReceivedInvitations(getContext(), usersList);
        recyclerView = view.findViewById(R.id.receivedInvitations);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 = firebaseAuth.getCurrentUser();
        useremail = user1.getEmail();

        // Add users based on the list of those that have sent invitation to the current user
        Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Invitation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Invitation invitation = snapshot.getValue(Invitation.class);
                        if(invitation.getReceiver().getUseremail().equals(useremail) && invitation.getStatus().equals("Pending")){
                            usersList.add(invitation.getSender());
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
}