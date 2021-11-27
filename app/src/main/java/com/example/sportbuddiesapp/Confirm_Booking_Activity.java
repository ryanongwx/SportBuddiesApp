package com.example.sportbuddiesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Confirm_Booking_Activity extends AppCompatActivity {

    private Button confirmBooking;
    private EditText numberOfPlayers;
    private TextView dateTimeText;
    private TextView specificLocation;
    private List<Booking> bookingList;
    private Integer totalrating = 0;
    private Integer count = 0;

    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.confirmtimeslotbooking);

        numberOfPlayers = findViewById(R.id.playerNumber);


        confirmBooking = findViewById(R.id.confirmBookingBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 = firebaseAuth.getCurrentUser();
        String username = user1.getEmail();

        // Creation of the get intent object
        // Must be done in the onCreate if not it will not work
        Intent intent = getIntent();

        // Retrieve the data from the previous button click cardview
        String time = intent.getStringExtra("time");
        String date = intent.getStringExtra("date");
        String location = intent.getStringExtra("location");


        dateTimeText = findViewById(R.id.dateTimeText);
        dateTimeText.setText(date + " " +time);
        specificLocation = findViewById(R.id.specificLocation);
        specificLocation.setText(location);

        DBManager db = new DBManager();

        // Set onclick listener for the confirm booking button
        confirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add the number of players, location, timeslot and profile to the database
                // location and timeslot can be obtained from intent information
                // how to access profile username/email

                // TODO Get the other players in the same time slot in the form oof a list (Must access the database)
                // TODO Get the number of other players (Must access the database)
                // TODO Get the average rating in the form of integer (Must access the database)
                String playerNumberText = numberOfPlayers.getText().toString();
                Integer playerNumber = Integer.parseInt(playerNumberText);

                Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Filter the booking list by all the bookings made by the user in this user session
                                // So it displays only the booking made by this user and not anu other user
                                if(snapshot.child("useremail").getValue().toString().equals(username))
                                {
                                    User user = snapshot.getValue(User.class);
                                    Booking bookings = new Booking(user, time, date, location, playerNumber, "Confirmed");
                                    db.addbooking(bookings).addOnSuccessListener(suc ->
                                    {
                                        Toast.makeText(Confirm_Booking_Activity.this, "Booking Successful", Toast.LENGTH_SHORT).show();
                                    });
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
