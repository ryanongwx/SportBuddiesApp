package com.example.sportbuddiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bookings_Activity extends AppCompatActivity implements New_Booking_Adapter.OnNoteListener {

    RecyclerView recyclerView;
    New_Booking_Adapter adapter;
    private FirebaseAuth firebaseAuth;
    List<Booking> bookingList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookings_layout);

        recyclerView = findViewById(R.id.bookingList);
        bookingList = new ArrayList<>();

        // Create an instance of the adapter
        adapter = new New_Booking_Adapter(this, bookingList, this);
        // Set the adapter to the listview
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String username = user.getEmail();

        Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Booking");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookingList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Filter the booking list by all the bookings made by the user in this user session
                        // So it displays only the booking made by this user and not anu other user
                        if(snapshot.child("user").child("useremail").getValue().toString().equals(username))
                        {
                            Booking booking = snapshot.getValue(Booking.class);
                            bookingList.add(booking);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//
////         If i want to set a listviewitem onclicklistener
//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // This sets an onclicklistener for the first element in the list
//                if (position==0){
//                    // Do something for the first booking in the list
//                    // I want this to open an individual booking item and put all the details into the next activity
//                    openSpecificBookingActivity(mSportLocation[position], mDate[position], mTime[position], mCurrentPlayerNumber[position], mBookingStatus[position]);
//                    //TODO Must add users
//                }
//            }
//        });

    }



    private void openSpecificBookingActivity(String location, String date, String time, Integer players, String status) {
        Intent intent = new Intent(this, Specific_Booking_Activity.class);
        intent.putExtra("location", location);
        intent.putExtra("date", date);
        intent.putExtra("time", time);
        intent.putExtra("players", players);
        intent.putExtra("status", status);
        startActivity(intent);

    }

    @Override
    public void onNoteClick(int position) {
        openSpecificBookingActivity(
                bookingList.get(position).getLocation(),
                bookingList.get(position).getDate(),
                bookingList.get(position).getTimeslot(),
                bookingList.get(position).getPlayernumber(),
                bookingList.get(position).getBookingstatus());
    }

    // This will take care of creating the menu bar within the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // This handles the onclick events of the items in the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMenu:
                firebaseAuth.signOut();
                finish();
                openLoginActivity();
            case R.id.home:
                openMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, Login_Activity.class);
        startActivity(intent);

    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
