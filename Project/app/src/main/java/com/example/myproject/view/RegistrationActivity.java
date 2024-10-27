
package com.example.myproject.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myproject.R;
import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;


public class RegistrationActivity extends AppCompatActivity {

    /**
     * editTextEmail stores value from textbox of email.
     */
    private TextInputEditText editTextEmail;
    /**
     * editTextEmail stores value from textbox of password.
     */
    private TextInputEditText editTextPassword;
    /**
     * buttonReg is a button that registers users when clicked.
     */
    private Button buttonReg;
    /**
     * buttonLogin is the button to move from registration page to login page.
     */
    private Button buttonLogin;

    /**
     * The method runs on create. It controls the whole activity.
     * @param savedInstanceState the saved instance state
     */
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        buttonLogin = findViewById(R.id.btn_login_switch);

        //If Login clicked, move to Login Activity
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //Adds functionality to register button.
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String email = String.valueOf(editTextEmail.getText());
                String password = String.valueOf(editTextPassword.getText());
                //check valid email
                if (TextUtils.isEmpty(email) || email.contains(" ")) {
                    Toast.makeText(RegistrationActivity.this,
                            "Enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                //check valid password
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegistrationActivity.this,
                            "Enter valid password", Toast.LENGTH_SHORT).show();
                    return;
                }
                OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(
                            @NonNull final
                            Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            if (firebaseUser != null) {
                                User user = new User(firebaseUser.getUid(),
                                        firebaseUser.getEmail());
                                DatabaseManager.getInstance().getReference()
                                        .child("users").child(user.getUid()).setValue(user);
                            }
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    "Account created",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // display message if register fails
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                DatabaseManager.getInstance().createUser(email, password, listener);
            }
        });
    }
}
