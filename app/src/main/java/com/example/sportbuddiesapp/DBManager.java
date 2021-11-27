package com.example.sportbuddiesapp;

import androidx.room.Database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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


    public Task<Void> update(String key, HashMap<String, Object> hashmap)
    {
        return databaseReference.child(key).updateChildren(hashmap);
    }

    public Task<Void> removebooking(String key)
    {
        return databaseReference.child("Booking").child(key).removeValue();
    }

}
