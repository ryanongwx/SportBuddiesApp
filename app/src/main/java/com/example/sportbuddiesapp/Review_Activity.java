package com.example.sportbuddiesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sportbuddiesapp.ObjectClass.Review;
import com.example.sportbuddiesapp.ObjectClass.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Review_Activity extends AppCompatActivity {

    TextView reviewLocation;
    RatingBar reviewRatingBar;
    Button reviewSubmitBtn;
    Integer ratings= 0;
    private FirebaseAuth firebaseAuth;
    private StringBuilder hasreviewed;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 = firebaseAuth.getCurrentUser();
        String useremail = user1.getEmail();

        setContentView(R.layout.review_layout);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        reviewLocation = findViewById(R.id.reviewLocation);
        reviewLocation.setText(location);

        reviewRatingBar = findViewById(R.id.reviewRatingBar);
        reviewRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratings += (int) rating;
            }
        });

        DBManager db = new DBManager();
        reviewSubmitBtn = findViewById(R.id.reviewSubmitBtn);

//         Accessing database tocheck whether review has been done
        Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Review");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    hasreviewed = new StringBuilder();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("user").child("useremail").getValue().toString().equals(useremail))
                        {
                            hasreviewed.append("True");
                        }
                    }
                    hasreviewed.append("False");
                }
                if(hasreviewed.toString().equals("False")){
                    reviewSubmitBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Register Reviews to Database
                            if (ratings != 0)
                            {

                                // Add review made to the database
                                Review review = new Review(location, ratings, user);
                                db.addreview(review);

                                Toast.makeText(Review_Activity.this, "Rating Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Review_Activity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(Review_Activity.this, "Please select a rating for this location.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else{
                    reviewSubmitBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(Review_Activity.this, "You have already submitted your ratings for this location.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Accessing database to obtain the user object of this user
        Query query = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.child("useremail").getValue().toString().equals(useremail))
                        {
                            user = snapshot.getValue(User.class);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
