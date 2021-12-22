package com.example.sportbuddiesapp;

import com.example.sportbuddiesapp.ObjectClass.Booking;
import com.example.sportbuddiesapp.ObjectClass.Chat;
import com.example.sportbuddiesapp.ObjectClass.Invitation;
import com.example.sportbuddiesapp.ObjectClass.Review;
import com.example.sportbuddiesapp.ObjectClass.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBManager {

    private DatabaseReference databaseReference;

    public DBManager()
    {
        // In the get instance i specified the url of the database  if not the connection cannot be established
        databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

    }

    public Task<Void> addbooking(Booking booking)
    {
        return databaseReference.child("Booking").push().setValue(booking);
    }

    public Task<Void> adduser(User user)
    {
        return databaseReference.child("User").push().setValue(user);
    }

    public Task<Void> addreview(Review review)
    {
        return databaseReference.child("Review").push().setValue(review);
    }

    public Task<Void> addchat(Chat chat)
    {
        return databaseReference.child("Chat").push().setValue(chat);
    }

    public Task<Void> addInvitation(Invitation invitation)
    {
        return databaseReference.child("Invitation").push().setValue(invitation);
    }


    public Task<Void> removebooking(String key)
    {
        return databaseReference.child("Booking").child(key).removeValue();
    }

}
