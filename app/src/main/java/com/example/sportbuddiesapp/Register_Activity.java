package com.example.sportbuddiesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class Register_Activity extends AppCompatActivity {

    private EditText registerName;
    private EditText registerPassword;
    private EditText registerConfirmPassword;
    private EditText registerEmail;
    private EditText registerTelegramHandle;
    private TextView registerLogin;
    private Button registerBtn;
    private FirebaseAuth firebaseAuth;

    Credentials credentials;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        registerName = findViewById(R.id.registerNameInput);
        registerPassword = findViewById(R.id.registerPasswordInput);
        registerConfirmPassword = findViewById(R.id.registerConfirmPasswordInput);
        registerBtn = findViewById(R.id.registerBtn);
        registerEmail = findViewById(R.id.registerEmail);
        registerLogin = findViewById(R.id.registerLogin);
        registerTelegramHandle = findViewById(R.id.registerTelegramHandle);

        credentials = new Credentials();
        firebaseAuth = FirebaseAuth.getInstance();
        // This creates a database named CredentialsDB which is saved on the phone
        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);

        // The editor will allow us to make changes to the DB which we previously created
        sharedPreferencesEditor = sharedPreferences.edit();

        // Set onClickListener for the already registered text
        registerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        DBManager db = new DBManager();

        if(sharedPreferences != null) {

            Map<String, ?> preferencesMap = sharedPreferences.getAll();
            if (preferencesMap.size() != 0) {
                credentials.loadCredentials(preferencesMap);
            }
        }

            registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerNameText = registerName.getText().toString();
                String registerEmailText = registerEmail.getText().toString();
                String registerPasswordText = registerPassword.getText().toString();
                String registerConfirmPasswordText = registerConfirmPassword.getText().toString();
                String registerTelegramHandleText = registerTelegramHandle.getText().toString();

                if(registerNameText.isEmpty() || registerEmailText.isEmpty() || registerPasswordText.isEmpty() || registerConfirmPasswordText.isEmpty())
                {
                    Toast.makeText(Register_Activity.this, "Please make sure all fields are filled in.", Toast.LENGTH_SHORT).show();
                }else{
                    if (registerPasswordText.equals(registerConfirmPasswordText)){
                        credentials.addCredentials(registerEmailText, registerPasswordText);
                        // Register the user into the database
                        // TODO
                        // Register the user into Firebase
                        firebaseAuth.createUserWithEmailAndPassword(registerEmailText, registerPasswordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Add to the sharedpreferences database
                                    sharedPreferencesEditor.putString(registerEmailText, registerPasswordText);
                                    // Must run apply to push and committed to the file
                                    sharedPreferencesEditor.apply();
                                    sendEmailVerification();

                                    // Add username, email, password and telegram handle as a User class into the online firebase database
                                    User user = new User(registerEmailText, registerNameText, registerPasswordText, registerTelegramHandleText);
                                    db.adduser(user);
                                }else{
                                    Toast.makeText(Register_Activity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, Login_Activity.class);
        startActivity(intent);

    }

    // This sends a verification email to ensure that users are genuine and to prvent spam users
    // Users now have to verify their emails when registering for an account
    // They will not be able to login until their account is verified
    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Register_Activity.this, "Registration Successful. Verification mail has been sent.", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        openLoginActivity();
                    }else{
                        Toast.makeText(Register_Activity.this, "Registration Failed. Verification mail has not been sent.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
