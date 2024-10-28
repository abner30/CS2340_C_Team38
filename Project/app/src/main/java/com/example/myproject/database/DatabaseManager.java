package com.example.myproject.database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Singleton class to manage Firebase Database and Firebase Authentication connections
 * for a single access point across the application.
 */
public class DatabaseManager {
    // Singleton instance
    private static DatabaseManager instance;

    // Reference to the Firebase Realtime Database
    private DatabaseReference databaseReference;

    // Firebase Authentication instance
    private FirebaseAuth firebaseAuth;

    /**
     * Private constructor to initialize Firebase Database and Authentication.
     * Ensures only one instance of DatabaseManager exists.
     */
    private DatabaseManager() {
        // Initialize the database reference pointing to the root of the database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize the Firebase authentication instance
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Provides a synchronized, thread-safe way to get the singleton instance of DatabaseManager.
     *
     * @return The single instance of DatabaseManager.
     */
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

    /**
     * Provides access to the Firebase Database reference, allowing database operations.
     *
     * @return The root DatabaseReference for Firebase Realtime Database.
     */
    public DatabaseReference getReference() {
        return databaseReference;
    }

    /**
     * Provides access to the Firebase Authentication instance for managing user authentication.
     *
     * @return The FirebaseAuth instance for the app.
     */
    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    /**
     * Retrieves the currently authenticated user, if there is one.
     *
     * @return The FirebaseUser object representing the authenticated user,
     *         or null if no user is currently authenticated.
     */
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    /**
     * Logs in a user using their email and password.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     * @param listener An OnCompleteListener to handle success or failure of the login operation.
     */
    public void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    /**
     * Creates a new user account with email and password.
     *
     * @param email    The new user's email address.
     * @param password The new user's password.
     * @param listener An OnCompleteListener to handle success or failure of the registration operation.
     */
    public void createUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    /**
     * Clears the singleton instance, effectively "closing" the DatabaseManager connection.
     * Useful for if we need to reset the instance for whatever reason.
     */
    public void closeConnection() {
        instance = null;
    }
}
