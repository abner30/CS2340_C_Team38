package com.example.myproject.database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {
    private static DatabaseManager instance;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private DatabaseManager() {
        // initializing database reference.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // initializing firebase connection
        firebaseAuth = FirebaseAuth.getInstance();
        // additional firebase config
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    // method to get the Firebase Database reference.
    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    // method to get the FirebaseAuth instance.
    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    // method to get the current authenticated user.
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    // method to log in a user with email and password.
    public void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    // method to create a new user with email and password.
    public void createUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    // not sure if we need this
    public void closeConnection() {
        instance = null;
    }
}
