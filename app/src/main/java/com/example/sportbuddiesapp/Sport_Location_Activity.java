package com.example.sportbuddiesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class Sport_Location_Activity<Textview> extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    FirebaseAuth firebaseAuth;

    private String shortDateString;

    private Button dateSetBtn;
    private ConstraintLayout timeslotlist;

    private ConstraintLayout line0600;
    private TextView text0600;
    private TextView players0600;
    private Button btn0600;

    private ConstraintLayout line0700;
    private TextView text0700;
    private TextView players0700;
    private Button btn0700;

    private ConstraintLayout line0800;
    private TextView text0800;
    private TextView players0800;
    private Button btn0800;

    private ConstraintLayout line0900;
    private TextView text0900;
    private TextView players0900;
    private Button btn0900;

    private ConstraintLayout line1000;
    private TextView text1000;
    private TextView players1000;
    private Button btn1000;

    private ConstraintLayout line1100;
    private TextView text1100;
    private TextView players1100;
    private Button btn1100;

    private ConstraintLayout line1200;
    private TextView text1200;
    private TextView players1200;
    private Button btn1200;

    private ConstraintLayout line1300;
    private TextView text1300;
    private TextView players1300;
    private Button btn1300;

    private ConstraintLayout line1400;
    private TextView text1400;
    private TextView players1400;
    private Button btn1400;

    private ConstraintLayout line1500;
    private TextView text1500;
    private TextView players1500;
    private Button btn1500;

    private ConstraintLayout line1600;
    private TextView text1600;
    private TextView players1600;
    private Button btn1600;

    private ConstraintLayout line1700;
    private TextView text1700;
    private TextView players1700;
    private Button btn1700;

    private ConstraintLayout line1800;
    private TextView text1800;
    private TextView players1800;
    private Button btn1800;

    private ConstraintLayout line1900;
    private TextView text1900;
    private TextView players1900;
    private Button btn1900;

    private ConstraintLayout line2000;
    private TextView text2000;
    private TextView players2000;
    private Button btn2000;

    private ConstraintLayout line2100;
    private TextView text2100;
    private TextView players2100;
    private Button btn2100;

    private ConstraintLayout line2200;
    private TextView text2200;
    private TextView players2200;
    private Button btn2200;

    private TextView dateSetText;


    private String location;
    private StringBuilder time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_location);

        firebaseAuth = FirebaseAuth.getInstance();

        TextView locationName = findViewById(R.id.specificLocation);

        dateSetText = findViewById(R.id.dateSelected);

        // Implement the button to open the date picker in calendar
        dateSetBtn = findViewById(R.id.dateSetBtn);
        dateSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new com.example.sportbuddiesapp.DatePicker();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


        // Creation of the get intent object
        // Must be done in the onCreate if not it will not work
        Intent intent = getIntent();

        // Retrieve the data from the previous button click cardview
        location = intent.getStringExtra("title");

        locationName.setText(location);

        timeslotlist = findViewById(R.id.timeslotlist);

        line0600 = timeslotlist.findViewById(R.id.line0600);
        text0600 = line0600.findViewById(R.id.text0600);
        text0600.setText("0600 - 0700");
        players0600 = line0600.findViewById(R.id.players0600);
        btn0600 = line0600.findViewById(R.id.btn0600);
        btn0600.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("0600 - 0700", shortDateString, location);
            }
        });

        line0700 = timeslotlist.findViewById(R.id.line0700);
        text0700 = line0700.findViewById(R.id.text0700);
        text0700.setText("0700 - 0800");
        players0700 = line0700.findViewById(R.id.players0700);
        btn0700 = line0700.findViewById(R.id.btn0700);
        btn0700.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("0700 - 0800", shortDateString, location);
            }
        });

        line0800 = timeslotlist.findViewById(R.id.line0800);
        text0800 = line0800.findViewById(R.id.text0800);
        text0800.setText("0800 - 0900");
        players0800 = line0800.findViewById(R.id.players0800);
        btn0800 = line0800.findViewById(R.id.btn0800);
        btn0800.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("0800 - 0900", shortDateString, location);
            }
        });

        line0900 = timeslotlist.findViewById(R.id.line0900);
        text0900 = line0900.findViewById(R.id.text0900);
        text0900.setText("0900 - 1000");
        players0900 = line0900.findViewById(R.id.players0900);
        btn0900 = line0900.findViewById(R.id.btn0900);
        btn0900.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("0900 - 1000", shortDateString, location);
            }
        });

        line1000 = timeslotlist.findViewById(R.id.line1000);
        text1000 = line1000.findViewById(R.id.text1000);
        text1000.setText("1000 - 1100");
        players1000 = line1000.findViewById(R.id.players1000);
        btn1000 = line1000.findViewById(R.id.btn1000);
        btn1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("1000 - 1100", shortDateString, location);
            }
        });

        line1100 = timeslotlist.findViewById(R.id.line1100);
        text1100 = line1100.findViewById(R.id.text1100);
        text1100.setText("1100 - 1200");
        players1100 = line1100.findViewById(R.id.players1100);
        btn1100 = line1100.findViewById(R.id.btn1100);
        btn1100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("1100 - 1200", shortDateString, location);
            }
        });

        line1200 = timeslotlist.findViewById(R.id.line1200);
        text1200 = line1200.findViewById(R.id.text1200);
        text1200.setText("1200 - 1300");
        players1200 = line1200.findViewById(R.id.players1200);
        btn1200 = line1200.findViewById(R.id.btn1200);
        btn1200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("1200 - 1300", shortDateString, location);
            }
        });

        line1300 = timeslotlist.findViewById(R.id.line1300);
        text1300 = line1300.findViewById(R.id.text1300);
        text1300.setText("1300 - 1400");
        players1300 = line1300.findViewById(R.id.players1300);
        btn1300 = line1300.findViewById(R.id.btn1300);
        btn1300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("1300 - 1400", shortDateString, location);
            }
        });

        line1400 = timeslotlist.findViewById(R.id.line1400);
        text1400 = line1400.findViewById(R.id.text1400);
        text1400.setText("1400 - 1500");
        players1400 = line1400.findViewById(R.id.players1400);
        btn1400 = line1400.findViewById(R.id.btn1400);
        btn1400.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("1400 - 1500", shortDateString, location);
            }
        });

        line1500 = timeslotlist.findViewById(R.id.line1500);
        text1500 = line1500.findViewById(R.id.text1500);
        text1500.setText("1500 - 1600");
        players1500 = line1500.findViewById(R.id.players1500);
        btn1500 = line1500.findViewById(R.id.btn1500);
        btn1500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("1500 - 1600", shortDateString, location);
            }
        });

        line1600 = timeslotlist.findViewById(R.id.line1600);
        text1600 = line1600.findViewById(R.id.text1600);
        text1600.setText("1600 - 1700");
        players1600 = line1600.findViewById(R.id.players1600);
        btn1600 = line1600.findViewById(R.id.btn1600);
        btn1600.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("1600 - 1700", shortDateString, location);
            }
        });

        line1700 = timeslotlist.findViewById(R.id.line1700);
        text1700 = line1700.findViewById(R.id.text1700);
        text1700.setText("1700 - 1800");
        players1700 = line1700.findViewById(R.id.players1700);
        btn1700 = line1700.findViewById(R.id.btn1700);
        btn1700.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("1700 - 1800", shortDateString, location);
            }
        });

        line1800 = timeslotlist.findViewById(R.id.line1800);
        text1800 = line1800.findViewById(R.id.text1800);
        text1800.setText("1800 - 1900");
        players1800 = line1800.findViewById(R.id.players1800);
        btn1800 = line1800.findViewById(R.id.btn1800);
        btn1800.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("1800 - 1900", shortDateString, location);
            }
        });

        line1900 = timeslotlist.findViewById(R.id.line1900);
        text1900 = line1900.findViewById(R.id.text1900);
        text1900.setText("1900 - 2000");
        players1900 = line1900.findViewById(R.id.players1900);
        btn1900 = line1900.findViewById(R.id.btn1900);
        btn1900.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("1900 - 2000", shortDateString, location);
            }
        });

        line2000 = timeslotlist.findViewById(R.id.line2000);
        text2000 = line2000.findViewById(R.id.text2000);
        text2000.setText("2000 - 2100");
        players2000 = line2000.findViewById(R.id.players2000);
        btn2000 = line2000.findViewById(R.id.btn2000);
        btn2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("2000 - 2100", shortDateString, location);
            }
        });

        line2100 = timeslotlist.findViewById(R.id.line2100);
        text2100 = line2100.findViewById(R.id.text2100);
        text2100.setText("2100 - 2200");
        players2100 = line2100.findViewById(R.id.players2100);
        btn2100 = line2100.findViewById(R.id.btn2100);
        btn2100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity("2100 - 2200", shortDateString, location);
            }
        });




    }

    private void openConfirmActivity(String time, String date, String location) {
        if (date!=null) {
            Intent intent = new Intent(this, Confirm_Booking_Activity.class);
            intent.putExtra("date", date);
            intent.putExtra("time", time);
            intent.putExtra("location", location);
            startActivity(intent);
        } else{
            Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show();
        }
    }

    // retrieve the date being picked
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // Converted to the locale of the phone
        shortDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());
        String longDateString = DateFormat.getDateInstance().format(c.getTime());
        dateSetText.setText("Date Selected: " + longDateString);

        Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Booking");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer playerNumber0600 = 0;
                Integer playerNumber0700 = 0;
                Integer playerNumber0800 = 0;
                Integer playerNumber0900 = 0;
                Integer playerNumber1000 = 0;
                Integer playerNumber1100 = 0;
                Integer playerNumber1200 = 0;
                Integer playerNumber1300 = 0;
                Integer playerNumber1400 = 0;
                Integer playerNumber1500 = 0;
                Integer playerNumber1600 = 0;
                Integer playerNumber1700 = 0;
                Integer playerNumber1800 = 0;
                Integer playerNumber1900 = 0;
                Integer playerNumber2000 = 0;
                Integer playerNumber2100 = 0;

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("0600 - 0700"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber0600 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("0700 - 0800"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber0700 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("0800 - 0900"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber0800 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("0900 - 1000"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber0900 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("1000 - 1100"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber1000 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("1100 - 1200"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber1100 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("1200 - 1300"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber1200 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("1300 - 1400"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber1300 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("1400 - 1500"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber1400 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("1500 - 1600"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber1500 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("1600 - 1700"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber1600 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("1700 - 1800"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber1700 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("1800 - 1900"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber1800 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("1900 - 2000"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber1900 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("2000 - 2100"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber2000 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }

                        if(snapshot.child("location").getValue().toString().equals(location) && snapshot.child("date").getValue().toString().equals(shortDateString)  && snapshot.child("timeslot").getValue().toString().equals("2100 - 2200"))
                        {
                            if(!snapshot.child("playernumber").getValue().toString().equals("null"))
                            {
                                playerNumber2100 += Integer.parseInt(snapshot.child("playernumber").getValue().toString());
                            }
                        }
                    }
                    players0600.setText(String.format(String.valueOf(playerNumber0600)) + "/10");
                    players0700.setText(String.format(String.valueOf(playerNumber0700)) + "/10");
                    players0800.setText(String.format(String.valueOf(playerNumber0800)) + "/10");
                    players0900.setText(String.format(String.valueOf(playerNumber0900)) + "/10");
                    players1000.setText(String.format(String.valueOf(playerNumber1000)) + "/10");
                    players1100.setText(String.format(String.valueOf(playerNumber1100)) + "/10");
                    players1200.setText(String.format(String.valueOf(playerNumber1200)) + "/10");
                    players1300.setText(String.format(String.valueOf(playerNumber1300)) + "/10");
                    players1400.setText(String.format(String.valueOf(playerNumber1400)) + "/10");
                    players1500.setText(String.format(String.valueOf(playerNumber1500)) + "/10");
                    players1600.setText(String.format(String.valueOf(playerNumber1600)) + "/10");
                    players1700.setText(String.format(String.valueOf(playerNumber1700)) + "/10");
                    players1800.setText(String.format(String.valueOf(playerNumber1800)) + "/10");
                    players1900.setText(String.format(String.valueOf(playerNumber1900)) + "/10");
                    players2000.setText(String.format(String.valueOf(playerNumber2000)) + "/10");
                    players2100.setText(String.format(String.valueOf(playerNumber2100)) + "/10");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Sport_Location_Activity.this, "Database Error", Toast.LENGTH_SHORT).show();
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