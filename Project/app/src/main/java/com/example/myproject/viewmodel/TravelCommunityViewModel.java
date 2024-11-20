package com.example.myproject.viewmodel;

import androidx.annotation.NonNull;
import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.TravelCommunity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/**
 * ViewModel class for managing Travel Community data operations.
 */
public class TravelCommunityViewModel {

    /**
     * Reference to the Firebase Realtime Database for travel community posts.
     * */
    private DatabaseReference database;

    /**
     * Initializes the ViewModel and sets up the database reference.
     */
    public TravelCommunityViewModel() {
        database = DatabaseManager.getInstance().getReference().child("communityTravel");
    }

    /**
     * Adds a new travel post to the Firebase database.
     * @param post The TravelCommunity object
     * @param callback Callback to handle the operation
     */
    public void addTravelPost(TravelCommunity post, DatabaseCallback callback) {
        String postId = database.push().getKey();
        if (postId != null) {
            database.child(postId).setValue(post)
                    .addOnSuccessListener(aVoid -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onError(e.getMessage()));
        }
    }

    /**
     * Retrieves all travel posts from the database.
     * @param callback Callback to handle the retrieved data or any errors
     */
    public void getTravelPosts(final DataCallback callback) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<TravelCommunity> posts = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    TravelCommunity post = postSnapshot.getValue(TravelCommunity.class);
                    if (post != null) {
                        posts.add(post);
                    }
                }
                callback.onDataReceived(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    /**
     * Interface for handling database operation callbacks.
     */
    public interface DatabaseCallback {
        /**
         * Called when a database operation is successful.
         */
        void onSuccess();

        /**
         * Called when a database operation fails.
         * @param error The error message describing what went wrong
         */
        void onError(String error);
    }

    /**
     * Interface for handling data retrieval callbacks.
     */
    public interface DataCallback {
        /**
         * Called when data is successfully retrieved from the database.
         * @param posts ArrayList of TravelCommunity objects retrieved from the database
         */
        void onDataReceived(ArrayList<TravelCommunity> posts);

        /**
         * Called when data retrieval fails.
         * @param error The error message describing what went wrong
         */
        void onError(String error);
    }
}

