package com.example.sprint1test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button registerButton, switchToLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        switchToLoginButton = findViewById(R.id.switchToLoginButton);

        // Set up register button 
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

        // Go to LoginActivity
        switchToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleRegister() {
        Toast.makeText(CreateAccountActivity.this, "Register button clicked!", Toast.LENGTH_SHORT).show();
    }
}
