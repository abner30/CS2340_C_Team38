package com.example.myproject.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    /**
     * editTextEmail stores value from text input.
     */
    private EditText editTextEmail;
    /**
     * editTextPassword stores value from text input.
     */
    private EditText editTextPassword;
    /**
     * buttonLogin use to complete login.
     */
    private Button buttonLogin;
    /**
     * mAuth is variable for firebase authentication.
     */
    private FirebaseAuth mAuth;
    /**
     * buttonRegister use to move to RegistrationActivity.
     */
    private Button buttonRegister;

    /**
     * If user already exists/logged in, then skip login process.
     */
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    /**
     * The method runs on create. It controls the whole activity.
     * @param savedInstanceState
     */
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        buttonRegister = findViewById(R.id.btn_register_switch);

        //If Create an Account is clicked, move to RegistrationActivity
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getApplicationContext(),
                        RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //If Login is clicked, extract info from textboxes to complete Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String email = String.valueOf(editTextEmail.getText());
                String password = String.valueOf(editTextPassword.getText());
                //check valid email
                if (TextUtils.isEmpty(email) || email.contains(" ")) {
                    Toast.makeText(LoginActivity.this,
                            "Enter valid email",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //check valid password
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this,
                            "Enter valid password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(
                                            @NonNull final
                                            Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success
                                            Toast.makeText(LoginActivity.this,
                                                    "Login Successfull.",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(
                                                    getApplicationContext(),
                                                    NavBar.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            // If sign in fails
                                            Toast.makeText(LoginActivity.this,
                                                    "Login Failed.",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });


            }
        });
    }
}