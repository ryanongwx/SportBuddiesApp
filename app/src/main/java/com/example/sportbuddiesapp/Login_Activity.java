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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class Login_Activity extends AppCompatActivity {

    private EditText email;
    private EditText Password;
    private Button loginBtn;
    private TextView resetPwText;
    private TextView registerText;
    private FirebaseAuth firebaseAuth;

    Credentials credentials;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        resetPwText = findViewById(R.id.resetPwText);
        registerText = findViewById(R.id.registerText);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){
            finish();
            openMainActivity();
        }

        credentials = new Credentials();

        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        if(sharedPreferences != null){

            Map<String, ?> preferencesMap = sharedPreferences.getAll();

            if (preferencesMap.size() != 0){
                credentials.loadCredentials(preferencesMap);
            }

            String savedUsername = sharedPreferences.getString("LastSavedUsername", "");
            String savedPassword = sharedPreferences.getString("LastSavedPassword", "");
            email.setText(savedUsername);
            Password.setText(savedPassword);

        }

        // Set what is to happen when the login button is clicked
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain the username and password input as strings
                String inputEmail = email.getText().toString();
                String inputPassword = Password.getText().toString();

                // Toast serve error message if username or password is not filled in
                if(inputEmail.isEmpty() || inputPassword.isEmpty())
                {
                    Toast.makeText(Login_Activity.this, "Please make sure both Email and Passwords are filled in", Toast.LENGTH_SHORT).show();
                }else{
                    // Validate Credentials
                    validate(inputEmail, inputPassword);
                }

            }
        });

        // Set what is to happen when register button is clicked
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();

            }
        });

        // Set what is to happen when reset button is clicked
        resetPwText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResetPwActivity();

            }
        });
    }

    private void validate(String username, String password){
        // In the real implementation we will retrieve username and password from a database and check against the credentials on the db
        // TODO
        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    checkEmailVerification();
                    sharedPreferencesEditor.putString("LastSavedUsername", username);
                    sharedPreferencesEditor.putString("LastSavedPassword", password);
                    sharedPreferencesEditor.apply();
                }else{
                    Toast.makeText(Login_Activity.this, "Username and/or Password is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, Register_Activity.class);
        startActivity(intent);

    }

    private void openResetPwActivity() {
        Intent intent = new Intent(this, Reset_Password_Activity.class);
        startActivity(intent);

    }

    // This verifies whether the user of the account has verified their account via email
    // Users now have to verify their emails when registering for an account
    // They will not be able to login until their account is verified
    private void checkEmailVerification(){
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = user.isEmailVerified();
        if(emailflag){
            Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            finish();
            openMainActivity();
        }else{
            Toast.makeText(Login_Activity.this, "Verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}