package com.example.sportbuddiesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Specific_Booking_Activity extends AppCompatActivity {

    private TextView specificDate;
    private TextView specificTime;
    private TextView specificPlayer;
    private TextView specificLocation;
    private  TextView specificBookingStatus;
    List<User> usersList;
    private Button cancelBtn;
    RecyclerView recyclerView;
    UserAdapter adapter;
    String currentbookingid;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.specific_booking_layout);

        DBManager db = new DBManager();

        specificDate = findViewById(R.id.specficDate);
        specificTime = findViewById(R.id.specficTime);
        specificPlayer = findViewById(R.id.specificPlayer);
        specificLocation = findViewById(R.id.specificLocation);
        specificBookingStatus = findViewById(R.id.specificBookingStatus);
        cancelBtn = findViewById(R.id.cancelBtn);
        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        // Retrieve the data from the previous button click cardview
        String time = intent.getStringExtra("time");
        String date = intent.getStringExtra("date");
        String players = intent.getStringExtra("players");
        String location = intent.getStringExtra("location");
        String status = intent.getStringExtra("status");


        usersList = new ArrayList<>();
        adapter = new UserAdapter(this, usersList);
        recyclerView = findViewById(R.id.usersList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Booking");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersList.clear();
                if (dataSnapshot.exists()) {
                    Integer playerNumber = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Filter the booking list by all the bookings made by the user in this user session
                        // So it displays only the booking made by this user and not anu other user
                        // This query is for summing up the toal number of players currently booked for the particular slot
                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("timeslot").getValue().toString().equals(time) && snapshot.child("date").getValue().toString().equals(date))
                        {
                            playerNumber += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            User user = snapshot.child("user").getValue(User.class);
                            usersList.add(user);
                            currentbookingid = snapshot.getKey().toString();
                        }

                    }
                    specificPlayer.setText(playerNumber.toString());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.removebooking(currentbookingid);
                Toast.makeText(Specific_Booking_Activity.this, "Booking Cancelled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Specific_Booking_Activity.this, Bookings_Activity.class);
                startActivity(intent);
            }
        });

        specificDate.setText(date);
        specificTime.setText(time);
        specificPlayer.setText(players);
        specificLocation.setText(location);
        specificBookingStatus.setText(status);

        // Create adapter for listview

    }

    private void openBookingsActivity() {
        Intent intent = new Intent(this, Bookings_Activity.class);
        startActivity(intent);

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
                break;
            case R.id.home:
                openMainActivity();
                break;
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
