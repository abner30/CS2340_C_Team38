package com.example.myproject.view.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myproject.R;
import com.example.myproject.database.DatabaseManager;
import com.example.myproject.viewmodel.TravelCommunityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Fragment for displaying and managing travel community posts.
 * This fragment allows users to view existing travel posts and create new ones.
 * It integrates with Firebase to store and retrieve post data.
 */
public class TravelCommunityFragment extends Fragment {
    /** ViewModel for handling business logic. */
    private TravelCommunityViewModel viewModel = new TravelCommunityViewModel();

    /** Reference to the Firebase database. */
    private DatabaseReference database;

    /** Container for displaying travel posts. */
    private LinearLayout postsContainer;

    /** Current user's unique identifier. */
    private String currentUserUid;

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views
     * @param container If non-null, this is the parent view that the fragment's UI attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state
     * @return The View for the fragment's UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_community, container, false);

        initializeComponents(view);
        return view;
    }

    /**
     * Initializes all components of the fragment.
     *
     * @param view The root view of the fragment
     */
    private void initializeComponents(View view) {
        currentUserUid = DatabaseManager.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference().child("travelCommunity");

        postsContainer = view.findViewById(R.id.posts_container);
        FloatingActionButton addPostButton = view.findViewById(R.id.btn_add_post);
        ScrollView scrollView = view.findViewById(R.id.scroll_view);

        addPostButton.setOnClickListener(v -> showAddPostDialog());
        loadPosts();
    }

    /**
     * Displays a dialog for creating a new travel post.
     * Includes form fields for destination, dates, transportation, and notes.
     */
    private void showAddPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Create Travel Post");

        LinearLayout layout = createDialogLayout();
        builder.setView(layout);

        final EditText[] inputs = createDialogInputs(layout);

        builder.setPositiveButton("Post", (dialog, which) -> {
            validateAndCreatePost(inputs);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Creates and configures the layout for the post creation dialog.
     *
     * @return Configured LinearLayout for the dialog
     */
    private LinearLayout createDialogLayout() {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 10, 20, 10);
        return layout;
    }

    /**
     * Creates and adds input fields to the dialog layout.
     *
     * @param layout The layout to add inputs to
     * @return Array of EditText fields in order: destination, startDate, endDate, transportation,
     * notes
     */
    private EditText[] createDialogInputs(LinearLayout layout) {
        EditText[] inputs = new EditText[7];
        String[] hints = {"Destination", "Start Date (MM/DD/YYYY)", "End Date (MM/DD/YYYY)",
                 "Transportation Details", "Accommodations", "Dining", "Trip Notes and Reflections"};

        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = new EditText(getActivity());
            inputs[i].setHint(hints[i]);
            layout.addView(inputs[i]);
        }

        return inputs;
    }

    /**
     * Validates input fields and creates a new post if validation passes.
     *
     * @param inputs Array of EditText fields containing post data
     */
    private void validateAndCreatePost(EditText[] inputs) {
        String destination = inputs[0].getText().toString().trim();
        String startDate = inputs[1].getText().toString().trim();
        String endDate = inputs[2].getText().toString().trim();
        String transportation = inputs[3].getText().toString().trim();
        String accommodations = inputs[4].getText().toString().trim();
        String dinings = inputs[5].getText().toString().trim();
        String notes = inputs[6].getText().toString().trim();

        if (validateInputs(destination, startDate, endDate)) {
            createTravelPost(destination, startDate, endDate, accommodations, dinings, transportation, notes);
        }
    }

    /**
     * Validates the required input fields for a travel post.
     *
     * @param destination The destination location
     * @param startDate The start date of the trip
     * @param endDate The end date of the trip
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInputs(String destination, String startDate, String endDate) {
        if (destination.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!viewModel.isValidDateFormat(startDate) || !viewModel.isValidDateFormat(endDate)) {
            Toast.makeText(getContext(), "Please use MM/DD/YYYY date format",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!viewModel.isValidDateRange(startDate, endDate)) {
            Toast.makeText(getContext(), "End date must be after start date",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Creates a new travel post in the database.
     *
     * @param destination The destination location
     * @param startDate The start date of the trip
     * @param endDate The end date of the trip
     * @param transportation The transportation details
     * @param notes User notes and reflections about the trip
     */
    private void createTravelPost(String destination, String startDate, String endDate,
                                  String accommodations, String dinings, String transportation,
                                  String notes) {
        Map<String, Object> postData = new HashMap<>();
        postData.put("userId", currentUserUid);
        postData.put("destination", destination);
        postData.put("startDate", startDate);
        postData.put("endDate", endDate);
        postData.put("transportation", transportation);
        postData.put("accommodations", accommodations);
        postData.put("dinings", dinings);
        postData.put("notes", notes);
        postData.put("timestamp", ServerValue.TIMESTAMP);

        String postId = database.push().getKey();
        if (postId != null) {
            database.child(postId).setValue(postData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Post created successfully",
                                Toast.LENGTH_SHORT).show();
                        loadPosts();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(),
                            "Error creating post: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Loads and displays all travel posts from the database.
     * If no posts exist, creates and displays a default post.
     */
    private void loadPosts() {
        postsContainer.removeAllViews();

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists() || !snapshot.hasChildren()) {
                    addDefaultPost();
                    return;
                }

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    createPostView(postSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading posts: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Creates and adds a view for a single travel post.
     *
     * @param postSnapshot The DataSnapshot containing the post data
     */
    private void createPostView(DataSnapshot postSnapshot) {
        LinearLayout postView = new LinearLayout(getContext());
        postView.setOrientation(LinearLayout.VERTICAL);
        postView.setPadding(16, 16, 16, 16);

        String[] fields = {"destination", "startDate", "endDate", "transportation",
                "accommodations", "dinings", "notes"};
        String[] labels = {"Destination: ", "Start Date: ", "End Date: ",
                "Transportation: ", "Accommodations", "Destinations:", "Notes: "};

        for (int i = 0; i < fields.length; i++) {
            String value = postSnapshot.child(fields[i]).getValue(String.class);
            if (value != null) {
                addDetailToPost(postView, labels[i] + value);
            }
        }

        View divider = createDivider();
        postsContainer.addView(postView);
        postsContainer.addView(divider);
    }

    /**
     * Creates a divider view to separate posts.
     *
     * @return The created divider View
     */
    private View createDivider() {
        View divider = new View(getContext());
        divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 2));
        return divider;
    }

    /**
     * Adds a text detail to a post view.
     *
     * @param container The container to add the detail to
     * @param text The text to display
     */
    private void addDetailToPost(LinearLayout container, String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(0, 4, 0, 4);
        container.addView(textView);
    }

    /**
     * Creates and adds a default post to the database when no posts exist.
     */
    private void addDefaultPost() {
        Map<String, Object> defaultPost = new HashMap<>();
        defaultPost.put("userId", "default");
        defaultPost.put("destination", "Paris, France");
        defaultPost.put("startDate", "12/01/2024");
        defaultPost.put("endDate", "12/15/2024");
        defaultPost.put("transportation", "Air France Flight 123");
        defaultPost.put("notes", "Beautiful city with amazing cuisine and culture!");
        defaultPost.put("timestamp", ServerValue.TIMESTAMP);

        database.child("default").setValue(defaultPost)
                .addOnSuccessListener(aVoid -> loadPosts());
    }
}