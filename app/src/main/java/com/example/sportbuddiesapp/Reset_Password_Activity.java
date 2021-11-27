package com.example.sportbuddiesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset_Password_Activity extends AppCompatActivity {
    private EditText recoveryEmail;
    private Button resetPasswordBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Implement a send to email or send to hp number function to reset password
        setContentView(R.layout.reset_password_layout);

        recoveryEmail = (EditText)findViewById(R.id.resetPaswordEmail);
        resetPasswordBtn = (Button) findViewById(R.id.btnPasswordReset);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = recoveryEmail.getText().toString();
                if (userEmail.equals("")){
                    Toast.makeText(Reset_Password_Activity.this, "Please Enter registered recovery email", Toast.LENGTH_SHORT).show();
                }else {
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Reset_Password_Activity.this, "Email has been sent", Toast.LENGTH_SHORT).show();
                                finish();
                                openLoginActivity();
                            }else{
                                Toast.makeText(Reset_Password_Activity.this, "Error in sending email. Please enter your registered email.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });


    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, Login_Activity.class);
        startActivity(intent);

    }
}
